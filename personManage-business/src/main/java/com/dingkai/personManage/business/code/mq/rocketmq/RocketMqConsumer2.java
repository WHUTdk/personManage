package com.dingkai.personManage.business.code.mq.rocketmq;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.ConsumeOrderlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeOrderlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerOrderly;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.common.consumer.ConsumeFromWhere;
import org.apache.rocketmq.common.message.MessageExt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.List;

/**
 * @author dingkai1
 * @desc
 * @date 2021/1/4 11:40
 */
@Component
public class RocketMqConsumer2 {

    private static final Logger logger = LoggerFactory.getLogger(RocketMqConsumer2.class);

    @Value("${rocketmq.name-server}")
    private String nameServer;

    //@PostConstruct
    public void initRocketMq() {
        createRocketMQConsumer("dingkai1_test", "dingkai1_consumer_group_1");
    }

    public void createRocketMQConsumer(String topic, String group) {
        DefaultMQPushConsumer consumer = new DefaultMQPushConsumer(group);
        consumer.setNamesrvAddr(nameServer);
//        consumer.setConsumeThreadMin(consumeThreadMin);
//        consumer.setConsumeThreadMax(consumeThreadMax);
        consumer.registerMessageListener(new MessageListenerOrderly() {
            @Override
            public ConsumeOrderlyStatus consumeMessage(List<MessageExt> list, ConsumeOrderlyContext consumeOrderlyContext) {
                if (CollectionUtils.isEmpty(list)) {
                    return ConsumeOrderlyStatus.SUCCESS;
                }
                for (MessageExt message : list) {
                    String msg = new String(message.getBody());
                    logger.info("当前线程:{},topic:{},messageId:{},接受到的消息为:{}", Thread.currentThread().getId(), topic, message.getMsgId(), msg);
                }
                return ConsumeOrderlyStatus.SUCCESS;
            }
        });
        //设置Consumer第一次启动是从队列头部开始消费还是队列尾部开始消费
        //如果非第一次启动，那么按照上次消费的位置继续消费
        consumer.setConsumeFromWhere(ConsumeFromWhere.CONSUME_FROM_LAST_OFFSET);
        //设置消费模型，集群还是广播，默认为集群
        //consumer.setMessageModel(MessageModel.CLUSTERING);
        //设置一次消费消息的条数，默认为1条
//        consumer.setConsumeMessageBatchMaxSize(consumeMessageBatchMaxSize);
        try {
            //设置该消费者订阅的主题和tag，如果是订阅该主题下的所有tag，则tag使用*；如果需要指定订阅该主题下的某些tag，则使用||分割，例如tag1||tag2||tag3
            consumer.subscribe(topic, "*");
            consumer.start();
            logger.info("启动rocketmq消费者成功,group:{},topic:{}", group, topic);
        } catch (MQClientException e) {
            logger.error("启动rocketmq消费者异常,group:{},topic:{},", group, topic, e);
        }
    }

}
