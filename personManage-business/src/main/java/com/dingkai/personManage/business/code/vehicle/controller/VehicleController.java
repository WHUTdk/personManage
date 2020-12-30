package com.dingkai.personManage.business.code.vehicle.controller;

import com.dingkai.personManage.business.code.person.vo.resp.SelPersonRespVo;
import com.dingkai.personManage.business.code.vehicle.service.VehicleService;
import com.dingkai.personManage.business.code.vehicle.vo.VehicleVo;
import com.dingkai.personManage.business.code.vehicle.vo.req.SaveVehicleReqVo;
import com.dingkai.personManage.business.common.annotation.OperateLog;
import com.dingkai.personManage.common.response.BaseResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
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
@Api(tags = "人员车辆管理")
@RestController
@RequestMapping("/vehicle")
public class VehicleController {

    private static final Logger logger = LoggerFactory.getLogger(VehicleController.class);

    @Autowired
    private VehicleService vehicleService;

    @ApiOperation(value = "保存车辆信息")
    @OperateLog
    @PostMapping("/saveVehicle")
    public BaseResult saveVehicle(@RequestBody SaveVehicleReqVo saveVehicleReqVo) {
        try {
            vehicleService.saveVehicle(saveVehicleReqVo);
            return BaseResult.success();
        } catch (Exception e) {
            logger.error("保存车辆信息出错", e);
            return BaseResult.error("-1", e.getMessage());
        }
    }

    @ApiOperation(value = "根据id集合删除车辆信息")
    @OperateLog
    @PostMapping("/deleteVehicleByIds")
    public BaseResult deleteVehicleByIds(@RequestBody List<Integer> ids) {
        try {
            vehicleService.deleteVehicleByIds(ids);
            return BaseResult.success();
        } catch (Exception e) {
            logger.error("根据id删除车辆信息出错", e);
            return BaseResult.error("-1", e.getMessage());
        }
    }

    @ApiOperation(value = "根据id获取车辆信息")
    @OperateLog
    @GetMapping("/getVehicleById")
    public BaseResult getVehicleById(@RequestParam("id") Integer id) {
        try {
            SelPersonRespVo vehicleById = vehicleService.getVehicleById(id);
            return BaseResult.success(vehicleById);
        } catch (Exception e) {
            logger.error("根据id查询人员信息出错", e);
            return BaseResult.error("-1", e.getMessage());
        }
    }


}
