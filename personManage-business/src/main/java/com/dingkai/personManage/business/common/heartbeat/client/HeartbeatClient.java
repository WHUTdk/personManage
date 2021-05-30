package com.dingkai.personManage.business.common.heartbeat.client;

import com.alibaba.fastjson.JSON;
import com.dingkai.personManage.business.common.heartbeat.callback.HeartbeatCallback;
import com.dingkai.personManage.business.common.heartbeat.config.HeartbeatConfig;
import com.dingkai.personManage.business.common.heartbeat.dto.HeartbeatParam;
import com.dingkai.personManage.common.enums.CodeEnum;
import com.dingkai.personManage.common.response.BaseResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @Author dingkai
 * @Date 2021/5/30 23:12
 */
public class HeartbeatClient {

    private static final Logger logger = LoggerFactory.getLogger(HeartbeatClient.class);

    /**
     * 心跳是否正常，true正常，false异常
     */
    private static final AtomicBoolean HEARTBEAT_FLAG = new AtomicBoolean();
    /**
     * 是否运行
     */
    private static final AtomicBoolean IS_RUNNING = new AtomicBoolean();

    private static HeartbeatConfig heartbeatConfig;

    private static final long heartbeatInterval = 10L * 60 * 1000;

    private static ScheduledFuture<?> scheduledFuture;

    /**
     * 心跳配置
     */
    public static void setHeartbeatConfig(HeartbeatConfig config) {
        if (config == null || config.getServerAddress() == null
                || "".equals(config.getServerAddress())) {
            throw new IllegalArgumentException("心跳服务地址不能为空");
        }
        if (config.getHeartbeatParam() == null) {
            throw new IllegalArgumentException("心跳请求参数不能为空");
        }
        heartbeatConfig = config;
        if (config.getHeartbeatInterval() > 1) {
            heartbeatConfig.setHeartbeatInterval(config.getHeartbeatInterval());
        } else {
            heartbeatConfig.setHeartbeatInterval(heartbeatInterval);
        }
        logger.info("心跳检测配置信息：{}", JSON.toJSONString(config));
    }

    /**
     * 启动心跳入口
     */
    public static void start() {
        if (heartbeatConfig == null) {
            throw new IllegalArgumentException("请先设置心跳配置参数");
        }
        logger.info("启动心跳线程");
        if (IS_RUNNING.get()) {
            logger.info("心跳检测线程已启动,无需再次启动");
            return;
        }
        if (heartbeatConfig.getHeartbeatCallback() == null) {
            throw new IllegalArgumentException("请先设置心跳回调方法");
        }
        IS_RUNNING.getAndSet(true);
        ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(3);
        scheduledFuture = scheduledExecutorService.scheduleWithFixedDelay(new HeartbeatHandler(), 0, HeartbeatClient.heartbeatInterval, TimeUnit.MILLISECONDS);
        logger.info("启动心跳线程成功");
    }

    private static void stop() {
        logger.info("停止心跳检测线程");
        IS_RUNNING.getAndSet(false);
        while (true) {
            if (scheduledFuture != null) {
                scheduledFuture.cancel(true);
            }
            if (!IS_RUNNING.get()) {
                logger.info("停止心跳检测线程成功");
                return;
            }
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                logger.error("线程异常");
            }
        }
    }

    /**
     * 实际执行线程
     */
    private static class HeartbeatHandler implements Runnable {

        @Override
        public void run() {
            try {
                if (HeartbeatClient.IS_RUNNING.get()) {
                    sendHeartbeat();
                } else {
                    HeartbeatClient.IS_RUNNING.getAndSet(false);
                    logger.info("心跳线程关闭");
                }
            } catch (Exception e) {
                logger.info("心跳线程异常", e);
            }
        }

    }


    private static void sendHeartbeat() {
        BaseResult<Boolean> result = null;
        //todo 调用远程心跳接口
        logger.info("开始调用心跳检测接口");
        heartbeatConfig.getHeartbeatParam().setTimestamp(System.currentTimeMillis());
        if (result != null && CodeEnum.SUCCESS.getCode().equals(result.getCode())) {
            if (result.getData()) {
                HeartbeatClient.HEARTBEAT_FLAG.getAndSet(true);
                heartbeatConfig.getHeartbeatCallback().success(heartbeatConfig.getHeartbeatParam());
            } else {
                // 非法心跳
                heartbeatConfig.getHeartbeatCallback().failure(heartbeatConfig.getHeartbeatParam());
            }
        } else {
            HeartbeatClient.HEARTBEAT_FLAG.getAndSet(false);
            logger.info("心跳检测失败");
            heartbeatConfig.getHeartbeatCallback().exception(heartbeatConfig.getHeartbeatParam());
        }
    }


}
