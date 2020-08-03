package com.dingkai.personManage.security.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Author dingkai
 * @Date 2020/8/3 23:42
 */
@Data
public class UserVO {

    @ApiModelProperty(value = "用户名",position = 0)
    private String username;

    @ApiModelProperty(value = "密码",position = 1)
    private String password;
}
