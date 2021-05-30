package com.dingkai.personManage.business.code.wechat.controller;

import com.dingkai.personManage.business.code.wechat.service.WechatService;
import com.dingkai.personManage.common.response.BaseResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author dingkai1
 * @desc
 * @date 2021/3/11 18:05
 */
@Api(tags = "微信相关接口")
@RestController
@RequestMapping("/wechat")
public class WechatController {

    @Autowired
    private WechatService wechatService;

    @ApiOperation("创建菜单")
    @PostMapping("/createMenu")
    public BaseResult createMenu(){
        return wechatService.createMenu();
    }

    @ApiOperation("获取临时二维码")
    @PostMapping("/getTempQrCode")
    public BaseResult getTempQrCode(){
        return wechatService.getTempQrCode();
    }


}
