package com.dingkai.personManage.business.code.person.service;

import com.dingkai.personManage.business.code.person.vo.req.PersonQueryReqVo;
import com.dingkai.personManage.common.response.PagedResponseVO;
import com.dingkai.personManage.business.code.person.vo.PersonVo;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

public interface PersonService {

    void savePerson(PersonVo personVO);

    PagedResponseVO<PersonVo> getPersonByCondition(PersonQueryReqVo personQueryReqVO);

    PersonVo getPersonById(Integer id) throws Exception;

    void deletePersonByIds(List<Integer> ids);

    void exportPersonByCondition(PersonQueryReqVo personQueryReqVO, HttpServletResponse response) throws IOException, IllegalAccessException;

    void downloadTemplate(HttpServletResponse response) throws IOException;
}
