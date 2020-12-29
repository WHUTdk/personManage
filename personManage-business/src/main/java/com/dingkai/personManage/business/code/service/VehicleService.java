package com.dingkai.personManage.business.code.service;

import com.dingkai.personManage.business.code.domain.VehicleDo;
import com.dingkai.personManage.business.code.vo.VehicleVo;

import java.util.List;

/**
 * @Author dingkai
 * @Date 2020/7/17 23:52
 */
public interface VehicleService {

    void saveVehicle(VehicleVo vehicleVO) throws Exception;

    void deleteVehicleByIds(List<Integer> ids);

    VehicleVo getVehicleById(Integer id) throws Exception;

    List<VehicleVo> getVehicleVOSByPersonId(Integer personId);

    List<VehicleDo> getVehicleDOSByPersonId(Integer personId);

    void deleteVehicleByPersonIds(List<Integer> personIds);

}
