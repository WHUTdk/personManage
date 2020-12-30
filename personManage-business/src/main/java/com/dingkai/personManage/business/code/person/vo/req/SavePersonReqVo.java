package com.dingkai.personManage.business.code.person.vo.req;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDate;

/**
 * @author dingkai1
 * @desc
 * @date 2020/12/30 9:58
 */
@Data
@ApiModel(value = "保存人员vo")
public class SavePersonReqVo {

    @ApiModelProperty(value = "id,空：保存；不空：更新", position = 1)
    private Integer id;

    @ApiModelProperty(value = "姓名", example = "丁凯", position = 2)
    private String name;

    @ApiModelProperty(value = "身份证号", example = "420704199509045053", position = 3)
    private String idNumber;

    @ApiModelProperty(value = "性别，0未知；1男；2女", example = "1", position = 4)
    private Integer sex;//0未知；1男；2女

    @ApiModelProperty(value = "民族", example = "01", position = 5)
    private String ethnicity;//民族

    @ApiModelProperty(value = "出生日期", example = "1995-09-04", position = 6)
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate birthday;

    @ApiModelProperty(value = "居住地址", example = "湖北省武汉市江夏区", position = 7)
    private String residentialAddress;//居住地址

    @ApiModelProperty(value = "户籍地址", example = "湖北省武汉市江夏区", position = 8)
    private String householdAddress;//户籍地址

}
