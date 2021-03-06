package com.dingkai.personManage.business.common.aop;

import com.dingkai.personManage.business.common.annotation.SubmitLock;
import com.dingkai.personManage.business.common.config.RequestHolder;
import com.dingkai.personManage.business.common.filter.RequestWrapper;
import com.dingkai.personManage.common.utils.IpUtil;
import com.dingkai.personManage.common.utils.RedisUtil;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.UUID;

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

    @Autowired
    private RedisUtil redisUtil;

    @Pointcut("@annotation(com.dingkai.personManage.business.common.annotation.SubmitLock)")
    public void pointCut() {
    }

    @Around("pointCut()")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        String methodName = methodSignature.getName();
        String className = methodSignature.getDeclaringTypeName();
        SubmitLock submitLock = methodSignature.getMethod().getAnnotation(SubmitLock.class);
        long lockTime = submitLock.lockTime();
        if (lockTime <= 0) {
            lockTime = 5;
        }
        RequestWrapper request = RequestHolder.getRequest();
        String ipAddress = IpUtil.getIpAddress(request);
        String lockKey = className + "." + methodName + ":" + ipAddress;
        //释放锁时，校验value
        String requestId = UUID.randomUUID().toString();
        try {
            boolean lockFlag = redisUtil.getDistributedLock(lockKey, requestId, lockTime, submitLock.timeUnit());
            if (lockFlag) {
                return joinPoint.proceed();
            } else {
                throw new RuntimeException("请勿重复提交");
            }
        } finally {
            // value相同时才能删除成功
            redisUtil.safeUnLock(lockKey, requestId);
        }

    }

}
