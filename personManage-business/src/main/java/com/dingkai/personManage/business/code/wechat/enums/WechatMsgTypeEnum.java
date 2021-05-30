package com.dingkai.personManage.business.code.wechat.enums;

import com.dingkai.personManage.business.code.wechat.service.WechatMsgService;
import com.dingkai.personManage.business.code.wechat.service.impl.*;
import com.dingkai.personManage.business.code.wechat.utils.MessageUtil;

/**
 * @author dingkai1
 * @desc
 * @date 2021/3/12 14:32
 */
public enum WechatMsgTypeEnum {

    TEXT(MessageUtil.REQ_MESSAGE_TYPE_TEXT, TextMsgService.class),
    IMAGE(MessageUtil.REQ_MESSAGE_TYPE_IMAGE, ImageMsgService.class),
    VOICE(MessageUtil.REQ_MESSAGE_TYPE_VOICE, VoiceMsgService.class),
    VIDEO(MessageUtil.REQ_MESSAGE_TYPE_VIDEO, VideoMsgService.class),
    LOCATION(MessageUtil.REQ_MESSAGE_TYPE_LOCATION, LocationMsgService.class),
    EVENT(MessageUtil.REQ_MESSAGE_TYPE_EVENT, EventMsgService.class);

    private String msgType;

    private Class<? extends WechatMsgService> service;

    WechatMsgTypeEnum(String msgType, Class<? extends WechatMsgService> service) {
        this.msgType = msgType;
        this.service = service;
    }

    public String getMsgType() {
        return msgType;
    }

    public void setMsgType(String msgType) {
        this.msgType = msgType;
    }

    public Class<? extends WechatMsgService> getService() {
        return service;
    }

    public void setService(Class<? extends WechatMsgService> service) {
        this.service = service;
    }

    public static WechatMsgTypeEnum getByType(String type) {
        WechatMsgTypeEnum wxMsgType = null;
        WechatMsgTypeEnum[] selfs = values();
        for (WechatMsgTypeEnum item : selfs) {
            if (item.getMsgType().equals(type)) {
                wxMsgType = item;
                break;
            }
        }
        return wxMsgType;
    }

}
