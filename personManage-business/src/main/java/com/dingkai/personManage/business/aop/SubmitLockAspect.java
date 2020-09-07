package com.dingkai.personManage.business.aop;

import com.dingkai.personManage.business.annotation.SubmitLock;
import com.dingkai.personManage.business.config.RequestHolder;
import com.dingkai.personManage.business.filter.RequestWrapper;
import com.dingkai.personManage.business.utils.IpUtil;
import com.dingkai.personManage.business.utils.RedisUtil;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

/**
 * @Author dingkai
 * @Date 2020/8/18 22:18
 */
@Component
@Aspect
public class SubmitLockAspect {

    private static final Logger logger = LoggerFactory.getLogger(SubmitLockAspect.class);

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Pointcut("@annotation(com.dingkai.personManage.business.annotation.SubmitLock)")
    public void pointCut() {
    }

    @Before("pointCut()")
    public void around(JoinPoint joinPoint) {
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        String methodName = methodSignature.getName();
        String className = methodSignature.getDeclaringTypeName();
        SubmitLock submitLock = methodSignature.getMethod().getAnnotation(SubmitLock.class);
        long lockTime = submitLock.lockTime();
        if (lockTime <= 0) {
            lockTime = 2;
        }
        RequestWrapper request = RequestHolder.getRequest();
        String ipAddress = IpUtil.getIpAddress(request);
        String lockKey = className + "." + methodName + ":" + ipAddress;
        //key不存在 就返回true 并保存key
        Boolean success = redisTemplate.opsForValue().setIfAbsent(lockKey, "");
        if (success != null && success) {
            redisTemplate.expire(lockKey, lockTime, submitLock.timeUnit());
        } else {
            throw new RuntimeException("请勿重复提交");
        }
    }

}