package com.dingkai.personManage.business.filter;

import com.dingkai.personManage.business.config.RequestHolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * @Author dingkai
 * @Date 2020/7/13 21:21
 */
@Component
@WebFilter(urlPatterns = "/**")
public class AccessFilter implements Filter {

    private static final Logger logger = LoggerFactory.getLogger(AccessFilter.class);

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        RequestWrapper requestWrapper = null;
        if (servletRequest instanceof HttpServletRequest) {
            //替换自定义包装类，解决输入流无法重复读取的问题
            HttpServletRequest request = (HttpServletRequest) servletRequest;
            String requestURI = request.getRequestURI();
            if (!requestURI.contains("import") && !requestURI.contains("upload") && !requestURI.contains("swagger")) {
                //排除上传、导入等方法
                requestWrapper = new RequestWrapper(request);
            }
        }
        if (requestWrapper == null) {
            filterChain.doFilter(servletRequest, servletResponse);
        } else {
            logger.debug("servletRequest成功替换为自定义requestWrapper");
            RequestHolder.setRequest(requestWrapper);
            filterChain.doFilter(requestWrapper, servletResponse);
        }

    }

    @Override
    public void destroy() {

    }
}
