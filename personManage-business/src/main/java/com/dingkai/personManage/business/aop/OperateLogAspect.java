package com.dingkai.personManage.business.aop;

import com.alibaba.fastjson.JSON;
import com.dingkai.personManage.business.bo.OperateLogBO;
import com.dingkai.personManage.business.config.RequestHolder;
import com.dingkai.personManage.business.filter.RequestWrapper;
import com.dingkai.personManage.business.utils.IpUtil;
import com.dingkai.personManage.common.response.BaseResult;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;

/**
 * @Author dingkai
 * @Date 2020/7/12 23:12
 */
@Component
@Aspect
@Order(1)
public class OperateLogAspect {

    private static final Logger logger = LoggerFactory.getLogger(OperateLogAspect.class);

    @Pointcut("@annotation(com.dingkai.personManage.business.annotation.OperateLog)")
    public void pointCut() {
    }

    @Around("pointCut()")
    public Object around(ProceedingJoinPoint joinPoint) {
        OperateLogBO operateLogBO = new OperateLogBO();
        String methodName = joinPoint.getSignature().getName();
        String className = joinPoint.getSignature().getDeclaringTypeName();
        operateLogBO.setOperateMethod(className + "." + methodName);
        operateLogBO.setOperateTime(LocalDateTime.now());

        //获取用户、ip信息、请求信息
        RequestWrapper request = RequestHolder.getRequest();
        operateLogBO.setOperateUsername(getUsername(request));
        operateLogBO.setOperateIp(IpUtil.getIpAddress(request));
        String requestUrl = request.getRequestURL().toString();
        operateLogBO.setRequestUrl(requestUrl);
        operateLogBO.setRequestParam(request.getQueryString());
        if (!requestUrl.contains("import") && !requestUrl.contains("upload")) {
            operateLogBO.setRequestBody(request.getBody());
        }
        try {
            //执行目标方法
            Object proceed = joinPoint.proceed();
            operateLogBO.setOperateResult(getOperateResult(proceed));
            return proceed;
        } catch (Throwable throwable) {
            throwable.printStackTrace();
            operateLogBO.setOperateResult(false);
            return BaseResult.error("-1", throwable.getMessage());
        } finally {
            logger.info("操作日志：{}", JSON.toJSONString(operateLogBO));
        }
        //return null;
    }

    public String getUsername(HttpServletRequest request) {
        try {
            return request.getUserPrincipal().getName();
        } catch (Exception e) {
            logger.error("获取登录用户出错，错误信息：{}", e.getMessage());
        }
        return "";
    }

    public boolean getOperateResult(Object object) {
        if (object instanceof BaseResult) {
            BaseResult baseResult = (BaseResult) object;
            return "0".equals(baseResult.getCode());
        }
        return false;
    }


}
