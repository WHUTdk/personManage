package com.dingkai.personManage.security.controller;

import com.dingkai.personManage.common.response.BaseResult;
import com.dingkai.personManage.security.dao.UserMapper;
import com.dingkai.personManage.security.domain.UserDO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Map;

/**
 * @Author dingkai
 * @Date 2020/7/14 23:09
 */
@RestController
@RequestMapping("/user")
public class UserController {

    @Resource
    private UserMapper userMapper;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @PostMapping("/register")
    public BaseResult registerUser(@RequestBody Map<String, String> registerUser) {
        UserDO userDO = new UserDO();
        userDO.setUsername(registerUser.get("username"));
        // 记得注册的时候把密码加密一下
        userDO.setPassword(bCryptPasswordEncoder.encode(registerUser.get("password")));
        userMapper.insert(userDO);
        return BaseResult.success();
    }


}
