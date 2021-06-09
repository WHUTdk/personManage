package com.dingkai.personManage;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @Author dingkai
 * @Date 2021/6/8 11:27
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class ApplicationContextTest {

    @Autowired
    private ApplicationContext applicationContext;

    @Test
    public void test(){
        Object object = applicationContext.getBean("customServiceTest");
        System.out.println(object);
    }

}
