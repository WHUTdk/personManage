package com.dingkai.personManage.business.code.wechat.service.impl;

import com.dingkai.personManage.business.code.wechat.service.WechatMsgService;
import org.apache.commons.lang3.StringUtils;

import java.util.Map;

/**
 * @author dingkai1
 * @desc
 * @date 2021/3/12 14:39
 */
public class TextMsgService extends WechatMsgService {

    /**
     * 处理用户发送的文本消息
     */
    @Override
    public String handleMsg(Map<String, String> reqMap) {
        String respMsg = "";
        String content = reqMap.get("Content");
        if (StringUtils.isBlank(content)) {
            return respMsg;
        }
        if (content.contains("你好")) {
            respMsg = "我不好";
        }
        return respMsg;
    }
}
