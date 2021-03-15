package com.dingkai.personManage.common.utils;

import com.alibaba.fastjson.JSONObject;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.commons.CommonsMultipartFile;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.File;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author dingkai
 * @Date 2020/8/4 21:12
 */
@Component
public class HttpUtil {

    @Autowired
    private RestTemplate restTemplate;

    public JSONObject doPostForJson(String url, Map<String, Object> body) {
        HttpEntity httpEntity = new HttpEntity<>(body);
        return restTemplate.postForObject(url, httpEntity, JSONObject.class);
    }

    public String doPostForString(String url, Map<String, Object> body) {
        HttpEntity httpEntity = new HttpEntity<>(body);
        return restTemplate.postForObject(url, httpEntity, String.class);
    }

    public JSONObject doPostForJson(String url, Map<String, Object> body, HttpHeaders headers) {
        HttpEntity httpEntity = new HttpEntity<>(body, headers);
        return restTemplate.postForObject(url, httpEntity, JSONObject.class);
    }

    public String doPostForString(String url, Map<String, Object> body, HttpHeaders headers) {
        HttpEntity httpEntity = new HttpEntity<>(body, headers);
        return restTemplate.postForObject(url, httpEntity, String.class);
    }

    public ResponseEntity<JSONObject> doPostForEntity(String url, Map<String, Object> body) {
        HttpEntity httpEntity = new HttpEntity<>(body);
        return restTemplate.postForEntity(url, httpEntity, JSONObject.class);
    }

    public ResponseEntity<JSONObject> doPostForEntity(String url, Map<String, Object> body, HttpHeaders headers) {
        HttpEntity httpEntity = new HttpEntity<>(body, headers);
        return restTemplate.postForEntity(url, httpEntity, JSONObject.class);
    }

    public JSONObject doGetForJson(String url) {
        return restTemplate.getForObject(url, JSONObject.class);
    }

    public String doGetForString(String url) {
        return restTemplate.getForObject(url, String.class);
    }

    public JSONObject doGetForJson(String url, Map<String, Object> params) {
        LinkedMultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        for (String key : params.keySet()) {
            map.add(key, String.valueOf(params.get(key)));
        }
        URI uri = UriComponentsBuilder.fromHttpUrl(url)
                .queryParams(map).build().encode().toUri();
        return restTemplate.getForObject(uri, JSONObject.class);
    }

    public JSONObject doGetForJson(String url, LinkedMultiValueMap<String, String> params) {
        URI uri = UriComponentsBuilder.fromHttpUrl(url)
                .queryParams(params).build().encode().toUri();
        return restTemplate.getForObject(uri, JSONObject.class);
    }

    public String doGetForString(String url, LinkedMultiValueMap<String, String> params) {
        URI uri = UriComponentsBuilder.fromHttpUrl(url)
                .queryParams(params).build().encode().toUri();
        return restTemplate.getForObject(uri, String.class);
    }

    public JSONObject doGetForJson(String url, Map<String, Object> params, HttpHeaders headers) {
        LinkedMultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        for (String key : params.keySet()) {
            map.add(key, String.valueOf(params.get(key)));
        }
        URI uri = UriComponentsBuilder.fromHttpUrl(url)
                .queryParams(map).build().encode().toUri();
        HttpEntity httpEntity = new HttpEntity<>(headers);
        return restTemplate.exchange(uri, HttpMethod.GET, httpEntity, JSONObject.class).getBody();
    }

    public JSONObject doGetForJson(String url, LinkedMultiValueMap<String, String> params, HttpHeaders headers) {
        URI uri = UriComponentsBuilder.fromHttpUrl(url)
                .queryParams(params).build().encode().toUri();
        HttpEntity httpEntity = new HttpEntity<>(headers);
        return restTemplate.exchange(uri, HttpMethod.GET, httpEntity, JSONObject.class).getBody();
    }

    public String doGetForString(String url, LinkedMultiValueMap<String, String> params, HttpHeaders headers) {
        URI uri = UriComponentsBuilder.fromHttpUrl(url)
                .queryParams(params).build().encode().toUri();
        HttpEntity httpEntity = new HttpEntity<>(headers);
        return restTemplate.exchange(uri, HttpMethod.GET, httpEntity, String.class).getBody();
    }

    public String uploadFile(String url, MultiValueMap<String, Object> param) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Accept", MediaType.APPLICATION_JSON.toString());
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);
        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(param, headers);
        ResponseEntity<String> result = restTemplate.postForEntity(url, requestEntity, String.class);
        return result.getBody();
    }

}
