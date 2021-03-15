package com.dingkai.personManage.business.code.wechat.service.impl;

import com.dingkai.personManage.business.code.wechat.dto.TextRespMsg;
import com.dingkai.personManage.business.code.wechat.service.WechatMsgService;
import com.dingkai.personManage.business.code.wechat.utils.MessageUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * @author dingkai1
 * @desc
 * @date 2021/3/12 14:41
 */
@Service
public class ImageMsgService extends WechatMsgService {

    @Override
    public String handleMsg(Map<String, String> reqMap) {
        String respMsg = "";
        String picUrl = reqMap.get("PicUrl");
        if (StringUtils.isBlank(picUrl)) {
            return respMsg;
        }
        respMsg = "图片链接为：" + picUrl;
        TextRespMsg textRespMsg = new TextRespMsg();
        textRespMsg.setBaseResp(reqMap, MessageUtil.RESP_MESSAGE_TYPE_TEXT);
        textRespMsg.setContent(respMsg);
        MessageUtil.textMessageToXml(textRespMsg);
        return MessageUtil.textMessageToXml(textRespMsg);
    }
}
