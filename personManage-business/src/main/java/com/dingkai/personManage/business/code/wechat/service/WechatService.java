package com.dingkai.personManage.business.code.wechat.service;

import com.alibaba.fastjson.JSONObject;
import com.dingkai.personManage.business.code.wechat.config.WechatConfig;
import com.dingkai.personManage.business.code.wechat.constants.WechatRedisConstant;
import com.dingkai.personManage.business.code.wechat.constants.WechatUrlConstant;
import com.dingkai.personManage.business.code.wechat.enums.WechatEventKeyEnum;
import com.dingkai.personManage.business.code.wechat.utils.WechatUtil;
import com.dingkai.personManage.common.response.BaseResult;
import com.dingkai.personManage.common.utils.HttpUtil;
import com.dingkai.personManage.common.utils.JsonUtil;
import com.dingkai.personManage.common.utils.RedisUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author dingkai1
 * @desc
 * @date 2021/3/11 18:05
 */
@Service
public class WechatService {

    private static final Logger logger = LoggerFactory.getLogger(WechatService.class);

    @Value("${wechat.qrCode.expire.seconds}")
    private String expireSecondsStr;

    @Autowired
    private WechatConfig wechatConfig;
    @Autowired
    private HttpUtil httpUtil;
    @Autowired
    private RedisUtil redisUtil;

    /**
     * 获取微信的AccessToken
     */
    public String getAccessToken() {
        //先查询缓存
        String redisKey = WechatRedisConstant.accessTokenKey.replace("#{appId}", wechatConfig.getAppId());
        String accessToken = (String) redisUtil.get(redisKey);
        if (StringUtils.isNotBlank(accessToken)) {
            return accessToken;
        }
        //调用微信接口
        String url = WechatUrlConstant.accessTokenUrl.replace("#{appId}", wechatConfig.getAppId()).replace("#{secret}", wechatConfig.getAppSecret());
        logger.info("调用微信获取accessToken请求url：{}", url);
        JSONObject jsonObject = httpUtil.doGetForJson(url);
        if (jsonObject != null) {
            logger.info("调用微信获取accessToken接口返回结果：{}", jsonObject.toJSONString());
            accessToken = jsonObject.getString("access_token");
            //凭证有效时间，单位：秒
            Integer expire = jsonObject.getInteger("expires_in");
            if (expire != null) {
                //提前5min过期
                expire -= 5 * 60;
                if (expire >= 0) {
                    redisUtil.set(redisKey, accessToken, expire);
                }
            }
        }
        return accessToken;
    }

    /**
     * 创建微信公众号菜单
     */
    public BaseResult createMenu() {
        Map<String, Object> requestMap = new HashMap<String, Object>();
        List<Object> buttons = new ArrayList<Object>();
        buttons.add(WechatUtil.createClickBtn(WechatEventKeyEnum.CLICK_TEST.getName(), WechatEventKeyEnum.CLICK_TEST.getKey()));
        buttons.add(WechatUtil.createLocationBtn("location", "location_test"));
        //子按钮
        ArrayList<Object> subButtons = new ArrayList<>();
        subButtons.add(WechatUtil.createViewBtn("view", "http://www.baidu.com"));
        subButtons.add(WechatUtil.createClickBtn(WechatEventKeyEnum.CLICK_TEST2.getName(), WechatEventKeyEnum.CLICK_TEST2.getKey()));
        buttons.add(WechatUtil.createSubBtn("子菜单", subButtons));
        requestMap.put("button", buttons);
        //调用微信接口
        logger.info("调用微信创建菜单接口，请求参数：{}", JsonUtil.toJsonString(requestMap));
        String url = WechatUrlConstant.createMenuUrl.replace("#{accessToken}", getAccessToken());
        JSONObject jsonObject = httpUtil.doPostForJson(url, requestMap);
        logger.info("调用微信创建菜单接口，返回结果：{}", JSONObject.toJSONString(jsonObject));
        return BaseResult.success();
    }

    /**
     * 获取临时二维码
     */
    public BaseResult getTempQrCode() {
        //携带业务参数
        String userId = "15271876499";
        //先从缓存获取
        String redisKey = WechatRedisConstant.qrCodeShowUrlKey.replace("#{userId}", userId);
        String showUrl = (String) redisUtil.get(redisKey);
        if (StringUtils.isNotBlank(showUrl)) {
            return BaseResult.success(showUrl);
        }
        HashMap<String, Object> requestParam = new HashMap<>();
        if (StringUtils.isBlank(expireSecondsStr)) {
            expireSecondsStr = "30";
        }
        requestParam.put("expire_seconds", Integer.parseInt(expireSecondsStr));
        requestParam.put("action_name", "QR_STR_SCENE");
        HashMap<String, Object> scene = new HashMap<>();
        HashMap<String, String> sceneStr = new HashMap<>();
        sceneStr.put("scene_str", userId);
        scene.put("scene", sceneStr);
        requestParam.put("action_info", scene);
        String createUrl = WechatUrlConstant.tempQrCodeCreateUrl.replace("#{accessToken}", getAccessToken());
        JSONObject jsonObject = httpUtil.doPostForJson(createUrl, requestParam);
        logger.info("调用微信创建二维码接口，返回结果：{}", jsonObject.toJSONString());
        String ticket = jsonObject.getString("ticket");
        Integer expireSeconds = jsonObject.getInteger("expire_seconds");
        //自己创建二维码时，可以用到url参数
        String url = jsonObject.getString("url");
        if (StringUtils.isBlank(ticket)) {
            return BaseResult.error("-1", "创建二维码票据为空");
        }
        //根据ticket获取二维码图片
        showUrl = WechatUrlConstant.tempQrCodeShowUrl.replace("#{ticket}", ticket);
        //存储到redis
        if (expireSeconds >= 30) {
            redisUtil.set(WechatRedisConstant.qrCodeShowUrlKey.replace("#{userId}", userId), showUrl, expireSeconds - 3);
        }
        return BaseResult.success(showUrl);
    }

}
