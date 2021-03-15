package com.dingkai.personManage.business.code.wechat.service.impl;

import com.dingkai.personManage.business.code.wechat.service.WechatMsgService;
import org.apache.commons.lang3.StringUtils;

import java.util.Map;

/**
 * @author dingkai1
 * @desc
 * @date 2021/3/12 14:41
 */
public class ImageMsgService extends WechatMsgService {

    @Override
    public String handleMsg(Map<String, String> reqMap) {
        String respMsg = "";
        String picUrl = reqMap.get("PicUrl");
        if (StringUtils.isBlank(picUrl)) {
            return respMsg;
        }
        respMsg = "图片链接为：" + picUrl;
        return respMsg;
    }
}
