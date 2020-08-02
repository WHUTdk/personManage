package com.dingkai.personManage.business.excel;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import com.alibaba.excel.annotation.write.style.ContentRowHeight;
import com.alibaba.excel.annotation.write.style.HeadRowHeight;
import com.dingkai.personManage.business.annotation.DictionaryTransfer;
import lombok.Data;

import java.time.LocalDate;

/**
 * @Author dingkai
 * @Date 2020/7/18 13:43
 */
@Data
@ColumnWidth(25)
@HeadRowHeight(30)
@ContentRowHeight(25)
public class PersonExportModel {

    @ExcelProperty(value = "姓名", index = 0)
    private String name;

    @ExcelProperty(value = "身份证号", index = 1)
    private String idNumber;

    @ExcelProperty(value = "性别", index = 2)
    @DictionaryTransfer(value = "person_sex")
    private String sex;

    @ExcelProperty(value = "民族", index = 3)
    @DictionaryTransfer(value = "person_ethnicity")
    private String ethnicity;//民族

    @ExcelProperty(value = "出生日期", index = 4, converter = LocalDateConverter.class)
    private LocalDate birthday;

    @ExcelProperty(value = "居住地址", index = 5)
    private String residentialAddress;//居住地址

    @ExcelProperty(value = "户籍地址", index = 6)
    private String householdAddress;//户籍地址

    @ExcelProperty(value = "车牌号", index = 7)
    private String plateNo;

    @ExcelProperty(value = "车辆品牌", index = 8)
    private String brand;

    @DictionaryTransfer(value = "vehicle_type")
    @ExcelProperty(value = "车辆类型", index = 9)
    private String type;

    @ExcelProperty(value = "是否进口", index = 10)
    @DictionaryTransfer(value = "vehicle_isImport")
    private String isImport;//是否进口，0否；1是
}
