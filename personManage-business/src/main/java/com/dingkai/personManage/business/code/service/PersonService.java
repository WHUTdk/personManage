package com.dingkai.personManage.business.code.service;

import com.dingkai.personManage.business.code.vo.PersonQueryVo;
import com.dingkai.personManage.common.response.PagedResponseVO;
import com.dingkai.personManage.business.code.vo.PersonVo;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

public interface PersonService {

    void savePerson(PersonVo personVO);

    PagedResponseVO<PersonVo> getPersonByCondition(PersonQueryVo personQueryVO);

    PersonVo getPersonById(Integer id) throws Exception;

    void deletePersonByIds(List<Integer> ids);

    void exportPersonByCondition(PersonQueryVo personQueryVO, HttpServletResponse response) throws IOException, IllegalAccessException;

    void downloadTemplate(HttpServletResponse response) throws IOException;
}
