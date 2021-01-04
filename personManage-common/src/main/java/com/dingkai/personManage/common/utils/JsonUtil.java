package com.dingkai.personManage.common.utils;

import com.dingkai.personManage.common.response.BaseResult;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

/**
 * @author dingkai1
 * @desc
 * @date 2021/1/4 16:11
 */
public class JsonUtil {

    private static ObjectMapper objectMapper = new ObjectMapper();

    static {
        // 设置时区
        objectMapper.setTimeZone(TimeZone.getTimeZone("GMT+8"))
                // Date 对象的格式，非 java8 时间
                .setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"))
                // 默认忽略值为 null 的属性，暂时不忽略，放开注释即不会序列化为 null 的属性
//				.setSerializationInclusion(JsonInclude.Include.NON_NULL)
                // 禁止打印时间为时间戳
                .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
                // 禁止使用科学记数法
                .enable(JsonGenerator.Feature.WRITE_BIGDECIMAL_AS_PLAIN)
                // 忽略空Bean转json的错误
                .configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false)
                // 忽略未知属性，防止json字符串中存在，java对象中不存在对应属性的情况出现错误
                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    /**
     * 对象转为json字符串
     */
    public static <T> String toJsonString(T obj) {
        if (obj == null) {
            return null;
        }
        try {
            return objectMapper.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * json字符串转为对象
     */
    public static <T> T jsonToBean(String jsonString, Class<T> cls) {
        try {
            if (StringUtils.isBlank(jsonString)) {
                return null;
            }
            return objectMapper.readValue(jsonString, cls);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static <T> List<T> jsonToList(String jsonString, Class<T> cls) {
        try {
            JavaType javaType = objectMapper.getTypeFactory().constructParametricType(ArrayList.class, cls);
            return objectMapper.readValue(jsonString, javaType);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    public static Map<String, Object> jsonToMap(String jsonString) {
        try {
            return objectMapper.readValue(jsonString, new TypeReference<Map<String, Object>>() {
            });
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) {
        BaseResult<Object> baseResult = new BaseResult<>();
        baseResult.setCode("0");
        baseResult.setMsg("msg...");
        ArrayList<String> list = new ArrayList<>();
        list.add("1");
        list.add("2");
        list.add("3");
        baseResult.setData(list);
        String jsonString = toJsonString(baseResult);
        System.out.println(jsonString);
        System.out.println(jsonToBean(jsonString, BaseResult.class));
        System.out.println(jsonToMap(jsonString));
        System.out.println(jsonToList(toJsonString(list), String.class));
    }

}
