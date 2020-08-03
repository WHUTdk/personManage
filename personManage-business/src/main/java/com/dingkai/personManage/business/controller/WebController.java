package com.dingkai.personManage.business.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import springfox.documentation.annotations.ApiIgnore;

@ApiIgnore
@Controller
public class WebController {

    @RequestMapping("/")
    public String index(){
        return "index";
    }
}
