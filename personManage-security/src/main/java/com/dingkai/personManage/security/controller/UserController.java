package com.dingkai.personManage.security.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.dingkai.personManage.common.response.BaseResult;
import com.dingkai.personManage.security.dao.UserMapper;
import com.dingkai.personManage.security.domain.UserDO;
import com.dingkai.personManage.security.vo.UserVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
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
@Api(tags = "用户管理")
@RestController
@RequestMapping("/user")
public class UserController {

    @Resource
    private UserMapper userMapper;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @ApiOperation(value = "用户注册")
    @PostMapping("/register")
    public BaseResult registerUser(@RequestBody UserVO userVO) {
        try {
            if (StringUtils.isEmpty(userVO.getUsername()) || StringUtils.isEmpty(userVO.getPassword())) {
                return BaseResult.error("-1", "用户名或密码不能为空");
            }
            UserDO one = userMapper.selectOne(new QueryWrapper<UserDO>().eq("username", userVO.getUsername()));
            if (one != null) {
                return BaseResult.error("-1", "用户名已存在");
            }
            UserDO userDO = new UserDO();
            userDO.setUsername(userVO.getUsername());
            userDO.setPassword(bCryptPasswordEncoder.encode(userVO.getPassword()));
            userMapper.insert(userDO);
            return BaseResult.success();
        } catch (Exception e) {
            return BaseResult.error("-1", e.getMessage());
        }
    }

    @ApiOperation(value = "用户登录")
    @PostMapping("/login")
    public BaseResult login(@RequestBody UserVO userVO) {


        return BaseResult.success();
    }

}
