package com.dingkai.personManage.business.task;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

/**
 * @Author dingkai
 * @Date 2020/9/9 0:39
 */
@Component
public class BeanPostProcessorTest implements BeanPostProcessor {

    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        if (bean instanceof BeanLifeCycleTest) {
            System.out.println("BeanPostProcessor前置处理" + beanName);
        }
        return bean;
    }


    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        if (bean instanceof BeanLifeCycleTest) {
            System.out.println("BeanPostProcessor后置处理" + beanName);
        }
        return bean;
    }
}
