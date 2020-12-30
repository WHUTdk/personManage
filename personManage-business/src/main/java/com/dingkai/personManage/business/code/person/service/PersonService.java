package com.dingkai.personManage.business.code.person.service;

import com.dingkai.personManage.business.code.person.vo.req.SavePersonReqVo;
import com.dingkai.personManage.business.code.person.vo.req.SelPersonReqVo;
import com.dingkai.personManage.business.code.person.vo.resp.SelPersonRespVo;
import com.dingkai.personManage.common.response.PagedResponseVO;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

public interface PersonService {

    void savePerson(SavePersonReqVo personVO);

    PagedResponseVO<SelPersonRespVo> getPersonByCondition(SelPersonReqVo selPersonReqVO);

    SelPersonRespVo getPersonById(Integer id) throws Exception;

    void deletePersonByIds(List<Integer> ids);

    void exportPersonByCondition(SelPersonReqVo selPersonReqVO, HttpServletResponse response) throws IOException, IllegalAccessException;

    void downloadTemplate(HttpServletResponse response) throws IOException;
}
