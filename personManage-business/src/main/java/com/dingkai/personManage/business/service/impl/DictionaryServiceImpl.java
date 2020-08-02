package com.dingkai.personManage.business.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.dingkai.personManage.business.dao.DictionaryMapper;
import com.dingkai.personManage.business.domain.DictionaryDO;
import com.dingkai.personManage.business.service.DictionaryService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @Author dingkai
 * @Date 2020/7/18 23:23
 */
@Service
public class DictionaryServiceImpl implements DictionaryService {

    private static final Logger logger = LoggerFactory.getLogger(DictionaryServiceImpl.class);

    @Resource
    private DictionaryMapper dictionaryMapper;

    @Override
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

    @Override
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
