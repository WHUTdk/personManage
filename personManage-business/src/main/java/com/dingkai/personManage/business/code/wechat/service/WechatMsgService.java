package com.dingkai.personManage.business.code.wechat.service;

import com.dingkai.personManage.business.code.wechat.dto.WechatRespMsgTemplate;
import com.dingkai.personManage.business.code.wechat.utils.MessageUtil;

import java.util.Map;

/**
 * @author dingkai1
 * @desc
 * @date 2021/3/12 14:35
 */
public abstract class WechatMsgService {

    public abstract String handleMsg(Map<String, String> reqMap);

}
