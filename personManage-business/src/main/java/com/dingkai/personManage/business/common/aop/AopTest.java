package com.dingkai.personManage.business.common.aop;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * @Author dingkai
 * @Date 2021/6/13 14:17
 */
@Component
@Aspect
public class AopTest {

    private static final Logger logger = LoggerFactory.getLogger(AopTest.class);

    @Pointcut("execution(* com.dingkai.personManage.business.common.heartbeat.task.HeartbeatTask.initHeartbeat())")
    public void pointCut() {
    }

    @Around("pointCut()")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        logger.info("AOP测试，around前方法");
        Object proceed = joinPoint.proceed();
        logger.info("AOP测试，around后方法");
        return proceed;
    }

    @Before("pointCut()")
    public void before(JoinPoint joinPoint) throws Throwable {
        logger.info("AOP测试，before方法");
    }

    @After("pointCut()")
    public void after(JoinPoint joinPoint) throws Throwable {
        logger.info("AOP测试，after方法");
    }

    @AfterReturning(value = "pointCut()", returning = "result")
    public Object afterReturning(JoinPoint point, Object result) {
        logger.info("AOP测试，afterReturning方法,result:{}", result);
        return result;
    }

    @AfterThrowing(value = "pointCut()", throwing = "ex")
    public void afterThrowing(JoinPoint point, Throwable ex) {
        logger.info("AOP测试，afterThrowing方法,ex:{}", ex.toString());
    }


}
