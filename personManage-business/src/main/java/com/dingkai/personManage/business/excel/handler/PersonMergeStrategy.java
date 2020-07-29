package com.dingkai.personManage.business.excel.handler;

import com.alibaba.excel.metadata.Head;
import com.alibaba.excel.write.merge.AbstractMergeStrategy;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.util.CellRangeAddress;

import java.util.List;

/**
 * @Author dingkai
 * @Date 2020/7/18 14:20
 */
public class PersonMergeStrategy extends AbstractMergeStrategy {

    private final List<Integer> groupCount;
    private Sheet sheet;

    public PersonMergeStrategy(List<Integer> groupCount) {
        this.groupCount = groupCount;
    }


    private void mergeGroupColumn(Integer index) {
        //从内容的第一行开始
        int rowCnt = 1;
        for (Integer count : groupCount) {
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
        if (cell.getRowIndex() == 1) {
            int columnIndex = cell.getColumnIndex();
            //要合并列索引
            if (columnIndex >= 0 && columnIndex <= 6) {
                this.mergeGroupColumn(columnIndex);
            }
        }
    }
}
