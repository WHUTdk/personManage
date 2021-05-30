package com.dingkai.personManage.business.common.heartbeat.task;

import com.dingkai.personManage.business.common.heartbeat.callback.HeartbeatCallback;
import com.dingkai.personManage.business.common.heartbeat.client.HeartbeatClient;
import com.dingkai.personManage.business.common.heartbeat.config.HeartbeatConfig;
import com.dingkai.personManage.business.common.heartbeat.dto.HeartbeatParam;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

/**
 * @Author dingkai
 * @Date 2021/5/30 23:09
 */
@Component
public class HeartbeatTask {

    private static final Logger logger= LoggerFactory.getLogger(HeartbeatTask.class);

    @Value("${heartbeat.server.address}")
    private String heartbeatServerAddress;

    @Value("${heartbeat.interval}")
    private String heartbeatInterval;

    public void initHeartbeat() {
        HeartbeatParam heartbeatParam = new HeartbeatParam();
        HeartbeatConfig config = new HeartbeatConfig(heartbeatParam, heartbeatServerAddress, StringUtils.isBlank(heartbeatInterval) ? 0L : Long.parseLong(heartbeatInterval),
                new HeartbeatCallback() {
                    @Override
                    public void success(HeartbeatParam heartBeatParam) {
                        logger.info("心跳回调成功");
                    }

                    @Override
                    public void failure(HeartbeatParam heartBeatParam) {
                        logger.info("心跳回调失败");
                    }

                    @Override
                    public void exception(HeartbeatParam heartBeatParam) {
                        logger.info("心跳回调异常");
                    }
                });
        HeartbeatClient.setHeartbeatConfig(config);
        HeartbeatClient.start();
    }


}
