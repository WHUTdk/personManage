package com.dingkai.personManage.security.filter;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.dingkai.personManage.common.response.BaseResult;
import com.dingkai.personManage.security.domain.UserDO;
import com.dingkai.personManage.security.exception.MyUsernameNotFoundException;
import com.dingkai.personManage.security.utils.JwtTokenUtils;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * @Author dingkai
 * @Date 2020/7/14 21:57
 */
public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private static final Logger logger = LoggerFactory.getLogger(JwtAuthenticationFilter.class);

    private final AuthenticationManager authenticationManager;

    public JwtAuthenticationFilter(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
        super.setFilterProcessesUrl("/user/login");
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws AuthenticationException {

        //获取请求体中的用户名密码
        Authentication authenticate = null;
        try {
            String body = IOUtils.toString(httpServletRequest.getInputStream(), StandardCharsets.UTF_8);
            JSONObject jsonObject = JSON.parseObject(body);
            String username = jsonObject.getString("username");
            String password = jsonObject.getString("password");
            authenticate = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(username, password)
            );
        } catch (IOException e) {
            logger.error("获取用户名密码出错，错误信息：{}", e.getMessage());
        }
        return authenticate;
    }

    // 成功验证后调用的方法
    // 如果验证成功，就生成token并返回
    @Override
    protected void successfulAuthentication(HttpServletRequest request,
                                            HttpServletResponse response,
                                            FilterChain chain,
                                            Authentication authResult) throws IOException, ServletException {

        // 查看源代码会发现调用getPrincipal()方法会返回一个实现了`UserDetails`接口的对象
        UserDO userDO = (UserDO) authResult.getPrincipal();
        String token = JwtTokenUtils.createToken(userDO.getUsername(), false);
        // 返回创建成功的token
        // 这里创建的token只是单纯的token，按照jwt的规定，最后请求的格式应该是 `Bearer token`
        //response.setHeader("token", JwtTokenUtils.TOKEN_PREFIX + token);
        response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
        BaseResult<String> baseResult = BaseResult.success(JwtTokenUtils.TOKEN_PREFIX + token);
        response.getWriter().write(JSON.toJSONString(baseResult));
    }

    // 这是验证失败时候调用的方法
    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException e) throws IOException {
        BaseResult<Object> baseResult = BaseResult.error("-2", "authentication failed, reason: " + e.getMessage());
        if (e instanceof BadCredentialsException) {
            baseResult = BaseResult.error("-2", "密码错误");
        } else if (e instanceof InternalAuthenticationServiceException) {
            baseResult = BaseResult.error("-2", "用户不存在");
        }
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
        response.getWriter().write(JSON.toJSONString(baseResult));
    }


}
