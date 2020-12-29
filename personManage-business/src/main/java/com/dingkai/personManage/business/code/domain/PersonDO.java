package com.dingkai.personManage.business.code.domain;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * @Author dingkai
 * @Date 2020/7/12 18:27
 */
@Data
@TableName("person_info")
public class PersonDO {

    @TableId(type = IdType.AUTO)
    private Integer id;

    private String name;

    private String idNumber;

    private Integer sex;//0未知；1男；2女

    private String ethnicity;//民族

    private LocalDate birthday;

    private String residentialAddress;//居住地址

    private String householdAddress;//户籍地址

    @TableField(value = "create_time",fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @TableField(value = "update_time",fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    @TableField(fill = FieldFill.INSERT)
    private String createId;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private String operateId;

}
