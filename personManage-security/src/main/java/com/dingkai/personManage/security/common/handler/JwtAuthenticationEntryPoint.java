package com.dingkai.personManage.security.common.handler;

import com.alibaba.fastjson.JSON;
import com.dingkai.personManage.common.response.BaseResult;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @Author dingkai
 * @Date 2020/7/14 23:30
 */
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

    /**
     * 处理403返回信息
     */
    @Override
    public void commence(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AuthenticationException e) throws IOException, ServletException {
        BaseResult<Object> baseResult = BaseResult.error("-2", "authentication failed");
        httpServletResponse.setStatus(HttpServletResponse.SC_FORBIDDEN);
        httpServletResponse.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
        httpServletResponse.getWriter().write(JSON.toJSONString(baseResult));
    }
}
