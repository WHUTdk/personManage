package com.dingkai.personManage.business.code.wechat.controller;

import com.dingkai.personManage.business.code.wechat.service.WechatMenuService;
import com.dingkai.personManage.common.response.BaseResult;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author dingkai1
 * @desc
 * @date 2021/3/11 18:05
 */
@Api(tags = "微信菜单接口")
@RestController
@RequestMapping("/wechat")
public class WechatMenuController {

    @Autowired
    private WechatMenuService menuService;

    @PostMapping("/createMenu")
    public BaseResult createMenu(){
        return menuService.createMenu();
    }

}
