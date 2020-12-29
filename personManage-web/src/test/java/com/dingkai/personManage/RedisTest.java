package com.dingkai.personManage;

import com.dingkai.personManage.business.code.person.entity.PersonDo;
import com.dingkai.personManage.common.utils.RedisUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author dingkai
 * @Date 2020/8/8 17:51
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class RedisTest {

    @Autowired
    private RedisUtil redisUtil;

    @Test
    public void redisTest() {
        redisUtil.set("k1", "v1");
        System.out.println(redisUtil.get("k1"));
        PersonDo personDO = new PersonDo();
        personDO.setName("丁凯");
        personDO.setIdNumber("420704199509045053");
        personDO.setSex(1);
        personDO.setHouseholdAddress("湖北省武汉市江夏区");
        personDO.setResidentialAddress("浙江省杭州市萧山区");
        redisUtil.set("dk", personDO);
        PersonDo dk = (PersonDo) redisUtil.get("dk");
        System.out.println(dk);
        HashMap<Object, Object> map = new HashMap<>();
        map.put("k1", "v1");
        map.put("k2", dk);
        redisUtil.hmSet("dingkai", map);
        Map<Object, Object> dingkai = redisUtil.hmGet("dingkai");
        System.out.println(dingkai);
        Object o = redisUtil.hGet("dingkai", "k2");
        System.out.println(o);
    }

}
