package com.dingkai.personManage.business.utils;

import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.HashMap;

/**
 * @Author dingkai
 * @Date 2020/8/4 21:12
 */
public class HttpUtil {

    @Autowired
    private RestTemplate restTemplate;

    public JSONObject doPost(String url, HashMap<String, Object> body) {
        HttpEntity httpEntity = new HttpEntity<>(body);
        return restTemplate.postForObject(url, httpEntity, JSONObject.class);
    }

    public JSONObject doPost(String url, HashMap<String, Object> body, HttpHeaders headers) {
        HttpEntity httpEntity = new HttpEntity<>(body, headers);
        return restTemplate.postForObject(url, httpEntity, JSONObject.class);
    }

    public ResponseEntity<JSONObject> doPostForEntity(String url, HashMap<String, Object> body, HttpHeaders headers) {
        HttpEntity httpEntity = new HttpEntity<>(body, headers);
        return restTemplate.postForEntity(url, httpEntity, JSONObject.class);
    }

    public ResponseEntity<JSONObject> doPostForEntity(String url, HashMap<String, Object> body) {
        HttpEntity httpEntity = new HttpEntity<>(body);
        return restTemplate.postForEntity(url, httpEntity, JSONObject.class);
    }

    public JSONObject doGet(String url) {
        return restTemplate.getForObject(url, JSONObject.class);
    }

    public JSONObject doGet(String url, HashMap<String, String> params) {
        LinkedMultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        for (String key : params.keySet()) {
            map.add(key, params.get(key));
        }
        URI uri = UriComponentsBuilder.fromHttpUrl(url)
                .queryParams(map).build().encode().toUri();
        return restTemplate.getForObject(uri, JSONObject.class);
    }

    public JSONObject doGet(String url, LinkedMultiValueMap<String, String> params) {
        URI uri = UriComponentsBuilder.fromHttpUrl(url)
                .queryParams(params).build().encode().toUri();
        return restTemplate.getForObject(uri, JSONObject.class);
    }

    public JSONObject doGet(String url, HashMap<String, String> params, HttpHeaders headers) {
        LinkedMultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        for (String key : params.keySet()) {
            map.add(key, params.get(key));
        }
        URI uri = UriComponentsBuilder.fromHttpUrl(url)
                .queryParams(map).build().encode().toUri();
        HttpEntity httpEntity = new HttpEntity<>(headers);
        return restTemplate.exchange(uri, HttpMethod.GET, httpEntity, JSONObject.class).getBody();
    }
}
