package com.dingkai.personManage.api.service;

import com.dingkai.personManage.common.response.BaseResult;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

/**
 * @Author dingkai
 * @Date 2020/7/29 21:51
 */

@Path("/personService")
@Produces({MediaType.APPLICATION_JSON})
@Consumes({MediaType.APPLICATION_JSON})
public interface PersonRestService {

    @GET
    @Path("/getPersonInfoByIdNumber")
    BaseResult getPersonInfoByIdNumber(@QueryParam("idNumber")
                                       @NotBlank(message = "身份证号不能为空") String idNumber);

}
