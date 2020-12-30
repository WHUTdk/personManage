package com.dingkai.personManage.security.code.user.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Author dingkai
 * @Date 2020/8/3 23:42
 */
@Data
@ApiModel("用户登录注册vo")
public class UserVO {

    @ApiModelProperty(value = "用户名", position = 1, example = "dingkai")
    private String username;

    @ApiModelProperty(value = "密码", position = 2, example = "dingkai")
    private String password;
}
