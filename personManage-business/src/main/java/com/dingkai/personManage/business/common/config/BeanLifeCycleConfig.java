package com.dingkai.personManage.business.common.config;

import com.dingkai.personManage.business.code.task.BeanLifeCycleTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Author dingkai
 * @Date 2020/9/9 0:14
 */
@Configuration
public class BeanLifeCycleConfig {

    /**
     * 自定义Bean的初始化方法和销毁方法
     */
    @Bean(name = "myBeanLifeCycleTest", initMethod = "initMethod", destroyMethod = "destroyMethod")
    public BeanLifeCycleTest beanLifeCycleTest() {
        return new BeanLifeCycleTest();
    }

}
