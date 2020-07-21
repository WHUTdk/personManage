package com.dingkai.personManage.business.filter;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ReadListener;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;

/**
 * @Author dingkai
 * @Date 2020/7/13 1:01
 */
public class RequestWrapper extends HttpServletRequestWrapper {

    private static final Logger logger = LoggerFactory.getLogger(RequestWrapper.class);

    /**
     * 请求体
     */
    private final String body;

    public RequestWrapper(HttpServletRequest request) {
        super(request);
        // 将输入流存储在body中
        body = getBody(request);
    }

    /**
     * 获取请求体
     *
     * @param request 请求
     * @return 请求体
     */
    private String getBody(HttpServletRequest request) {
        try {
            return IOUtils.toString(request.getInputStream(), "UTF-8");
        } catch (IOException e) {
            logger.debug("请求体转换出错，出错信息：{}" + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    @Override
    public BufferedReader getReader() throws IOException {
        return new BufferedReader(new InputStreamReader(getInputStream()));
    }

    @Override
    public ServletInputStream getInputStream() throws IOException {
        // 创建新的字节数组输入流，每次调用此方法，都相当于重新将body转为输入流
        final ByteArrayInputStream bais = new ByteArrayInputStream(body.getBytes(Charset.defaultCharset()));

        return new ServletInputStream() {
            @Override
            public boolean isFinished() {
                return false;
            }

            @Override
            public boolean isReady() {
                return false;
            }

            @Override
            public void setReadListener(ReadListener readListener) {

            }

            @Override
            public int read() throws IOException {
                return bais.read();
            }
        };
    }

    public String getBody() {
        return body;
    }

}
