package com.dingkai.personManage.api.config;

import com.dingkai.personManage.common.response.BaseResult;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

/**
 * @Author dingkai
 * @Date 2020/7/30 0:08
 */
@Provider
public class JerseyExceptionMapper implements ExceptionMapper<Exception> {

    /**
     * jersey统一异常处理类，必须在jersey配置的扫描路径下
     */
    @Override
    public Response toResponse(Exception e) {
        e.printStackTrace();
        return Response.status(Response.Status.OK).type(MediaType.APPLICATION_JSON)
                .entity(BaseResult.error("-1", e.getMessage())).build();
    }
}
