package com.dingkai.personManage.provider.impl;

import com.dingkai.personManage.api.service.PersonRestService;
import com.dingkai.personManage.common.response.BaseResult;
import org.springframework.stereotype.Service;

/**
 * @Author dingkai
 * @Date 2020/7/29 21:55
 */
@Service
public class PersonRestServiceImpl implements PersonRestService {

    @Override
    public BaseResult getPersonInfoByIdNumber(String idNumber) {

        return BaseResult.success();
    }
}
