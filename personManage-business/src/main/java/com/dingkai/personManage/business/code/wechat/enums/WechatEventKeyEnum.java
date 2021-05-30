package com.dingkai.personManage.business.code.wechat.enums;

/**
 * @author dingkai1
 * @desc
 * @date 2021/3/12 17:11
 */
public enum WechatEventKeyEnum {

    CLICK_TEST("click","click_test"),
    CLICK_TEST1("click1","click1_test"),
    CLICK_TEST2("click2","click2_test"),
    ;

    private String name;

    private String key;

    WechatEventKeyEnum(String name, String key) {
        this.name = name;
        this.key = key;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}
