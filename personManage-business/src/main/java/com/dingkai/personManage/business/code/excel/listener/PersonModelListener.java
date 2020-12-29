package com.dingkai.personManage.business.code.excel.listener;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.dingkai.personManage.business.code.dao.PersonMapper;
import com.dingkai.personManage.business.code.domain.PersonDO;
import com.dingkai.personManage.business.code.excel.PersonImportModel;
import com.dingkai.personManage.business.common.utils.DictionaryUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author dingkai
 * @Date 2020/8/2 23:02
 */
public class PersonModelListener extends AnalysisEventListener<PersonImportModel> {

    private static final Logger logger = LoggerFactory.getLogger(PersonModelListener.class);

    /**
     * 批量处理数据
     */
    private static final int BATCH_COUNT = 1000;

    List<PersonImportModel> list = new ArrayList<>();

    private PersonMapper personMapper;

    private DictionaryUtil dictionaryUtil;

    public PersonModelListener(PersonMapper personMapper, DictionaryUtil dictionaryUtil) {
        this.personMapper = personMapper;
        this.dictionaryUtil = dictionaryUtil;
    }

    /**
     * 每解析一行调用一次
     */
    @Override
    public void invoke(PersonImportModel personImportModel, AnalysisContext analysisContext) {
        list.add(personImportModel);
        // 达到BATCH_COUNT了，需要去存储一次数据库，防止数据几万条数据在内存，容易OOM
        if (list.size() >= BATCH_COUNT) {
            handleData(list);
            // 存储完成清理 list
            list.clear();
        }
    }

    /**
     * 所有数据解析完后调用此方法
     */
    @Override
    public void doAfterAllAnalysed(AnalysisContext analysisContext) {
        handleData(list);
        list.clear();
    }

    /**
     * 处理数据
     */
    private void handleData(List<PersonImportModel> list) {
        for (PersonImportModel personImportModel : list) {
            try {
                dictionaryUtil.nameToCode(personImportModel);
                PersonDO personDO = new PersonDO();
                BeanUtils.copyProperties(personImportModel, personDO);
                String sex = personImportModel.getSex();
                personDO.setSex(StringUtils.isEmpty(sex) ? 0 : Integer.parseInt(sex));
                personMapper.insert(personDO);
            } catch (Exception e) {
                logger.error("解析excel数据出错，excel数据：{}，错误信息：{}", personImportModel, e.getMessage());
            }
        }
    }


}
