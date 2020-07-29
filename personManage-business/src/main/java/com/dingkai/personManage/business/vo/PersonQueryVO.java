package com.dingkai.personManage.business.vo;

import lombok.Data;

import javax.validation.constraints.Min;

/**
 * @Author dingkai
 * @Date 2020/7/12 19:44
 */
@Data
public class PersonQueryVO {

    @Min(value = 1, message = "pageNo不能小于1")
    private Integer pageNo = 1;

    @Min(value = 1, message = "pageSize不能小于1")
    private Integer pageSize = 10;

    private String name;

    private String idNumber;

    private Integer sex;

    private String keyword;

}
