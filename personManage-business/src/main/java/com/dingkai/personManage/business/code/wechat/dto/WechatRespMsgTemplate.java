package com.dingkai.personManage.business.code.wechat.dto;

import com.dingkai.personManage.business.code.wechat.utils.MessageUtil;

import java.io.Serializable;
import java.util.Map;

/**
 * @author dingkai1
 * @desc
 * @date 2021/3/12 16:18
 */
public class WechatRespMsgTemplate implements Serializable {

    private static final long serialVersionUID = 7221407299725791512L;

    private String ToUserName;
    private String FromUserName;
    private long CreateTime;
    private String MsgType;

    public String getToUserName() {
        return ToUserName;
    }

    public void setToUserName(String toUserName) {
        ToUserName = toUserName;
    }

    public String getFromUserName() {
        return FromUserName;
    }

    public void setFromUserName(String fromUserName) {
        FromUserName = fromUserName;
    }

    public long getCreateTime() {
        return CreateTime;
    }

    public void setCreateTime(long createTime) {
        CreateTime = createTime;
    }

    public String getMsgType() {
        return MsgType;
    }

    public void setMsgType(String msgType) {
        MsgType = msgType;
    }

    public void setBaseResp(Map<String, String> reqMap,String respMsgType){
        this.setToUserName(reqMap.get(MessageUtil.FROM_USERNAME));
        this.setFromUserName(reqMap.get(MessageUtil.TO_USERNAME));
        this.setCreateTime(System.currentTimeMillis());
        this.setMsgType(respMsgType);
    }
}
