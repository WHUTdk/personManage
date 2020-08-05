package com.dingkai.personManage.business.init;

import com.dingkai.personManage.business.task.TaskService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
public class InitTask {

    private static final Logger logger = LoggerFactory.getLogger(InitTask.class);

    @Autowired
    private TaskService taskService;

    @PostConstruct
    public void initMethod() {
        logger.info("初始化方法执行中...");
        taskService.test();
    }


}
