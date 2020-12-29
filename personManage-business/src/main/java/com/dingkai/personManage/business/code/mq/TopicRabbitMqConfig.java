package com.dingkai.personManage.business.code.mq;

import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

/**
 * @Author dingkai
 * @Date 2020/8/10 22:14
 */
@Configuration
@PropertySource("classpath:mq.properties")
public class TopicRabbitMqConfig {

    //队列名称
    @Value("${topic_queue}")
    private String topicQueue;

    //交换机名称
    @Value("${topic_exchange}")
    private String topicExchange;

    //routingKey名称
    @Value("${topic_routingKey}")
    private String topicRoutingKey;//*代表一个词,#任意个

    /**
     * 声明交换机对象-topic模式(通配符模式)
     */
    @Bean
    public Exchange topicExchange() {
        //durable(true) 持久化，mq重启之后交换机还在
        return ExchangeBuilder.topicExchange(topicExchange).durable(true).build();
    }

    /**
     * 声明队列
     */
    @Bean
    public Queue topicQueue() {
        return new Queue(topicQueue);
    }

    /**
     * 指定routingKey来绑定交换机和队列
     */
    @Bean
    public Binding bindingRoutingKey(@Qualifier("topicQueue") Queue queue,
                                           @Qualifier("topicExchange") Exchange topicExchange) {
        return BindingBuilder.bind(queue).to(topicExchange).with(topicRoutingKey).noargs();
    }

}
