package com.dingkai.personManage.business.code.vehicle.service;

import com.dingkai.personManage.business.code.person.vo.resp.SelPersonRespVo;
import com.dingkai.personManage.business.code.vehicle.entity.VehicleDo;
import com.dingkai.personManage.business.code.vehicle.vo.VehicleVo;
import com.dingkai.personManage.business.code.vehicle.vo.req.SaveVehicleReqVo;

import java.util.List;

/**
 * @Author dingkai
 * @Date 2020/7/17 23:52
 */
public interface VehicleService {

    void saveVehicle(SaveVehicleReqVo vehicleVO) throws Exception;

    void deleteVehicleByIds(List<Integer> ids);

    SelPersonRespVo getVehicleById(Integer id) throws Exception;

    List<SelPersonRespVo> getVehicleVOSByPersonId(Integer personId);

    List<VehicleDo> getVehicleDOSByPersonId(Integer personId);

    void deleteVehicleByPersonIds(List<Integer> personIds);

}
