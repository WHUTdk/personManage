package com.dingkai.personManage.business.controller;

import com.alibaba.excel.write.metadata.style.WriteCellStyle;
import com.alibaba.excel.write.metadata.style.WriteFont;
import com.alibaba.excel.write.style.HorizontalCellStyleStrategy;
import com.dingkai.personManage.business.annotation.OperateLog;
import com.dingkai.personManage.business.service.PersonService;
import com.dingkai.personManage.business.vo.PagedResponseVO;
import com.dingkai.personManage.business.vo.PersonQueryVO;
import com.dingkai.personManage.business.vo.PersonVO;
import com.dingkai.personManage.common.response.BaseResult;
import org.apache.poi.hssf.usermodel.HSSFDataFormat;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/person")
public class PersonController {

    private static final Logger logger = LoggerFactory.getLogger(PersonController.class);

    @Autowired
    private PersonService personService;

    @OperateLog
    @PostMapping("/savePerson")
    public BaseResult savePerson(@RequestBody PersonVO personVO) {
        try {
            personService.savePerson(personVO);
            return BaseResult.success();
        } catch (Exception e) {
            logger.error("保存人员信息出错，错误信息：{}", e.getMessage());
            return BaseResult.error("-1", e.getMessage());
        }
    }

    @OperateLog
    @PostMapping("/getPersonByCondition")
    public BaseResult getPersonByCondition(@RequestBody PersonQueryVO personQueryVO) {
        try {
            PagedResponseVO<PersonVO> personByCondition = personService.getPersonByCondition(personQueryVO);
            return BaseResult.success(personByCondition);
        } catch (Exception e) {
            logger.error("条件查询人员信息出错，错误信息：{}", e.getMessage());
            return BaseResult.error("-1", e.getMessage());
        }
    }

    @OperateLog
    @GetMapping("/getPersonById")
    public BaseResult getPersonById(@RequestParam("id") Integer id) {
        try {
            PersonVO personById = personService.getPersonById(id);
            return BaseResult.success(personById);
        } catch (Exception e) {
            logger.error("根据id查询人员信息出错，错误信息：{}", e.getMessage());
            return BaseResult.error("-1", e.getMessage());
        }
    }

    @OperateLog
    @PostMapping("/deletePersonByIds")
    public BaseResult deletePersonByIds(@RequestBody Map<String, List<Integer>> map) {
        try {
            List<Integer> ids = map.get("ids");
            personService.deletePersonByIds(ids);
            return BaseResult.success();
        } catch (Exception e) {
            logger.error("根据id删除人员信息出错，错误信息：{}", e.getMessage());
            return BaseResult.error("-1", e.getMessage());
        }
    }

    @OperateLog
    @PostMapping("/exportPersonByCondition")
    public void exportPersonByCondition(@RequestBody PersonQueryVO personQueryVO, HttpServletResponse response) {
        try {
            personService.exportPersonByCondition(personQueryVO, response);
        } catch (Exception e) {
            logger.error("条件导出人员信息出错，错误信息：{}", e.getMessage());
        }
    }

    @OperateLog
    @GetMapping("/exportPerson")
    public void exportPerson(Integer sex,
                                   String IdNumber,
                                   HttpServletResponse response) {
        try {
            PersonQueryVO personQueryVO = new PersonQueryVO();
            personQueryVO.setSex(sex);
            personQueryVO.setIdNumber(IdNumber);
            personService.exportPersonByCondition(personQueryVO, response);
        } catch (Exception e) {
            logger.error("条件导出人员信息出错，错误信息：{}", e.getMessage());
        }
    }

}
