package com.dingkai.personManage;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication(scanBasePackages = {"com.dingkai.personManage"})
@MapperScan(basePackages = {"com.dingkai.personManage.*.dao"})
@ServletComponentScan(basePackages="com.dingkai.personManage.*.filter")
public class StartApplication {

    public static void main(String[] args) {
        SpringApplication.run(StartApplication.class, args);
    }
}
