package com.dingkai.personManage.business.code.vehicle.vo.resp;

import lombok.Data;

/**
 * @Author dingkai
 * @Date 2020/7/17 23:54
 */
@Data
public class SelVehicleRespVo {

    private Integer id;

    private String plateNo;

    private String brand;

    private String type;

    private Integer personId;

    private Integer isImport;

}
