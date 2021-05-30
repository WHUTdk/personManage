package com.dingkai.personManage.common.enums;

/**
 * @Author dingkai
 * @Date 2021/5/30 22:53
 */
public enum CodeEnum {

    SUCCESS("0","成功"),
    ERROR("-1","失败"),

    ;

    private String code;

    private String message;

    CodeEnum(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
