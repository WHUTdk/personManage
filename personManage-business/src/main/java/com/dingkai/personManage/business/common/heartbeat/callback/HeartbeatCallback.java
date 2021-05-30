package com.dingkai.personManage.business.common.heartbeat.callback;

import com.dingkai.personManage.business.common.heartbeat.dto.HeartbeatParam;

/**
 * @Author dingkai
 * @Date 2021/5/30 23:06
 */
public interface HeartbeatCallback {

    /**
     * 正常心跳
     */
    void success(HeartbeatParam heartBeatParam);

    /**
     * 心跳非法
     */
    void failure(HeartbeatParam heartBeatParam);

    /**
     * 心跳失败
     */
    void exception(HeartbeatParam heartBeatParam);

}
