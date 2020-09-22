package com.dingkai.personManage.business.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.io.IOException;
import java.util.concurrent.*;

/**
 * @Author dingkai
 * @Date 2020/8/5 21:23
 */
@Configuration
public class AsyncConfig {

    @Bean(name = "myAsyncTaskExecutor")
    public ThreadPoolTaskExecutor getPoolTaskExecutor() {

        ThreadPoolTaskExecutor taskExecutor = new ThreadPoolTaskExecutor();
        //核心线程数
        taskExecutor.setCorePoolSize(10);
        //线程池维护线程的最大数量,只有在缓冲队列满了之后才会申请超过核心线程数的线程
        taskExecutor.setMaxPoolSize(15);
        //缓存队列长度 默认LinkedBlockingQueue
        taskExecutor.setQueueCapacity(1000);
        //允许的空闲时间,当超过了核心线程数之外的线程在空闲时间到达之后会被销毁
        taskExecutor.setKeepAliveSeconds(200);
        //异步方法线程名称
        taskExecutor.setThreadNamePrefix("myAsyncTask-");

        /**
         * 当线程池的任务缓存队列已满并且线程池中的线程数目达到maximumPoolSize，如果还有任务到来就会采取任务拒绝策略
         * 通常有以下四种策略：
         * ThreadPoolExecutor.AbortPolicy:丢弃任务并抛出RejectedExecutionException异常。
         * ThreadPoolExecutor.DiscardPolicy：也是丢弃任务，但是不抛出异常。
         * ThreadPoolExecutor.DiscardOldestPolicy：丢弃队列最前面的任务，然后重新尝试执行任务（重复此过程）
         * ThreadPoolExecutor.CallerRunsPolicy：重试添加当前的任务，自动重复调用 execute() 方法，直到成功
         */
        taskExecutor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        taskExecutor.initialize();
        return taskExecutor;
    }

    public static void main(String[] args) {
        try {
            Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        System.out.println("start interrupt thread...");
                        Thread.currentThread().interrupt();
                        //执行打断方法后，线程并不会立即停止，只是被设置了中断标识，
                        //执行打断方法后，再执行阻塞方法，会抛出中断异常，并真正停止线程
                        //Thread.sleep(1000);
                        //此处检查当前线程是否中断，返回true，检查后，不会清理中断标识
                        System.out.println("thread isInterrupted：" + Thread.currentThread().isInterrupted());
                        System.out.println("thread isInterrupted：" + Thread.currentThread().isInterrupted());
                        //此处检查当前线程是否中断，第一次返回true,但会清理中断标识，第二次返回false
                        System.out.println("thread interrupted：" + Thread.interrupted());
                        System.out.println("thread interrupted：" + Thread.interrupted());
                        System.out.println("end thread...");
                    } catch (Exception e) {
                        System.out.println("thread error...");
                        e.printStackTrace();
                    }
                }
            });
            thread.start();
            Thread.sleep(1000);
            thread.interrupt();
            //此处其实检测的是当前Main线程是否中断，返回都是false
            System.out.println(thread.isInterrupted());
            System.out.println(thread.isInterrupted());
            System.out.println(Thread.interrupted());
            System.out.println(Thread.interrupted());
            System.out.println("main thread end...");
        } catch (Exception e) {
            System.out.println("main thread error...");
            e.printStackTrace();
        }
    }

}
