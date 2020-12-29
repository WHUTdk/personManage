package com.dingkai.personManage.business.code.bo;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * @Author dingkai
 * @Date 2020/7/12 23:17
 */
@Data
public class OperateLogBo {

    private String operateUsername;

    private String operateIp;

    private String operateMethod;

    private LocalDateTime operateTime;

    private String requestUrl;

    private String requestParam;

    private String requestBody;

    private Boolean operateResult;

}
