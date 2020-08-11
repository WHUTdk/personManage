package com.dingkai.personManage.business.mq;

import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @Author dingkai
 * @Date 2020/8/11 0:12
 */
@Component
public class RabbitMqProducer {

    @Autowired
    private AmqpTemplate rabbitMqTemplate;

    public void sendMessage(){
        rabbitMqTemplate.convertAndSend("test_topic_exchange","test.dingkai.key","test");
    }

}
