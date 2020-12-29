package com.dingkai.personManage.business.code.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.dingkai.personManage.business.code.domain.VehicleDo;
import com.dingkai.personManage.business.code.service.VehicleService;
import com.dingkai.personManage.business.code.vo.VehicleVo;
import com.dingkai.personManage.business.code.dao.VehicleMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author dingkai
 * @Date 2020/7/17 23:53
 */
@Service
public class VehicleServiceImpl implements VehicleService {

    @Resource
    private VehicleMapper vehicleMapper;

    /**
     * 保存车辆信息
     */
    @Override
    public void saveVehicle(VehicleVo vehicleVO) throws Exception {
        Integer personId = vehicleVO.getPersonId();
        if (personId == null) {
            throw new Exception("车辆必须关联用户id");
        }
        VehicleDo vehicleDO = new VehicleDo();
        BeanUtils.copyProperties(vehicleVO, vehicleDO);
        vehicleDO.insertOrUpdate();
    }

    /**
     * 根据id集合删除车辆信息
     */
    @Override
    public void deleteVehicleByIds(List<Integer> ids) {
        vehicleMapper.deleteBatchIds(ids);
    }

    /**
     * 根据id查询车辆信息
     */
    @Override
    public VehicleVo getVehicleById(Integer id) throws Exception {
        VehicleDo vehicleDO = vehicleMapper.selectById(id);
        if (vehicleDO == null) {
            throw new Exception("根据id查询数据不存在");
        }
        return copyVehicleDOToVehicleVO(vehicleDO);
    }

    /**
     * 根据人员id获取人员名下车辆信息
     */
    @Override
    public List<VehicleVo> getVehicleVOSByPersonId(Integer personId) {
        QueryWrapper<VehicleDo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("person_id", personId);
        List<VehicleDo> vehicleDos = vehicleMapper.selectList(queryWrapper);
        ArrayList<VehicleVo> vehicleVos = new ArrayList<>();
        for (VehicleDo vehicleDO : vehicleDos) {
            VehicleVo vehicleVO = copyVehicleDOToVehicleVO(vehicleDO);
            vehicleVos.add(vehicleVO);
        }
        return vehicleVos;
    }

    /**
     * 根据人员id获取人员名下车辆信息
     */
    @Override
    public List<VehicleDo> getVehicleDOSByPersonId(Integer personId) {
        QueryWrapper<VehicleDo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("person_id", personId);
        return vehicleMapper.selectList(queryWrapper);
    }

    /**
     * DO-VO属性赋值
     */
    private VehicleVo copyVehicleDOToVehicleVO(VehicleDo vehicleDO) {
        VehicleVo vehicleVO = new VehicleVo();
        BeanUtils.copyProperties(vehicleDO, vehicleVO);
        return vehicleVO;
    }

    /**
     * 根据人员id删除人员名下车辆信息
     */
    @Override
    public void deleteVehicleByPersonIds(List<Integer> personIds) {
        if (CollectionUtils.isNotEmpty(personIds)) {
            for (Integer personId : personIds) {
                QueryWrapper<VehicleDo> queryWrapper = new QueryWrapper<>();
                queryWrapper.eq("person_id", personId);
                vehicleMapper.delete(queryWrapper);
            }
        }
    }


}
