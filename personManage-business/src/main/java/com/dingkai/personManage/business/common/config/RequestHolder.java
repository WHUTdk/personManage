package com.dingkai.personManage.business.common.config;

import com.dingkai.personManage.business.common.filter.RequestWrapper;

/**
 * @Author dingkai
 * @Date 2020/7/12 23:47
 */
public class RequestHolder {

    private static final ThreadLocal<RequestWrapper> requestHolder = new ThreadLocal<RequestWrapper>();

    public static void setRequest(RequestWrapper request) {
        requestHolder.set(request);
    }

    public static RequestWrapper getRequest() {
        return requestHolder.get();
    }

    public static void remove() {
        requestHolder.remove();
    }
}

