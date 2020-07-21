package com.dingkai.personManage.business.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.dingkai.personManage.business.dao.VehicleMapper;
import com.dingkai.personManage.business.domain.VehicleDO;
import com.dingkai.personManage.business.service.VehicleService;
import com.dingkai.personManage.business.vo.VehicleVO;
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
    public void saveVehicle(VehicleVO vehicleVO) throws Exception {
        Integer personId = vehicleVO.getPersonId();
        if (personId == null) {
            throw new Exception("车辆必须关联用户id");
        }
        VehicleDO vehicleDO = new VehicleDO();
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
    public VehicleVO getVehicleById(Integer id) throws Exception {
        VehicleDO vehicleDO = vehicleMapper.selectById(id);
        if (vehicleDO == null) {
            throw new Exception("根据id查询数据不存在");
        }
        return copyVehicleDOToVehicleVO(vehicleDO);
    }

    /**
     * 根据人员id获取人员名下车辆信息
     */
    @Override
    public List<VehicleVO> getVehicleVOSByPersonId(Integer personId) {
        QueryWrapper<VehicleDO> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("person_id", personId);
        List<VehicleDO> vehicleDOS = vehicleMapper.selectList(queryWrapper);
        ArrayList<VehicleVO> vehicleVOS = new ArrayList<>();
        for (VehicleDO vehicleDO : vehicleDOS) {
            VehicleVO vehicleVO = copyVehicleDOToVehicleVO(vehicleDO);
            vehicleVOS.add(vehicleVO);
        }
        return vehicleVOS;
    }

    /**
     * 根据人员id获取人员名下车辆信息
     */
    @Override
    public List<VehicleDO> getVehicleDOSByPersonId(Integer personId) {
        QueryWrapper<VehicleDO> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("person_id", personId);
        return vehicleMapper.selectList(queryWrapper);
    }

    /**
     * DO-VO属性赋值
     */
    private VehicleVO copyVehicleDOToVehicleVO(VehicleDO vehicleDO) {
        VehicleVO vehicleVO = new VehicleVO();
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
                QueryWrapper<VehicleDO> queryWrapper = new QueryWrapper<>();
                queryWrapper.eq("person_id", personId);
                vehicleMapper.delete(queryWrapper);
            }
        }
    }


}
