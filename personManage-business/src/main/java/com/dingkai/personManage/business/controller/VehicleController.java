package com.dingkai.personManage.business.controller;

import com.dingkai.personManage.business.annotation.OperateLog;
import com.dingkai.personManage.business.service.VehicleService;
import com.dingkai.personManage.business.vo.PersonVO;
import com.dingkai.personManage.business.vo.VehicleVO;
import com.dingkai.personManage.common.response.BaseResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * @Author dingkai
 * @Date 2020/7/18 0:23
 */
@RestController
@RequestMapping("/vehicle")
public class VehicleController {

    private static final Logger logger = LoggerFactory.getLogger(VehicleController.class);

    @Autowired
    private VehicleService vehicleService;

    @OperateLog
    @PostMapping("/saveVehicle")
    public BaseResult saveVehicle(@RequestBody VehicleVO vehicleVO) {
        try {
            vehicleService.saveVehicle(vehicleVO);
            return BaseResult.success();
        } catch (Exception e) {
            logger.error("保存车辆信息出错，错误信息：{}", e.getMessage());
            return BaseResult.error("-1", e.getMessage());
        }
    }

    @OperateLog
    @PostMapping("/deleteVehicleByIds")
    public BaseResult deleteVehicleByIds(@RequestBody Map<String, List<Integer>> map) {
        try {
            List<Integer> ids = map.get("ids");
            vehicleService.deleteVehicleByIds(ids);
            return BaseResult.success();
        } catch (Exception e) {
            logger.error("根据id删除车辆信息出错，错误信息：{}", e.getMessage());
            return BaseResult.error("-1", e.getMessage());
        }
    }

    @OperateLog
    @GetMapping("/getVehicleById")
    public BaseResult getVehicleById(@RequestParam("id") Integer id) {
        try {
            VehicleVO vehicleById = vehicleService.getVehicleById(id);
            return BaseResult.success(vehicleById);
        } catch (Exception e) {
            logger.error("根据id查询人员信息出错，错误信息：{}", e.getMessage());
            return BaseResult.error("-1", e.getMessage());
        }
    }



}
