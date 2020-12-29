package com.dingkai.personManage.business.code.excel.handler;

import com.alibaba.excel.metadata.CellData;
import com.alibaba.excel.metadata.Head;
import com.alibaba.excel.write.handler.CellWriteHandler;
import com.alibaba.excel.write.metadata.holder.WriteSheetHolder;
import com.alibaba.excel.write.metadata.holder.WriteTableHolder;
import com.dingkai.personManage.business.common.utils.DictionaryUtil;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.CellRangeAddressList;
import org.apache.poi.xssf.usermodel.XSSFDataValidation;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author dingkai
 * @Date 2020/7/18 14:20
 */
public class PersonWriteHandler implements CellWriteHandler {

    private List<Integer> mergeCount;
    private Sheet sheet;
    private DictionaryUtil dictionaryUtil;

    public PersonWriteHandler(List<Integer> mergeCount) {
        this.mergeCount = mergeCount;
    }

    public PersonWriteHandler(DictionaryUtil dictionaryUtil) {
        this.dictionaryUtil = dictionaryUtil;
    }

    private void mergeGroupColumn(int index) {
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

    protected void merge(Cell cell) {
        int columnIndex = cell.getColumnIndex();
        //要合并列索引
        if (columnIndex >= 0 && columnIndex <= 6) {
            this.mergeGroupColumn(columnIndex);
        }
    }

    @Override
    public void beforeCellCreate(WriteSheetHolder writeSheetHolder, WriteTableHolder writeTableHolder, Row row, Head head, Integer integer, Integer integer1, Boolean aBoolean) {

    }

    @Override
    public void afterCellCreate(WriteSheetHolder writeSheetHolder, WriteTableHolder writeTableHolder, Cell cell, Head head, Integer integer, Boolean aBoolean) {

    }

    @Override
    public void afterCellDataConverted(WriteSheetHolder writeSheetHolder, WriteTableHolder writeTableHolder, CellData cellData, Cell cell, Head head, Integer integer, Boolean aBoolean) {

    }

    @Override
    public void afterCellDispose(WriteSheetHolder writeSheetHolder, WriteTableHolder writeTableHolder, List<CellData> cellDataList, Cell cell, Head head, Integer relativeRowIndex, Boolean isHead) {
        this.sheet = writeSheetHolder.getSheet();
        if (!isHead && cell.getRowIndex() == 1 ) {
            //未传入字典类时，视为导出数据，不执行下拉框策略，执行合并策略
            if (dictionaryUtil == null) {
                this.merge(cell);
            } else {
                if(cell.getColumnIndex() == 1){
                    String[] sex = {"男", "女", "未知"};
                    Map<Integer, String[]> mapDropDown = new HashMap<>();
                    mapDropDown.put(2, sex);
                    ///开始设置下拉框
                    DataValidationHelper helper = sheet.getDataValidationHelper();
                    for (Map.Entry<Integer, String[]> entry : mapDropDown.entrySet()) {
                        //起始行、终止行、起始列、终止列
                        CellRangeAddressList addressList = new CellRangeAddressList(1, 100, entry.getKey(), entry.getKey());
                        //设置下拉框数据
                        DataValidationConstraint constraint = helper.createExplicitListConstraint(entry.getValue());
                        setDataValidation(helper, constraint, addressList);
                    }
                    //针对大数据量下拉框，需要引用sheet的方式设置
                    String[] ethnicity = dictionaryUtil.getNameArrayByGroup("person_ethnicity");
                    setDataValidationForHiddenSheet(helper, ethnicity);
                }
            }
        }
    }

    private void setDataValidationForHiddenSheet(DataValidationHelper helper, String[] values) {
        int[] columnIndexs = {3};
        //创建新sheet，存放数据
        Workbook workbook = sheet.getWorkbook();
        String hiddenName = "hidden";
        Sheet hidden = workbook.createSheet(hiddenName);
        for (int i = 0; i < values.length; i++) {
            //遍历集合存放数据
            hidden.createRow(i).createCell(0).setCellValue(values[i]);
        }
        //创建名称，可以被其他单元格引用
        Name workbookName = workbook.createName();
        workbookName.setNameName(hiddenName);
        workbookName.setRefersToFormula(hiddenName + "!$A$1:$A$" + values.length);
        for (int columnIndex : columnIndexs) {
            //设置下拉框
            DataValidationConstraint constraint = helper.createFormulaListConstraint(hiddenName);
            CellRangeAddressList addressList = new CellRangeAddressList(1, 100, columnIndex, columnIndex);
            setDataValidation(helper, constraint, addressList);
            //隐藏创建的国籍sheet
            workbook.setSheetHidden(1, true);
        }
    }

    private void setDataValidation(DataValidationHelper helper, DataValidationConstraint constraint, CellRangeAddressList addressList) {
        DataValidation dataValidation = helper.createValidation(constraint, addressList);
        //处理兼容性
        if (dataValidation instanceof XSSFDataValidation) {
            dataValidation.setSuppressDropDownArrow(true);
            dataValidation.setShowErrorBox(true);
        } else {
            dataValidation.setSuppressDropDownArrow(false);
        }
        sheet.addValidationData(dataValidation);
    }

}
