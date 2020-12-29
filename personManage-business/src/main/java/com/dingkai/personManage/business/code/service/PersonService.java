package com.dingkai.personManage.business.code.service;

import com.dingkai.personManage.business.code.vo.PersonQueryVO;
import com.dingkai.personManage.common.response.PagedResponseVO;
import com.dingkai.personManage.business.code.vo.PersonVO;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

public interface PersonService {

    void savePerson(PersonVO personVO);

    PagedResponseVO<PersonVO> getPersonByCondition(PersonQueryVO personQueryVO);

    PersonVO getPersonById(Integer id) throws Exception;

    void deletePersonByIds(List<Integer> ids);

    void exportPersonByCondition(PersonQueryVO personQueryVO, HttpServletResponse response) throws IOException, IllegalAccessException;

    void downloadTemplate(HttpServletResponse response) throws IOException;
}
