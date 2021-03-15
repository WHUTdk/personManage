package com.dingkai.personManage.business.code.wechat.service;

import com.alibaba.fastjson.JSONObject;
import com.dingkai.personManage.business.code.wechat.config.WechatConfig;
import com.dingkai.personManage.business.code.wechat.constants.WechatRedisConstant;
import com.dingkai.personManage.business.code.wechat.constants.WechatUrlConstant;
import com.dingkai.personManage.common.utils.HttpUtil;
import com.dingkai.personManage.common.utils.RedisUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author dingkai1
 * @desc
 * @date 2021/3/11 10:04
 */
@Service
public class WechatQueryService {

    private static final Logger logger = LoggerFactory.getLogger(WechatQueryService.class);

    @Autowired
    private RedisUtil redisUtil;
    @Autowired
    private WechatConfig wechatConfig;
    @Autowired
    private HttpUtil httpUtil;

    /**
     * 获取微信的AccessToken
     *
     * @return
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
            if (expire == null) {
                expire = 7200;
            }
            //提前10min过期
            expire -= 10 * 60;
            redisUtil.set(redisKey, accessToken, expire);
        }
        return accessToken;
    }

}
