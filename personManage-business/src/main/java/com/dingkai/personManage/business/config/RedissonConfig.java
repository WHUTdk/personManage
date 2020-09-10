package com.dingkai.personManage.business.config;

import org.redisson.Redisson;
import org.redisson.config.Config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Author dingkai
 * @Date 2020/9/10 21:12
 */
@Configuration
public class RedissonConfig {

    @Bean
    public Redisson redisson(){
        Config config = new Config();
        config.useSingleServer().setAddress("redis://47.99.187.36:6379").setDatabase(0);
        return (Redisson) Redisson.create(config);
    }

}
