package com.dingkai.personManage;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication(scanBasePackages = {"com.dingkai.personManage"})
@MapperScan(basePackages = {"com.dingkai.personManage.**.dao"})
@ServletComponentScan(basePackages = "com.dingkai.personManage")
@EnableAsync
public class StartApplication extends SpringBootServletInitializer {

    public static void main(String[] args) {
        SpringApplication.run(StartApplication.class, args);
    }

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(StartApplication.class);
    }
}
