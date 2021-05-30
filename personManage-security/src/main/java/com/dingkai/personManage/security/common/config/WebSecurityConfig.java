package com.dingkai.personManage.security.common.config;

import com.dingkai.personManage.security.common.filter.JwtAuthenticationFilter;
import com.dingkai.personManage.security.common.filter.JwtAuthorizationFilter;
import com.dingkai.personManage.security.common.handler.JwtAuthenticationEntryPoint;
import com.dingkai.personManage.security.common.handler.MyLogoutSuccessHandler;
import com.dingkai.personManage.security.code.user.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.List;

/**
 * @Author dingkai
 * @Date 2020/7/14 20:57
 */
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Value("${no.login.url}")
    private String noLoginUrl;

    @Autowired
    private UserService userService;

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userService).passwordEncoder(bCryptPasswordEncoder());
    }


    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.cors().and()
                .csrf().disable()
                .authorizeRequests()
                .antMatchers("/js/**", "/css/**", "/images/*", "/fonts/**").permitAll()
                .antMatchers("/user/login", "/user/register").permitAll()
                .antMatchers("/swagger-resources/**", "/webjars/**", "/v2/**", "/swagger-ui.html/**").permitAll()
                .antMatchers("/service/rs/**").permitAll()
                .antMatchers(getNoLoginUrl()).permitAll()
                .anyRequest().authenticated()
                .and()
                .addFilter(new JwtAuthenticationFilter(authenticationManager()))
                .addFilter(new JwtAuthorizationFilter(authenticationManager()))
                // 不需要session
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .logout().logoutUrl("/user/logout")
                .logoutSuccessHandler(new MyLogoutSuccessHandler()).permitAll()
                .and()
                .exceptionHandling().authenticationEntryPoint(new JwtAuthenticationEntryPoint());
    }

    private String[] getNoLoginUrl(){
        String[] split={};
        if(StringUtils.isBlank(noLoginUrl)){
            return split;
        }
        split = noLoginUrl.split(",");
        return split;
    }

}
