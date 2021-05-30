package com.dingkai.personManage.business.code.wechat.utils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author dingkai1
 * @desc
 * @date 2021/3/11 18:09
 */
public class WechatUtil {

    /**
     * 创建点击按钮
     */
    public static Map<String, Object> createClickBtn(String name, String key) {
        Map<String, Object> obj = new HashMap<String, Object>();
        obj.put("type", "click");
        obj.put("name", name);
        obj.put("key", key);
        return obj;
    }

    /**
     * 创建跳转url按钮
     */
    public static Map<String, Object> createViewBtn(String name, String url) {
        Map<String, Object> obj = new HashMap<String, Object>();
        obj.put("type", "view");
        obj.put("name", name);
        obj.put("url", url);
        return obj;
    }

    /**
     * 创建发送定位按钮
     */
    public static Map<String, Object> createLocationBtn(String name, String key) {
        Map<String, Object> obj = new HashMap<String, Object>();
        obj.put("type", "location_select");
        obj.put("name", name);
        obj.put("key", key);
        return obj;
    }

    /**
     * 创建子菜单
     */
    public static Map<String, Object> createSubBtn(String name, List<Object> subButtons) {
        Map<String, Object> obj = new HashMap<String, Object>();
        obj.put("name", name);
        obj.put("sub_button", subButtons);
        return obj;
    }

}
