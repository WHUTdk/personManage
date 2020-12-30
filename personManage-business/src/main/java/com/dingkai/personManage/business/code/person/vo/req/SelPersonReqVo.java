package com.dingkai.personManage.business.code.person.vo.req;

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
public class SelPersonReqVo {

    @Min(value = 1, message = "pageNo不能小于1")
    @ApiModelProperty(example = "1", position = 1)
    private Integer pageNo = 1;

    @Min(value = 1, message = "pageSize不能小于1")
    @ApiModelProperty(example = "10", position = 2)
    private Integer pageSize = 10;

    @ApiModelProperty(value = "姓名", example = "丁凯", position = 3)
    private String name;

    @ApiModelProperty(value = "身份证号", example = "420704199509045053", position = 4)
    private String idNumber;

    @ApiModelProperty(value = "性别，0未知 1男 2女", position = 5)
    private Integer sex;

    @ApiModelProperty(value = "关键字", example = " ", position = 6)
    private String keyword;

}
