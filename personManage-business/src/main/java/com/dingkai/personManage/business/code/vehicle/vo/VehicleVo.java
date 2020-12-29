package com.dingkai.personManage.business.code.vehicle.vo;

import lombok.Data;

/**
 * @Author dingkai
 * @Date 2020/7/17 23:54
 */
@Data
public class VehicleVo {

    private Integer id;

    private String plateNo;

    private String brand;

    private String type;

    private Integer personId;

    private Integer isImport;

}
