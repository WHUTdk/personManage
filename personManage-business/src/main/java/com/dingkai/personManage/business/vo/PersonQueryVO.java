package com.dingkai.personManage.business.vo;

import lombok.Data;

/**
 * @Author dingkai
 * @Date 2020/7/12 19:44
 */
@Data
public class PersonQueryVO {

    private Integer pageNo = 1;

    private Integer pageSize = 10;

    private String name;

    private String idNumber;

    private Integer sex;

    private String keyword;

}
