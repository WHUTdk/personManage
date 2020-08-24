package com.dingkai.personManage.business.mq;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * @Author dingkai
 * @Date 2020/8/23 15:04
 */
@Component
public class RabbitMqConsumer {

    @RabbitListener(queues = "test_topic_queue")
    public void receiveMsg(String message){
        System.out.println("接收到rmq消息："+message);
    }

}
