package com.dingkai.personManage.business.service;

import com.dingkai.personManage.business.vo.PagedResponseVO;
import com.dingkai.personManage.business.vo.PersonQueryVO;
import com.dingkai.personManage.business.vo.PersonVO;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

public interface PersonService {

    void savePerson(PersonVO personVO);

    PagedResponseVO<PersonVO> getPersonByCondition(PersonQueryVO personQueryVO);

    PersonVO getPersonById(Integer id);

    void deletePersonByIds(List<Integer> ids);

    void exportPersonByCondition(PersonQueryVO personQueryVO, HttpServletResponse response) throws IOException, IllegalAccessException;
}
