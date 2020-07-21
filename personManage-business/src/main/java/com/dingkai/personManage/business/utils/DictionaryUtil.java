package com.dingkai.personManage.business.utils;

import com.dingkai.personManage.business.annotation.CodeToName;
import com.dingkai.personManage.business.service.DictionaryService;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.formula.functions.T;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;

/**
 * @Author dingkai
 * @Date 2020/7/18 23:21
 */
@Component
public class DictionaryUtil {

    @Autowired
    private DictionaryService dictionaryService;

    public <T> void codeToName(T t) throws IllegalAccessException {
        Class<?> clazz = t.getClass();
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            field.setAccessible(true);
            CodeToName codeToName = field.getDeclaredAnnotation(CodeToName.class);
            if (codeToName != null && StringUtils.isNotEmpty(codeToName.value()) && field.get(t) != null) {
                String groupName = codeToName.value();
                String code = (String) field.get(t);
                if (StringUtils.isNotEmpty(code)) {
                    String name = dictionaryService.getNameByGroupAndCode(groupName, code);
                    field.set(t, name);
                }
            }
        }
    }

}
