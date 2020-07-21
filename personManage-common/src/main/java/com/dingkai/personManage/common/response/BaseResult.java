package com.dingkai.personManage.common.response;

public class BaseResult<T> {

    private static final String successCode = "0";
    private static final String errorCode = "-1";
    private static final String successMsg = "success";
    private String code;

    private String msg;

    private T data;

    public BaseResult() {
    }

    public BaseResult(String code, String msg, T data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    public BaseResult(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public BaseResult(T data) {
        this.data = data;
    }

    public static <T> BaseResult<T> success() {
        return new BaseResult<T>(successCode, successMsg);
    }

    public static <T> BaseResult<T> success(T data) {
        return new BaseResult<T>(successCode, successMsg, data);
    }

    public static <T> BaseResult<T> error(String code, String msg) {
        return new BaseResult<T>(code, msg);
    }

    public static <T> BaseResult<T> error(String code, String msg, T data) {
        return new BaseResult<T>(code, msg, data);
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

}
