package com.dingkai.personManage.business.common.heartbeat.config;

import com.dingkai.personManage.business.common.heartbeat.callback.HeartbeatCallback;
import com.dingkai.personManage.business.common.heartbeat.dto.HeartbeatParam;

/**
 * @Author dingkai
 * @Date 2021/5/30 23:05
 */
public class HeartbeatConfig {

    /**
     * 心跳请求参数
     */
    private HeartbeatParam heartbeatParam;

    /**
     * 服务器地址
     */
    private String serverAddress;
    /**
     * 心跳间隔
     */
    private long heartbeatInterval;

    /**
     * 回调函数
     */
    private HeartbeatCallback heartbeatCallback;

    public HeartbeatConfig(HeartbeatParam heartbeatParam, String serverAddress, long heartbeatInterval, HeartbeatCallback heartbeatCallback) {
        this.heartbeatParam = heartbeatParam;
        this.serverAddress = serverAddress;
        this.heartbeatInterval = heartbeatInterval;
        this.heartbeatCallback = heartbeatCallback;
    }

    public HeartbeatParam getHeartbeatParam() {
        return heartbeatParam;
    }

    public void setHeartbeatParam(HeartbeatParam heartbeatParam) {
        this.heartbeatParam = heartbeatParam;
    }

    public String getServerAddress() {
        return serverAddress;
    }

    public void setServerAddress(String serverAddress) {
        this.serverAddress = serverAddress;
    }

    public long getHeartbeatInterval() {
        return heartbeatInterval;
    }

    public void setHeartbeatInterval(long heartbeatInterval) {
        this.heartbeatInterval = heartbeatInterval;
    }

    public HeartbeatCallback getHeartbeatCallback() {
        return heartbeatCallback;
    }

    public void setHeartbeatCallback(HeartbeatCallback heartbeatCallback) {
        this.heartbeatCallback = heartbeatCallback;
    }
}
