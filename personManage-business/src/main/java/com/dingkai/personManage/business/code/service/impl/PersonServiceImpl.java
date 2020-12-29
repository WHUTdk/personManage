package com.dingkai.personManage.business.code.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.dingkai.personManage.business.code.domain.VehicleDo;
import com.dingkai.personManage.business.code.service.PersonService;
import com.dingkai.personManage.business.code.service.VehicleService;
import com.dingkai.personManage.business.code.vo.PersonQueryVo;
import com.dingkai.personManage.business.code.vo.VehicleVo;
import com.dingkai.personManage.business.code.dao.PersonMapper;
import com.dingkai.personManage.business.code.domain.PersonDo;
import com.dingkai.personManage.business.code.excel.PersonImportModel;
import com.dingkai.personManage.business.code.excel.handler.PersonWriteHandler;
import com.dingkai.personManage.business.code.excel.PersonExportModel;
import com.dingkai.personManage.business.common.utils.DictionaryUtil;
import com.dingkai.personManage.business.common.utils.EasyexcelUtil;
import com.dingkai.personManage.common.response.PagedResponseVO;
import com.dingkai.personManage.business.code.vo.PersonVo;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class PersonServiceImpl implements PersonService {

    private static final Logger logger = LoggerFactory.getLogger(PersonServiceImpl.class);

    @Resource
    private PersonMapper personMapper;

    @Resource
    private VehicleService vehicleService;

    @Autowired
    private DictionaryUtil dictionaryUtil;


    /**
     * 保存人员信息
     */
    @Override
    public void savePerson(PersonVo personVO) {
        PersonDo personDO = new PersonDo();
        BeanUtils.copyProperties(personVO, personDO);
        if (personDO.getId() == null) {
            //新增
            personMapper.insert(personDO);
        } else {
            //更新
            personMapper.updateById(personDO);
        }
    }

    /**
     * 条件查询人员信息
     */
    @Override
    public PagedResponseVO<PersonVo> getPersonByCondition(PersonQueryVo personQueryVO) {
        Integer pageNo = personQueryVO.getPageNo();
        Integer pageSize = personQueryVO.getPageSize();
        //分页参数
        Page<PersonDo> page = new Page<>(pageNo, pageSize);
        QueryWrapper<PersonDo> queryWrapper = setQueryCondition(personQueryVO);
        IPage<PersonDo> iPage = personMapper.selectPage(page, queryWrapper);
        //属性赋值
        ArrayList<PersonVo> personVos = new ArrayList<>();
        List<PersonDo> personDos = iPage.getRecords();
        for (PersonDo personDO : personDos) {
            PersonVo personVO = copyPersonDOToPersonVO(personDO);
            personVos.add(personVO);
        }
        PagedResponseVO<PersonVo> responseVO = new PagedResponseVO<>();
        responseVO.setPageNo(pageNo);
        responseVO.setPageSize(pageSize);
        responseVO.setTotal(iPage.getTotal());
        responseVO.setTotalPages(iPage.getPages());
        responseVO.setList(personVos);
        return responseVO;
    }

    /**
     * 设置查询条件
     */
    private QueryWrapper<PersonDo> setQueryCondition(PersonQueryVo personQueryVO) {
        //查询条件设置
        QueryWrapper<PersonDo> queryWrapper = new QueryWrapper<>();
        String keyword = personQueryVO.getKeyword();
        if (StringUtils.isEmpty(keyword)) {
            if (StringUtils.isNotEmpty(personQueryVO.getName())) {
                queryWrapper.like("name", personQueryVO.getName());
            }
            if (StringUtils.isNotEmpty(personQueryVO.getIdNumber())) {
                queryWrapper.like("id_number", personQueryVO.getIdNumber());
            }
            if (personQueryVO.getSex() != null) {
                queryWrapper.eq("sex", personQueryVO.getSex());
            }
        } else {
            //关键字查询
            queryWrapper.and(wrapper -> wrapper.like("name", keyword).or()
                    .like("id_number", keyword).or()
                    .like("residential_address", keyword).or()
                    .like("household_address", keyword));
        }
        return queryWrapper;
    }

    /**
     * 根据id查询人员信息
     */
    @Override
    public PersonVo getPersonById(Integer id) throws Exception {
        PersonDo personDO = personMapper.selectById(id);
        if (personDO == null) {
            throw new Exception("根据id查询数据不存在");
        }
        return copyPersonDOToPersonVO(personDO);
    }

    /**
     * DO-VO属性赋值
     */
    private PersonVo copyPersonDOToPersonVO(PersonDo personDO) {
        PersonVo personVO = new PersonVo();
        BeanUtils.copyProperties(personDO, personVO);
        //根据Id查询名下车辆
        List<VehicleVo> vehicleVos = vehicleService.getVehicleVOSByPersonId(personDO.getId());
        personVO.setVehicleVos(vehicleVos);
        return personVO;
    }

    /**
     * 根据id集合删除人员信息
     */
    @Override
    public void deletePersonByIds(List<Integer> ids) {
        if (CollectionUtils.isNotEmpty(ids)) {
            //删除名下车辆
            vehicleService.deleteVehicleByPersonIds(ids);
            personMapper.deleteBatchIds(ids);
        }
    }

    /**
     * 根据查询条件导出人员信息excel
     */
    @Override
    public void exportPersonByCondition(PersonQueryVo personQueryVO, HttpServletResponse response) throws IOException, IllegalAccessException {
        ArrayList<PersonExportModel> personExportModels = new ArrayList<>();
        ArrayList<Integer> mergeCount = new ArrayList<>();
        QueryWrapper<PersonDo> queryWrapper = setQueryCondition(personQueryVO);
        List<PersonDo> personDos = personMapper.selectList(queryWrapper);
        for (PersonDo personDO : personDos) {
            //根据人员名下车辆个数动态设置合并行数
            List<VehicleDo> vehicleDos = vehicleService.getVehicleDOSByPersonId(personDO.getId());
            if (CollectionUtils.isEmpty(vehicleDos)) {
                mergeCount.add(1);
                PersonExportModel personExportModel = new PersonExportModel();
                BeanUtils.copyProperties(personDO, personExportModel);
                personExportModel.setSex(String.valueOf(personDO.getSex()));
                dictionaryUtil.codeToName(personExportModel);
                personExportModels.add(personExportModel);
            } else {
                //有多个车辆，需要合并行
                mergeCount.add(vehicleDos.size());
                for (VehicleDo vehicleDO : vehicleDos) {
                    PersonExportModel personExportModel = new PersonExportModel();
                    BeanUtils.copyProperties(personDO, personExportModel);
                    BeanUtils.copyProperties(vehicleDO, personExportModel);
                    personExportModel.setSex(String.valueOf(personDO.getSex()));
                    personExportModel.setIsImport(String.valueOf(vehicleDO.getIsImport()));
                    dictionaryUtil.codeToName(personExportModel);
                    personExportModels.add(personExportModel);
                }
            }
        }
        //合并策略
        PersonWriteHandler personWriteHandler = new PersonWriteHandler(mergeCount);
        EasyexcelUtil.write(response, personExportModels, PersonExportModel.class, "person_info", personWriteHandler);
    }

    @Override
    public void downloadTemplate(HttpServletResponse response) throws IOException {
        ArrayList<PersonImportModel> list = new ArrayList<>();
        PersonImportModel personImportModel = new PersonImportModel();
        list.add(personImportModel);
        EasyexcelUtil.write(response, list, PersonImportModel.class, "person_template", new PersonWriteHandler(dictionaryUtil));
    }


}
