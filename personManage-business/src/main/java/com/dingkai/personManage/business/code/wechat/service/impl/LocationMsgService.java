package com.dingkai.personManage.business.code.wechat.service.impl;

import com.dingkai.personManage.business.code.wechat.dto.TextRespMsg;
import com.dingkai.personManage.business.code.wechat.service.WechatMsgService;
import com.dingkai.personManage.business.code.wechat.utils.MessageUtil;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * @author dingkai1
 * @desc
 * @date 2021/3/12 14:41
 */
@Service
public class LocationMsgService extends WechatMsgService {

    @Override
    public String handleMsg(Map<String, String> reqMap) {
        String location_x = reqMap.get("Location_X");
        String location_y = reqMap.get("Location_Y");
        String scale = reqMap.get("Scale");
        String label = reqMap.get("Label");
        String respMsg = "位置具体信息：\n纬度：" + location_x + "\n经度：" + location_y + "\n地图缩放大小：" + scale + "\n地理位置信息：" + label;
        TextRespMsg textRespMsg = new TextRespMsg();
        textRespMsg.setBaseResp(reqMap, MessageUtil.RESP_MESSAGE_TYPE_TEXT);
        textRespMsg.setContent(respMsg);
        MessageUtil.textMessageToXml(textRespMsg);
        return MessageUtil.textMessageToXml(textRespMsg);
    }
}
