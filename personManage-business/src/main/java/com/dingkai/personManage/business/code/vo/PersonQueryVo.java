package com.dingkai.personManage.business.code.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Min;

/**
 * @Author dingkai
 * @Date 2020/7/12 19:44
 */
@ApiModel("人员查询类")
@Data
public class PersonQueryVo {

    @Min(value = 1, message = "pageNo不能小于1")
    private Integer pageNo = 1;

    @Min(value = 1, message = "pageSize不能小于1")
    private Integer pageSize = 10;

    @ApiModelProperty("姓名")
    private String name;

    @ApiModelProperty("身份证号")
    private String idNumber;

    @ApiModelProperty("性别，1男 2女")
    private Integer sex;

    @ApiModelProperty("关键字")
    private String keyword;

}
