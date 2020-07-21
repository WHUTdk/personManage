package com.dingkai.personManage.business.config;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.dingkai.personManage.business.filter.RequestWrapper;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * @Author dingkai
 * @Date 2020/7/13 22:14
 */
@Component
public class MybatisFillFieldHandler implements MetaObjectHandler {

    @Override
    public void insertFill(MetaObject metaObject) {
        setFieldValByName("createTime", LocalDateTime.now(), metaObject);
        setFieldValByName("updateTime", LocalDateTime.now(), metaObject);
        setFieldValByName("createId", getUsername(), metaObject);
        setFieldValByName("operateId", getUsername(), metaObject);
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        setFieldValByName("updateTime", LocalDateTime.now(), metaObject);
        setFieldValByName("operateId", getUsername(), metaObject);
    }

    private String getUsername() {
        RequestWrapper request = RequestHolder.getRequest();
        try {
            return request.getUserPrincipal().getName();
        } catch (Exception e) {
            return "";
        }
    }

}
