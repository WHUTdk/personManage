package com.dingkai.personManage.api.config;

import com.dingkai.personManage.common.response.BaseResult;

import javax.validation.ValidationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

/**
 * @Author dingkai
 * @Date 2020/7/30 0:17
 */
@Provider
public class ValidationExceptionMapper implements ExceptionMapper<ValidationException> {

    /**
     * jersey的参数校验异常
     */
    @Override
    public Response toResponse(ValidationException e) {
        e.printStackTrace();
        return Response.status(Response.Status.OK).type(MediaType.APPLICATION_JSON)
                .entity(BaseResult.error("-1", e.getMessage())).build();
    }
}
