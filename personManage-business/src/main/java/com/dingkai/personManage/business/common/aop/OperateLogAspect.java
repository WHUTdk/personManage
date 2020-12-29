package com.dingkai.personManage.business.common.aop;

import com.alibaba.fastjson.JSON;
import com.dingkai.personManage.business.code.bo.OperateLogBo;
import com.dingkai.personManage.business.common.config.RequestHolder;
import com.dingkai.personManage.business.common.filter.RequestWrapper;
import com.dingkai.personManage.common.utils.IpUtil;
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

    @Pointcut("@annotation(com.dingkai.personManage.business.common.annotation.OperateLog)")
    public void pointCut() {
    }

    @Around("pointCut()")
    public Object around(ProceedingJoinPoint joinPoint) {
        OperateLogBo operateLogBo = new OperateLogBo();
        String methodName = joinPoint.getSignature().getName();
        String className = joinPoint.getSignature().getDeclaringTypeName();
        operateLogBo.setOperateMethod(className + "." + methodName);
        operateLogBo.setOperateTime(LocalDateTime.now());

        //获取用户、ip信息、请求信息
        RequestWrapper request = RequestHolder.getRequest();
        operateLogBo.setOperateUsername(getUsername(request));
        operateLogBo.setOperateIp(IpUtil.getIpAddress(request));
        String requestUrl = request.getRequestURL().toString();
        operateLogBo.setRequestUrl(requestUrl);
        operateLogBo.setRequestParam(request.getQueryString());
        if (!requestUrl.contains("import") && !requestUrl.contains("upload")) {
            operateLogBo.setRequestBody(request.getBody());
        }
        try {
            //执行目标方法
            Object proceed = joinPoint.proceed();
            operateLogBo.setOperateResult(getOperateResult(proceed));
            return proceed;
        } catch (Throwable throwable) {
            throwable.printStackTrace();
            operateLogBo.setOperateResult(false);
            return BaseResult.error("-1", throwable.getMessage());
        } finally {
            logger.info("操作日志：{}", JSON.toJSONString(operateLogBo));
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
