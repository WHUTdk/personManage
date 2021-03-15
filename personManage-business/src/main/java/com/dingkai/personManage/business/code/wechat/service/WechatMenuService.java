package com.dingkai.personManage.business.code.wechat.service;

import com.alibaba.fastjson.JSONObject;
import com.dingkai.personManage.business.code.wechat.constants.WechatUrlConstant;
import com.dingkai.personManage.business.code.wechat.enums.WechatEventKeyEnum;
import com.dingkai.personManage.business.code.wechat.utils.WechatUtil;
import com.dingkai.personManage.common.response.BaseResult;
import com.dingkai.personManage.common.utils.HttpUtil;
import com.dingkai.personManage.common.utils.JsonUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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
public class WechatMenuService {

    private static final Logger logger = LoggerFactory.getLogger(WechatMenuService.class);

    @Autowired
    private WechatQueryService queryService;
    @Autowired
    private HttpUtil httpUtil;

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
        String url = WechatUrlConstant.createMenuUrl.replace("#{accessToken}", queryService.getAccessToken());
        JSONObject jsonObject = httpUtil.doPostForJson(url, requestMap);
        logger.info("调用微信创建菜单接口，返回结果：{}", JSONObject.toJSONString(jsonObject));
        return BaseResult.success();
    }

}
