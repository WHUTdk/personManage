package com.dingkai.personManage.business.excel.handler;

import com.alibaba.excel.metadata.CellData;
import com.alibaba.excel.metadata.Head;
import com.alibaba.excel.write.merge.AbstractMergeStrategy;
import com.alibaba.excel.write.metadata.holder.WriteSheetHolder;
import com.alibaba.excel.write.metadata.holder.WriteTableHolder;
import com.dingkai.personManage.business.utils.DictionaryUtil;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.CellRangeAddressList;
import org.apache.poi.xssf.usermodel.XSSFDataValidation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author dingkai
 * @Date 2020/7/18 14:20
 */
public class PersonWriteHandler extends AbstractMergeStrategy {

    private List<Integer> mergeCount;
    private Sheet sheet;
    private DictionaryUtil dictionaryUtil;

    public PersonWriteHandler(List<Integer> mergeCount) {
        this.mergeCount = mergeCount;
    }

    public PersonWriteHandler(DictionaryUtil dictionaryUtil) {
        this.dictionaryUtil = dictionaryUtil;
    }

    private void mergeGroupColumn(Integer index) {
        //从内容的第一行开始
        int rowCnt = 1;
        for (Integer count : mergeCount) {
            //大于2才进行合并行
            if (count >= 2) {
                CellRangeAddress cellRangeAddress = new CellRangeAddress(rowCnt, rowCnt + count - 1, index, index);
                sheet.addMergedRegionUnsafe(cellRangeAddress);
            }
            rowCnt += count;
        }
    }

    @Override
    protected void merge(Sheet sheet, Cell cell, Head head, Integer integer) {
        this.sheet = sheet;
        int columnIndex = cell.getColumnIndex();
        //要合并列索引
        if (columnIndex >= 0 && columnIndex <= 6) {
            this.mergeGroupColumn(columnIndex);
        }

    }

    @Override
    public void afterCellDispose(WriteSheetHolder writeSheetHolder, WriteTableHolder writeTableHolder, List<CellData> cellDataList, Cell cell, Head head, Integer relativeRowIndex, Boolean isHead) {
        if (!isHead && cell.getRowIndex() == 1) {
            //未传入字典类时，视为导出数据，不执行下拉框策略，执行合并策略
            if (dictionaryUtil == null) {
                this.merge(writeSheetHolder.getSheet(), cell, head, relativeRowIndex);
            } else {
                String[] ethnicity = dictionaryUtil.getNameArrayByGroup("person_ethnicity");
                String[] sex = {"男", "女", "未知"};
                Map<Integer, String[]> mapDropDown = new HashMap<>();
                mapDropDown.put(2, sex);
                mapDropDown.put(3, ethnicity);
                Sheet sheet = writeSheetHolder.getSheet();
                ///开始设置下拉框
                DataValidationHelper helper = sheet.getDataValidationHelper();
                for (Map.Entry<Integer, String[]> entry : mapDropDown.entrySet()) {
                    //起始行、终止行、起始列、终止列
                    CellRangeAddressList addressList = new CellRangeAddressList(1, 100, entry.getKey(), entry.getKey());
                    //设置下拉框数据
                    DataValidationConstraint constraint = helper.createExplicitListConstraint(entry.getValue());
                    DataValidation dataValidation = helper.createValidation(constraint, addressList);
                    //处理Excel兼容性问题
                    if (dataValidation instanceof XSSFDataValidation) {
                        dataValidation.setSuppressDropDownArrow(true);
                        dataValidation.setShowErrorBox(true);
                    } else {
                        dataValidation.setSuppressDropDownArrow(false);
                    }
                    sheet.addValidationData(dataValidation);
                }
            }
        }
    }


}
