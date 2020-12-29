package com.dingkai.personManage.business.code.person.excel;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import com.alibaba.excel.annotation.write.style.ContentRowHeight;
import com.alibaba.excel.annotation.write.style.HeadRowHeight;
import com.dingkai.personManage.business.code.excel.LocalDateConverter;
import com.dingkai.personManage.business.common.annotation.DictionaryTransfer;
import lombok.Data;

import java.time.LocalDate;

/**
 * @Author dingkai
 * @Date 2020/8/2 23:37
 */
@Data
@ColumnWidth(25)
@HeadRowHeight(30)
@ContentRowHeight(25)
public class PersonImportModel {

    @ExcelProperty(value = "姓名", index = 0)
    private String name = "姓名（导入时删除该行）";

    @ExcelProperty(value = "身份证号", index = 1)
    private String idNumber = "420704199509045053";

    @ExcelProperty(value = "性别", index = 2)
    @DictionaryTransfer(value = "person_ethnicity")
    private String sex = "男";

    @ExcelProperty(value = "民族", index = 3)
    @DictionaryTransfer(value = "person_ethnicity")
    private String ethnicity = "汉族";//民族

    @ExcelProperty(value = "出生日期", index = 4, converter = LocalDateConverter.class)
    private LocalDate birthday = LocalDate.now();

    @ExcelProperty(value = "居住地址", index = 5)
    private String residentialAddress = "湖北省鄂州市汀祖镇";//居住地址

    @ExcelProperty(value = "户籍地址", index = 6)
    private String householdAddress = "湖北省武汉市江夏区";//户籍地址

}
