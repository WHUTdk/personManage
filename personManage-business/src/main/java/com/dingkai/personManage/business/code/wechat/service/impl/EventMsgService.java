package com.dingkai.personManage.business.code.wechat.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.dingkai.personManage.business.code.wechat.constants.WechatUrlConstant;
import com.dingkai.personManage.business.code.wechat.dto.ImageRespMsg;
import com.dingkai.personManage.business.code.wechat.dto.TextRespMsg;
import com.dingkai.personManage.business.code.wechat.enums.WechatEventKeyEnum;
import com.dingkai.personManage.business.code.wechat.service.WechatMsgService;
import com.dingkai.personManage.business.code.wechat.service.WechatService;
import com.dingkai.personManage.business.code.wechat.utils.MessageUtil;
import com.dingkai.personManage.business.code.wechat.utils.WechatUploadUtil;
import com.dingkai.personManage.common.utils.HttpUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Map;

/**
 * @author dingkai1
 * @desc
 * @date 2021/3/12 14:42
 */
@Service
public class EventMsgService extends WechatMsgService {

    private static final Logger logger = LoggerFactory.getLogger(EventMsgService.class);

    @Autowired
    private HttpUtil httpUtil;
    @Autowired
    private WechatService wechatService;

    @Override
    public String handleMsg(Map<String, String> reqMap) {
        String respMsg;
        String event = reqMap.get(MessageUtil.EVENT_NAME);
        switch (event) {
            case MessageUtil.EVENT_TYPE_SUBSCRIBE:
                respMsg = subscribeEvent(reqMap);
                break;
            case MessageUtil.EVENT_TYPE_UNSUBSCRIBE:
                respMsg = unSubscribeEvent(reqMap);
                break;
            case MessageUtil.EVENT_TYPE_CLICK:
                respMsg = clinicEvent(reqMap);
                break;
            case MessageUtil.EVENT_TYPE_VIEW:
                respMsg = viewEvent(reqMap);
                break;
            case MessageUtil.EVENT_TYPE_SCAN:
                respMsg = scanEvent(reqMap);
                break;
            case MessageUtil.EVENT_TYPE_LOCATION:
                respMsg = locationEvent(reqMap);
                break;
            default:
                logger.info("未知的推送事件类型，不处理，event：{}", event);
                return "";
        }
        return respMsg;
    }

    /**
     * 订阅事件
     */
    private String subscribeEvent(Map<String, String> reqMap) {
        //事件KEY值，qrscene_为前缀，后面为二维码的参数值
        String eventKey = reqMap.get(MessageUtil.EVENT_KEY);
        if (eventKey != null && eventKey.contains("qrscene_")) {
            //解析数据，处理业务逻辑
        }
        //返回数据
        TextRespMsg textRespMsg = new TextRespMsg();
        textRespMsg.setBaseResp(reqMap, MessageUtil.RESP_MESSAGE_TYPE_TEXT);
        String content = "一炉香烟往上升！<a href=\"https://github.com/WHUTdk/personManage/\">源代码地址</a>";
        textRespMsg.setContent(content);
        return MessageUtil.textMessageToXml(textRespMsg);
    }

    /**
     * 取消订阅事件
     */
    private String unSubscribeEvent(Map<String, String> reqMap) {
        return null;
    }

    /**
     * 用户已关注 扫码事件
     */
    private String scanEvent(Map<String, String> reqMap) {
        return null;
    }

    /**
     * 点击按钮事件
     */
    private String clinicEvent(Map<String, String> reqMap) {
        //对应菜单按钮设置的key
        try {
            String eventKey = reqMap.get(MessageUtil.EVENT_KEY);
            if (WechatEventKeyEnum.CLICK_TEST.getKey().equals(eventKey)) {
                TextRespMsg textRespMsg = new TextRespMsg();
                textRespMsg.setBaseResp(reqMap, MessageUtil.RESP_MESSAGE_TYPE_TEXT);
                textRespMsg.setContent("敲击");
                return MessageUtil.textMessageToXml(textRespMsg);
            } else if (WechatEventKeyEnum.CLICK_TEST1.getKey().equals(eventKey)) {

            } else if (WechatEventKeyEnum.CLICK_TEST2.getKey().equals(eventKey)) {
                ImageRespMsg imageRespMsg = new ImageRespMsg();
                imageRespMsg.setBaseResp(reqMap, MessageUtil.RESP_MESSAGE_TYPE_IMAGE);
                ImageRespMsg.Image image = new ImageRespMsg.Image();
                String url = WechatUrlConstant.uploadUrl.replace("#{accessToken}", wechatService.getAccessToken()).replace("#{type}", "image");
                String result = httpUtil.uploadFile(url, WechatUploadUtil.getParams("/opt/images/鲮鲤2020Q4团建.jpg", "image"));
                logger.info("调用微信上传素材接口，返回结果：{}", result);
                String mediaId = "";
                if (result != null) {
                    JSONObject jsonObject = JSONObject.parseObject(result);
                    mediaId = jsonObject.getString("media_id");
                }
                image.setMediaId(mediaId);
                imageRespMsg.setImage(image);
                return MessageUtil.imageMessageToXml(imageRespMsg);
            } else {
                logger.info("未知的eventKey：{}", eventKey);
            }
        } catch (IOException e) {
            logger.error("响应微信推送事件消息出错", e);
        }
        return null;
    }

    /**
     * 跳转url事件
     */
    private String viewEvent(Map<String, String> reqMap) {
        return null;
    }

    /**
     * 发送地理位置事件
     */
    private String locationEvent(Map<String, String> reqMap) {
        String latitude = reqMap.get("Latitude");
        String longitude = reqMap.get("Longitude");
        String precision = reqMap.get("Precision");
        TextRespMsg textRespMsg = new TextRespMsg();
        textRespMsg.setBaseResp(reqMap, MessageUtil.RESP_MESSAGE_TYPE_TEXT);
        String content = "发送定位的具体信息：\n纬度：" + latitude + "\n" + "经度：" + longitude;
        textRespMsg.setContent(content);
        return MessageUtil.textMessageToXml(textRespMsg);
    }
}
