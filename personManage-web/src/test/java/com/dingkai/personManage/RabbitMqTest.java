package com.dingkai.personManage;

import com.dingkai.personManage.business.mq.RabbitMqProducer;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @Author dingkai
 * @Date 2020/8/11 22:15
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class RabbitMqTest {

    @Autowired
    private RabbitMqProducer rabbitMqProducer;

    @Autowired
    private AmqpTemplate rabbitMqTemplate;

    @Test
    public void producerTest() throws InterruptedException {
        rabbitMqProducer.sendMessage();
        Thread.sleep(5000);

    }

}
