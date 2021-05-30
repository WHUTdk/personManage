package com.dingkai.personManage.business.common.heartbeat.dto;

import java.io.Serializable;

/**
 * @Author dingkai
 * @Date 2021/5/30 23:00
 * 心跳请求参数
 */
public class HeartbeatParam implements Serializable {
    private static final long serialVersionUID = -469971730507355883L;

    private long timestamp;

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
}
