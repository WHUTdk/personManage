package com.dingkai.personManage.security.code.user.service;

import com.dingkai.personManage.security.code.user.dao.UserMapper;
import com.dingkai.personManage.security.code.user.entity.UserDO;
import com.dingkai.personManage.security.common.exception.MyUsernameNotFoundException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @Author dingkai
 * @Date 2020/7/14 21:33
 */
@Service
public class UserService implements UserDetailsService {

    @Resource
    private UserMapper userMapper;

    @Override
    public UserDetails loadUserByUsername(String username) throws MyUsernameNotFoundException {
        if (StringUtils.isEmpty(username)) {
            throw new MyUsernameNotFoundException("用户名不能为空");
        }
        UserDO userDO = userMapper.selectById(username);
        if (userDO == null) {
            throw new MyUsernameNotFoundException("用户不存在");
        }
        return userDO;
    }
}
