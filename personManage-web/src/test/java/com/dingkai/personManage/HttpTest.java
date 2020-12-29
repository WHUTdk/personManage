package com.dingkai.personManage;

import com.alibaba.fastjson.JSONObject;
import com.dingkai.personManage.common.utils.HttpUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.LinkedMultiValueMap;

/**
 * @Author dingkai
 * @Date 2020/8/4 21:48
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class HttpTest {

    @Autowired
    private HttpUtil httpUtil;

    @Test
    public void testHttps1(){
        String url="https://tcc.taobao.com/cc/json/mobile_tel_segment.htm";
        LinkedMultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("tel","15271876499");
        String result = httpUtil.doGetForString(url, params);
        System.out.println(result);
    }

    @Test
    public void testHttps2(){
        String url="https://api.apiopen.top/musicRankings";
        JSONObject jsonObject = httpUtil.doGetForJson(url);
        System.out.println(jsonObject.toJSONString());
    }

    @Test
    public void testHttps(){
        String url="http://api.apiopen.top/videoHomeTab";
        JSONObject jsonObject = httpUtil.doGetForJson(url);
        System.out.println(jsonObject.toJSONString());
    }

}
