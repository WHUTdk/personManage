package com.dingkai.personManage.security.common.exception;

import org.springframework.security.core.AuthenticationException;

/**
 * @Author dingkai
 * @Date 2020/7/15 0:02
 */
public class MyUsernameNotFoundException extends AuthenticationException {
    public MyUsernameNotFoundException(String msg) {
        super(msg);
    }

    public MyUsernameNotFoundException(String msg, Throwable t) {
        super(msg, t);
    }

}
