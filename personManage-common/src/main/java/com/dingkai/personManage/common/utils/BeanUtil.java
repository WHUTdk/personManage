package com.dingkai.personManage.common.utils;

import java.lang.reflect.Field;

/**
 * @Author dingkai
 * @Date 2020/8/2 22:56
 */
public class BeanUtil {

    public static <T> Field[] getFields(T t) {
        Class<?> clazz = t.getClass();
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            field.setAccessible(true);
        }
        return fields;
    }

}
