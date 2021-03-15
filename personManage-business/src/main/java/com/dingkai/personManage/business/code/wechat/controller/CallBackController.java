package com.dingkai.personManage.business.code.wechat.controller;

import com.dingkai.personManage.business.code.wechat.config.WechatConfig;
import com.dingkai.personManage.business.code.wechat.enums.WechatMsgTypeEnum;
import com.dingkai.personManage.business.code.wechat.service.WechatMsgService;
import com.dingkai.personManage.business.code.wechat.utils.MessageUtil;
import com.dingkai.personManage.business.common.utils.SpringUtil;
import com.dingkai.personManage.common.utils.ArrayUtil;
import com.dingkai.personManage.common.utils.JsonUtil;
import io.swagger.annotations.Api;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.Map;

/**
 * @author dingkai1
 * @desc
 * @date 2021/3/10 13:25
 */
@Api(tags = "微信回调")
@RestController
@RequestMapping("/wechat")
public class CallBackController {

    private static final Logger logger = LoggerFactory.getLogger(CallBackController.class);

    @Autowired
    private WechatConfig wechatConfig;

    @RequestMapping("/callBack")
    public String callBack(HttpServletRequest request) {
        String msg = "";
        String method = request.getMethod();
        if ("GET".equalsIgnoreCase(method)) {
            msg = doGet(request);
        } else if ("POST".equalsIgnoreCase(method)) {
            msg = doPost(request);
        }
        return msg;
    }

    private String doGet(HttpServletRequest request) {
        String echostr = request.getParameter("echostr");
        String signature = request.getParameter("signature");
        String timestamp = request.getParameter("timestamp");
        String nonce = request.getParameter("nonce");
        String token = wechatConfig.getToken();
        logger.info("微信回调认证，echostr：{}，signature:{}，token:{}", echostr, signature, token);
        /*
         1）将token、timestamp、nonce三个参数进行字典序排序
         2）将三个参数字符串拼接成一个字符串进行sha1加密
         3）开发者获得加密后的字符串可与signature对比，标识该请求来源于微信**/
        String[] strings = {token, timestamp, nonce};
        Arrays.sort(strings);
        //apache的加密工具类
        String sha1Hex = DigestUtils.sha1Hex(ArrayUtil.arrayToStr(strings, ""));
        if (StringUtils.equals(sha1Hex, signature)) {
            return echostr;
        } else {
            return "error";
        }
    }

    private String doPost(HttpServletRequest request) {
        try {
            Map<String, String> reqMap = MessageUtil.parseXml(request);
            logger.info("微信post回调数据：{}", JsonUtil.toJsonString(reqMap));
            String msgType = reqMap.get(MessageUtil.MSG_TYPE);
            //根据不通消息类型，分别处理消息
            WechatMsgTypeEnum msgTypeEnum = WechatMsgTypeEnum.getByType(msgType);
            if (msgTypeEnum == null) {
                logger.info("未知的消息类型，不处理，msgType:{}", msgType);
                return "";
            }
            String respMsg = SpringUtil.getBean(msgTypeEnum.getService()).handleMsg(reqMap);
            logger.info("微信post回调响应明文数据：{}", respMsg);
            return respMsg;
        } catch (Exception e) {
            logger.error("解析微信post回调数据异常", e);
        }
        return "";
    }

}
