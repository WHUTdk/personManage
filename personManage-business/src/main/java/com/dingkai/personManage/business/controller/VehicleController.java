package com.dingkai.personManage.business.controller;

import com.dingkai.personManage.business.service.VehicleService;
import com.dingkai.personManage.business.vo.VehicleVO;
import com.dingkai.personManage.common.response.BaseResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

    @RequestMapping("/saveVehicle")
    public BaseResult saveVehicle(@RequestBody VehicleVO vehicleVO) {
        try {
            vehicleService.saveVehicle(vehicleVO);
            return BaseResult.success();
        } catch (Exception e) {
            logger.error("保存车辆信息出错，错误信息：{}", e.getMessage());
            return BaseResult.error("-1", e.getMessage());
        }
    }

}
