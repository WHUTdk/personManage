package com.dingkai.personManage.business.task;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanNameAware;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

/**
 * @Author dingkai
 * @Date 2020/9/8 23:58
 */
public class BeanLifeCycleTest implements BeanNameAware, InitializingBean, DisposableBean {

    private String beanName;

    public BeanLifeCycleTest() {
        System.out.println("执行Bean构造方法");
    }

    /**
     * 设置BeanName
     */
    @Override
    public void setBeanName(String beanName) {
        this.beanName = beanName;
        System.out.println("beanNameAware:" + beanName);
    }

    @PostConstruct
    public void postConstruct() {
        System.out.println("执行postConstruct()方法");
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        System.out.println("执行InitializingBean接口的afterPropertiesSet()方法");
    }

    public void initMethod() {
        System.out.println("执行Bean自定义init方法");
    }

    @PreDestroy
    public void preDestroy() {
        System.out.println("执行preDestroy()方法");
    }

    @Override
    public void destroy() throws Exception {
        System.out.println("执行DisposableBean接口的destroy()方法");
    }

    public void destroyMethod() {
        System.out.println("执行Bean自定义destroy方法");
    }

    public String getBeanName() {
        return beanName;
    }
}
