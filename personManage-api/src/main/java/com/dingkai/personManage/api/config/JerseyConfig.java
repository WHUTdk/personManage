package com.dingkai.personManage.api.config;

import org.glassfish.jersey.server.ResourceConfig;
import org.springframework.stereotype.Component;

import javax.ws.rs.ApplicationPath;

/**
 * @Author dingkai
 * @Date 2020/7/29 23:54
 */
@Component
@ApplicationPath("/service/rs")
public class JerseyConfig extends ResourceConfig {

    public JerseyConfig() {
        packages("com.dingkai.personManage.api");
    }

}
