package com.dingkai.personManage.business.code.task;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;

/**
 * @Author dingkai
 * @Date 2020/8/5 21:31
 */
@Component
public class TaskService {

    private static final Logger logger = LoggerFactory.getLogger(TaskService.class);

    @Autowired
    @Qualifier("myAsyncTaskExecutor")
    private ThreadPoolTaskExecutor threadPoolTaskExecutor;

    @Async("myAsyncTaskExecutor")
    public void test() {
        int activeCount = threadPoolTaskExecutor.getActiveCount();
        String name = Thread.currentThread().getName();
        logger.info("当前线程名称：{},当前异步线程池活跃线程数：{}", name, activeCount);
    }

}
