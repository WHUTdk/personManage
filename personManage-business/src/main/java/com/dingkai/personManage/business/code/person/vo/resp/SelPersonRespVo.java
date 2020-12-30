package com.dingkai.personManage.business.code.person.vo.resp;

import com.dingkai.personManage.business.code.vehicle.vo.VehicleVo;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * @author dingkai1
 * @desc
 * @date 2020/12/30 10:01
 */
@Data
public class SelPersonRespVo {

    private Integer id;

    private String name;

    private String idNumber;

    private Integer sex;//0未知；1男；2女

    private String ethnicity;//民族

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate birthday;

    private String residentialAddress;//居住地址

    private String householdAddress;//户籍地址

    private List<SelPersonRespVo> vehicleVos = new ArrayList<>();

}
