package com.dingkai.personManage;

import com.dingkai.personManage.business.code.mq.rocketmq.RocketMqProducer;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author dingkai1
 * @desc
 * @date 2020/12/31 17:48
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class RocketMqTest {

    @Autowired
    private RocketMqProducer rocketMqProducer;

    @Test
    public void producerTest(){
        rocketMqProducer.sendMsg();
        System.out.println("11");
    }

}
