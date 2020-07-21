package com.dingkai.personManage.business.init;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
public class InitTask {

    private static final Logger logger = LoggerFactory.getLogger(InitTask.class);

    @PostConstruct
    public void initMethod(){
        logger.info("初始化方法执行中...");
    }


}
