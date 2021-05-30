package com.dingkai.personManage.business.code.mq.rocketmq;

import com.dingkai.personManage.common.utils.JsonUtil;
import org.apache.rocketmq.client.exception.MQBrokerException;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.*;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.common.message.MessageQueue;
import org.apache.rocketmq.remoting.exception.RemotingException;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;

import java.util.List;

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
        //使用springBoot包装方法，发送带tag、key值和带分区的
        org.springframework.messaging.Message<String> springMsg = MessageBuilder.withPayload("hello3").setHeader("KEYS", orderId).build();
        System.out.println(rocketMQTemplate.syncSendOrderly(topic + ":tagA", springMsg, orderId));
        try {
            //使用原生方法发送带tag、key值、分区
            Message message = new Message();
            message.setTopic(topic);
            message.setTags("tagA");
            message.setBody("hello4".getBytes());
            message.setKeys(orderId);
            SendResult send = rocketMQProducer.send(message, new MessageQueueSelector() {
                @Override
                public MessageQueue select(List<MessageQueue> mqs, Message msg, Object arg) {
                    int value = arg.hashCode();
                    if (value < 0) {
                        value = Math.abs(value);
                    }
                    value = value % mqs.size();
                    return mqs.get(value);
                }
            }, orderId);
            System.out.println(JsonUtil.toJsonString(send));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
