package com.dingkai.personManage.business.code.vehicle.vo.req;

import io.swagger.annotations.ApiModel;
import lombok.Data;

/**
 * @Author dingkai
 * @Date 2020/7/17 23:54
 */
@Data
@ApiModel(value = "车辆保存vo")
public class SaveVehicleReqVo {

    private Integer id;

    private Integer personId;

    private String plateNo;

    private String brand;

    private String type;

    private Integer isImport;

}
