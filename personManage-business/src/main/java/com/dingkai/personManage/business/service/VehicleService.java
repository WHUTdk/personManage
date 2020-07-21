package com.dingkai.personManage.business.service;

import com.dingkai.personManage.business.domain.VehicleDO;
import com.dingkai.personManage.business.vo.VehicleVO;

import java.util.List;

/**
 * @Author dingkai
 * @Date 2020/7/17 23:52
 */
public interface VehicleService {

    void saveVehicle(VehicleVO vehicleVO) throws Exception;

    void deleteVehicleByIds(List<Integer> ids);

    VehicleVO getVehicleById(Integer id) throws Exception;

    List<VehicleVO> getVehicleVOSByPersonId(Integer personId);

    List<VehicleDO> getVehicleDOSByPersonId(Integer personId);

    void deleteVehicleByPersonIds(List<Integer> personIds);

}
