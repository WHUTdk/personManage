package com.dingkai.personManage.business.code.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * @Author dingkai
 * @Date 2020/7/18 23:19
 */
@Data
@TableName("dictionary_info")
public class DictionaryDo {

    @TableId(type = IdType.AUTO)
    private Integer id;

    private String groupName;

    private String code;

    private String name;

}
