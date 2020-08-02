package com.dingkai.personManage.business.utils;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.dingkai.personManage.business.annotation.DictionaryTransfer;
import com.dingkai.personManage.business.dao.DictionaryMapper;
import com.dingkai.personManage.business.domain.DictionaryDO;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.lang.reflect.Field;

/**
 * @Author dingkai
 * @Date 2020/7/18 23:21
 */
@Component
public class DictionaryUtil {

    private static final Logger logger = LoggerFactory.getLogger(DictionaryUtil.class);

    @Resource
    private DictionaryMapper dictionaryMapper;

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
                    String name = getNameByGroupAndCode(groupName, code);
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
                    String code = getCodeByGroupAndName(groupName, name);
                    field.set(t, code);
                }
            }
        }
    }

    public String getNameByGroupAndCode(String groupName, String code) {
        //一般是查询缓存
        QueryWrapper<DictionaryDO> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("group_name", groupName);
        queryWrapper.eq("code", code);
        DictionaryDO dictionaryDO = dictionaryMapper.selectOne(queryWrapper);
        if (dictionaryDO == null || StringUtils.isEmpty(dictionaryDO.getName())) {
            logger.error("根据groupName：{}和code：{}获取到的name为空", groupName, code);
            return null;
        }
        return dictionaryDO.getName();
    }

    public String getCodeByGroupAndName(String groupName, String name) {
        //一般是查询缓存
        QueryWrapper<DictionaryDO> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("group_name", groupName);
        queryWrapper.eq("name", name);
        DictionaryDO dictionaryDO = dictionaryMapper.selectOne(queryWrapper);
        if (dictionaryDO == null || StringUtils.isEmpty(dictionaryDO.getCode())) {
            logger.error("根据groupName：{}和name：{}获取到的code为空", groupName, name);
            return null;
        }
        return dictionaryDO.getCode();
    }


}
