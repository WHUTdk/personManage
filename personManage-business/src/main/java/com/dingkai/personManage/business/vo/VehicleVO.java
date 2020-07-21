package com.dingkai.personManage.business.vo;

import com.dingkai.personManage.business.annotation.CodeToName;
import lombok.Data;

/**
 * @Author dingkai
 * @Date 2020/7/17 23:54
 */
@Data
public class VehicleVO {

    private Integer id;

    private String plateNo;

    private String brand;

    private String type;

    private Integer personId;

    private Integer isImport;

}
