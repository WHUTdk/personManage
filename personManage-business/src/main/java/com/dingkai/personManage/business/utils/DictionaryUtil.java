package com.dingkai.personManage.business.utils;

import com.dingkai.personManage.business.annotation.DictionaryTransfer;
import com.dingkai.personManage.business.service.DictionaryService;
import org.apache.commons.lang3.StringUtils;
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

    /**
     * 对象字段值code转name
     */
    public <T> void codeToName(T t) throws IllegalAccessException {
        Field[] fields = BeanUtil.getFields(t);
        for (Field field : fields) {
            DictionaryTransfer dictionaryTransfer = field.getDeclaredAnnotation(DictionaryTransfer.class);
            if (dictionaryTransfer != null && StringUtils.isNotEmpty(dictionaryTransfer.value()) && field.get(t) != null) {
                String groupName = dictionaryTransfer.value();
                String code = (String) field.get(t);
                if (StringUtils.isNotEmpty(code)) {
                    String name = dictionaryService.getNameByGroupAndCode(groupName, code);
                    field.set(t, name);
                }
            }
        }
    }

    /**
     * 对象字段值name转code
     */
    public <T> void nameToCode(T t) throws IllegalAccessException {
        Field[] fields = BeanUtil.getFields(t);
        for (Field field : fields) {
            DictionaryTransfer dictionaryTransfer = field.getDeclaredAnnotation(DictionaryTransfer.class);
            if (dictionaryTransfer != null && StringUtils.isNotEmpty(dictionaryTransfer.value()) && field.get(t) != null) {
                String groupName = dictionaryTransfer.value();
                String name = (String) field.get(t);
                if (StringUtils.isNotEmpty(name)) {
                    String code = dictionaryService.getCodeByGroupAndName(groupName, name);
                    field.set(t, code);
                }
            }
        }
    }


}
