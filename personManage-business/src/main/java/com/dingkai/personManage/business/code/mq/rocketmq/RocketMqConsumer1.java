package com.dingkai.personManage.business.code.mq.rocketmq;

import org.apache.rocketmq.common.message.MessageExt;
import org.apache.rocketmq.spring.annotation.ConsumeMode;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * @author dingkai1
 * @desc
 * @date 2021/1/4 10:55
 */
@Component
@RocketMQMessageListener(
        topic = "dingkai1_test",
        consumerGroup = "dingkai1_consumer_group_1",
        consumeMode = ConsumeMode.ORDERLY
)
public class RocketMqConsumer1 implements RocketMQListener<MessageExt> {

    private static final Logger logger = LoggerFactory.getLogger(RocketMqConsumer1.class);

    @Override
    public void onMessage(MessageExt message) {
        String topic = message.getTopic();
        String body = new String(message.getBody());
        logger.info("RocketMqConsumer1接收到rocketmq消息，topic:{},body:{}", topic, body);
    }
}
