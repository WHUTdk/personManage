package com.dingkai.personManage.business.code.mq.rocketmq;

import com.dingkai.personManage.common.utils.JsonUtil;
import org.apache.rocketmq.client.exception.MQBrokerException;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.MQProducer;
import org.apache.rocketmq.client.producer.SendCallback;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.remoting.exception.RemotingException;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author dingkai1
 * @desc
 * @date 2020/12/31 17:38
 */
@Component
public class RocketMqProducer {

    @Autowired
    private RocketMQTemplate rocketMQTemplate;

    @Autowired
    private MQProducer rocketMQProducer;

    public void sendMsg() {
        String topic = "dingkai1_test";
        rocketMQTemplate.convertAndSend(topic, "hello");
        SendResult sendResult = rocketMQTemplate.syncSend(topic, "hello2");
        System.out.println(sendResult);
        //同步顺序发送 传入唯一标识hashKey保证消息发送到同一个队列中  hashKey：use this key to select queue. for example: orderId, productId
        String orderId = "1111";
        System.out.println(rocketMQTemplate.syncSendOrderly(topic, "hello3", orderId));
        try {
            Message message = new Message();
            message.setTopic(topic);
            message.setBody("hello4".getBytes());
            message.setKeys(orderId);
            SendResult send = rocketMQProducer.send(message);
            System.out.println(JsonUtil.toJsonString(send));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
