package com.dingkai.personManage.business.code.person.controller;

import com.dingkai.personManage.business.code.person.service.PersonService;
import com.dingkai.personManage.business.code.person.vo.req.SavePersonReqVo;
import com.dingkai.personManage.business.code.person.vo.req.SelPersonReqVo;
import com.dingkai.personManage.business.code.person.vo.resp.SelPersonRespVo;
import com.dingkai.personManage.business.common.annotation.OperateLog;
import com.dingkai.personManage.business.common.annotation.SubmitLock;
import com.dingkai.personManage.common.response.PagedResponseVO;
import com.dingkai.personManage.common.response.BaseResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

@Api(tags = "人员信息管理")
@RestController
@RequestMapping("/person")
public class PersonController {

    private static final Logger logger = LoggerFactory.getLogger(PersonController.class);

    @Autowired
    private PersonService personService;

    @ApiOperation(value = "保存人员信息")
    @OperateLog
    @SubmitLock
    @PostMapping("/savePerson")
    public BaseResult savePerson(@RequestBody SavePersonReqVo savePersonReqVo) {
        try {
            personService.savePerson(savePersonReqVo);
            return BaseResult.success();
        } catch (Exception e) {
            logger.error("保存人员信息出错", e);
            return BaseResult.error("-1", e.getMessage());
        }
    }

    @ApiOperation(value = "条件查询人员信息")
    @OperateLog
    @PostMapping("/getPersonByCondition")
    public BaseResult getPersonByCondition(@RequestBody @Validated SelPersonReqVo selPersonReqVO) {
        try {
            PagedResponseVO<SelPersonRespVo> personByCondition = personService.getPersonByCondition(selPersonReqVO);
            return BaseResult.success(personByCondition);
        } catch (Exception e) {
            logger.error("条件查询人员信息出错", e);
            return BaseResult.error("-1", e.getMessage());
        }
    }

    @ApiOperation(value = "根据id获取人员信息")
    @OperateLog
    @GetMapping("/getPersonById")
    public BaseResult getPersonById(@RequestParam("id") Integer id) {
        try {
            SelPersonRespVo personById = personService.getPersonById(id);
            return BaseResult.success(personById);
        } catch (Exception e) {
            logger.error("根据id查询人员信息出错", e);
            return BaseResult.error("-1", e.getMessage());
        }
    }

    @ApiOperation(value = "根据id集合删除人员信息")
    @OperateLog
    @PostMapping("/deletePersonByIds")
    public BaseResult deletePersonByIds(@RequestBody List<Integer> ids) {
        try {
            personService.deletePersonByIds(ids);
            return BaseResult.success();
        } catch (Exception e) {
            logger.error("根据id删除人员信息出错", e);
            return BaseResult.error("-1", e.getMessage());
        }
    }

    @ApiOperation(value = "条件导出人员信息")
    @OperateLog
    @PostMapping("/exportPersonByCondition")
    public void exportPersonByCondition(@RequestBody SelPersonReqVo selPersonReqVO, HttpServletResponse response) {
        try {
            personService.exportPersonByCondition(selPersonReqVO, response);
        } catch (Exception e) {
            logger.error("条件导出人员信息出错", e);
        }
    }

    @ApiOperation(value = "下载人员导入模板")
    @OperateLog
    @GetMapping("/downloadTemplate")
    public void downloadTemplate(HttpServletResponse response) {
        try {
            personService.downloadTemplate(response);
        } catch (Exception e) {
            logger.error("下载人员导入模板出错", e);
        }
    }


}
