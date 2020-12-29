package com.dingkai.personManage.business.common.annotation;

import java.lang.annotation.*;
import java.util.concurrent.TimeUnit;

/**
 * @Author dingkai
 * @Date 2020/8/18 22:17
 */
@Documented
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface SubmitLock {

    long lockTime() default 2;//默认2s

    TimeUnit timeUnit() default TimeUnit.SECONDS;

}
