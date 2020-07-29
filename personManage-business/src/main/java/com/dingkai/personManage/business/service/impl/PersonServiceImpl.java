package com.dingkai.personManage.business.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.dingkai.personManage.business.dao.PersonMapper;
import com.dingkai.personManage.business.domain.PersonDO;
import com.dingkai.personManage.business.domain.VehicleDO;
import com.dingkai.personManage.business.excel.PersonMergeStrategy;
import com.dingkai.personManage.business.excel.PersonModel;
import com.dingkai.personManage.business.service.PersonService;
import com.dingkai.personManage.business.service.VehicleService;
import com.dingkai.personManage.business.utils.DictionaryUtil;
import com.dingkai.personManage.business.utils.EasyexcelUtil;
import com.dingkai.personManage.common.response.PagedResponseVO;
import com.dingkai.personManage.business.vo.PersonQueryVO;
import com.dingkai.personManage.business.vo.PersonVO;
import com.dingkai.personManage.business.vo.VehicleVO;
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
    public void savePerson(PersonVO personVO) {
        PersonDO personDO = new PersonDO();
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
    public PagedResponseVO<PersonVO> getPersonByCondition(PersonQueryVO personQueryVO) {
        Integer pageNo = personQueryVO.getPageNo();
        Integer pageSize = personQueryVO.getPageSize();
        //分页参数
        Page<PersonDO> page = new Page<>(pageNo, pageSize);
        QueryWrapper<PersonDO> queryWrapper = setQueryCondition(personQueryVO);
        IPage<PersonDO> iPage = personMapper.selectPage(page, queryWrapper);
        //属性赋值
        ArrayList<PersonVO> personVOS = new ArrayList<>();
        List<PersonDO> personDOS = iPage.getRecords();
        for (PersonDO personDO : personDOS) {
            PersonVO personVO = copyPersonDOToPersonVO(personDO);
            personVOS.add(personVO);
        }
        PagedResponseVO<PersonVO> responseVO = new PagedResponseVO<>();
        responseVO.setPageNo(pageNo);
        responseVO.setPageSize(pageSize);
        responseVO.setTotal(iPage.getTotal());
        responseVO.setTotalPages(iPage.getPages());
        responseVO.setList(personVOS);
        return responseVO;
    }

    /**
     * 设置查询条件
     */
    private QueryWrapper<PersonDO> setQueryCondition(PersonQueryVO personQueryVO) {
        //查询条件设置
        QueryWrapper<PersonDO> queryWrapper = new QueryWrapper<>();
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
    public PersonVO getPersonById(Integer id) throws Exception {
        PersonDO personDO = personMapper.selectById(id);
        if (personDO == null) {
            throw new Exception("根据id查询数据不存在");
        }
        return copyPersonDOToPersonVO(personDO);
    }

    /**
     * DO-VO属性赋值
     */
    private PersonVO copyPersonDOToPersonVO(PersonDO personDO) {
        PersonVO personVO = new PersonVO();
        BeanUtils.copyProperties(personDO, personVO);
        //根据Id查询名下车辆
        List<VehicleVO> vehicleVOS = vehicleService.getVehicleVOSByPersonId(personDO.getId());
        personVO.setVehicleVOS(vehicleVOS);
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
    public void exportPersonByCondition(PersonQueryVO personQueryVO, HttpServletResponse response) throws IOException, IllegalAccessException {
        ArrayList<PersonModel> personModels = new ArrayList<>();
        ArrayList<Integer> groupCount = new ArrayList<>();
        QueryWrapper<PersonDO> queryWrapper = setQueryCondition(personQueryVO);
        List<PersonDO> personDOS = personMapper.selectList(queryWrapper);
        for (PersonDO personDO : personDOS) {
            //根据人员名下车辆个数动态设置合并行数
            List<VehicleDO> vehicleDOS = vehicleService.getVehicleDOSByPersonId(personDO.getId());
            if (CollectionUtils.isEmpty(vehicleDOS)) {
                groupCount.add(1);
                PersonModel personModel = new PersonModel();
                BeanUtils.copyProperties(personDO, personModel);
                personModel.setSex(String.valueOf(personDO.getSex()));
                dictionaryUtil.codeToName(personModel);
                personModels.add(personModel);
            } else {
                //有多个车辆，需要合并行
                groupCount.add(vehicleDOS.size());
                for (VehicleDO vehicleDO : vehicleDOS) {
                    PersonModel personModel = new PersonModel();
                    BeanUtils.copyProperties(personDO, personModel);
                    BeanUtils.copyProperties(vehicleDO, personModel);
                    personModel.setSex(String.valueOf(personDO.getSex()));
                    personModel.setIsImport(String.valueOf(vehicleDO.getIsImport()));
                    dictionaryUtil.codeToName(personModel);
                    personModels.add(personModel);
                }
            }
        }
        //合并策略
        PersonMergeStrategy personMergeStrategy = new PersonMergeStrategy(groupCount);
        EasyexcelUtil.write(response, personModels, PersonModel.class, "person_info", personMergeStrategy);
    }


}
