package com.dingkai.personManage.business.excel.handler;

import com.alibaba.excel.metadata.CellData;
import com.alibaba.excel.metadata.Head;
import com.alibaba.excel.write.merge.AbstractMergeStrategy;
import com.alibaba.excel.write.metadata.holder.WriteSheetHolder;
import com.alibaba.excel.write.metadata.holder.WriteTableHolder;
import com.dingkai.personManage.business.utils.DictionaryUtil;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.util.CellRangeAddress;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author dingkai
 * @Date 2020/7/18 14:20
 */
public class PersonWriteHandler extends AbstractMergeStrategy {

    private List<Integer> mergeCount = new ArrayList<>();
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
        if (cell.getRowIndex() == 1) {
            int columnIndex = cell.getColumnIndex();
            //要合并列索引
            if (columnIndex >= 0 && columnIndex <= 6) {
                this.mergeGroupColumn(columnIndex);
            }
        }
    }

    public void afterCellDispose(WriteSheetHolder writeSheetHolder, WriteTableHolder writeTableHolder, List<CellData> cellDataList, Cell cell, Head head, Integer relativeRowIndex, Boolean isHead) {
        if (!isHead) {
            this.merge(writeSheetHolder.getSheet(), cell, head, relativeRowIndex);
        }
    }

}
