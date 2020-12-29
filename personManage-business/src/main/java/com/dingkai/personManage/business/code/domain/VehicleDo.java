package com.dingkai.personManage.business.code.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @Author dingkai
 * @Date 2020/7/13 23:25
 */
@EqualsAndHashCode(callSuper = false)
@Data
@TableName("vehicle_info")
public class VehicleDo extends Model<VehicleDo> {

    @TableId(type = IdType.AUTO)
    private Integer id;

    private String plateNo;

    private String brand;

    private String type;

    private Integer personId;

    private Integer isImport;//是否进口，0否；1是

}
