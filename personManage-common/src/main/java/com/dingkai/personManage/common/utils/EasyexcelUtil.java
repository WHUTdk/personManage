package com.dingkai.personManage.common.utils;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.write.handler.CellWriteHandler;
import com.alibaba.excel.write.handler.WriteHandler;
import com.alibaba.excel.write.merge.AbstractMergeStrategy;
import com.alibaba.excel.write.metadata.style.WriteCellStyle;
import com.alibaba.excel.write.metadata.style.WriteFont;
import com.alibaba.excel.write.style.HorizontalCellStyleStrategy;
import org.apache.poi.hssf.usermodel.HSSFDataFormat;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.VerticalAlignment;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;

/**
 * @Author dingkai
 * @Date 2020/7/18 17:42
 */
public class EasyexcelUtil {

    public static void setExcelResponse(HttpServletResponse response, String fileName) throws UnsupportedEncodingException {
        response.setContentType("application/vnd.ms-excel");
        response.setCharacterEncoding("utf-8");
        // 这里URLEncoder.encode可以防止中文乱码
        fileName = URLEncoder.encode(fileName, "UTF-8");
        response.setHeader("Content-disposition", "attachment;filename=" + fileName + ".xlsx");
    }

    public static <T> void write(HttpServletResponse response, List<T> list, Class<T> tClass,
                                 String fileName, CellWriteHandler cellWriteHandler) throws IOException {
        setExcelResponse(response, fileName);
        EasyExcel.write(response.getOutputStream(), tClass)
                .registerWriteHandler(cellWriteHandler)
                .registerWriteHandler(setDefaultCellStyle())
                .sheet("sheet1").doWrite(list);
    }

    public static <T> void write(HttpServletResponse response, List<T> list, Class<T> tClass,
                                 String fileName, String sheetName, CellWriteHandler cellWriteHandler) throws IOException {
        setExcelResponse(response, fileName);
        EasyExcel.write(response.getOutputStream(), tClass)
                .registerWriteHandler(cellWriteHandler)
                .registerWriteHandler(setDefaultCellStyle())
                .sheet(sheetName).doWrite(list);
    }

    public static <T> void write(HttpServletResponse response, List<T> list, Class<T> tClass, String fileName) throws IOException {
        setExcelResponse(response, fileName);
        EasyExcel.write(response.getOutputStream(), tClass)
                .registerWriteHandler(setDefaultCellStyle())
                .sheet("sheet1").doWrite(list);
    }

    public static <T> void write(HttpServletResponse response, List<T> list, Class<T> tClass, String fileName, String sheetName) throws IOException {
        setExcelResponse(response, fileName);
        EasyExcel.write(response.getOutputStream(), tClass)
                .registerWriteHandler(setDefaultCellStyle())
                .sheet(sheetName).doWrite(list);
    }

    private static HorizontalCellStyleStrategy setDefaultCellStyle() {
        // 头的策略
        WriteCellStyle headWriteCellStyle = new WriteCellStyle();
        // 背景设置为红色
        headWriteCellStyle.setFillForegroundColor(IndexedColors.RED.getIndex());
        WriteFont headWriteFont = new WriteFont();
        headWriteFont.setFontHeightInPoints((short) 12);
        headWriteCellStyle.setWriteFont(headWriteFont);
        // 内容的策略
        WriteCellStyle contentWriteCellStyle = new WriteCellStyle();
        HSSFDataFormat dataFormat = new HSSFWorkbook().createDataFormat();
        // 本文格式
        contentWriteCellStyle.setDataFormat(dataFormat.getFormat("@"));
        contentWriteCellStyle.setHorizontalAlignment(HorizontalAlignment.CENTER);
        contentWriteCellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        // 填充类型 这里需要指定 FillPatternType 为FillPatternType.SOLID_FOREGROUND 不然无法显示背景颜色.头默认了 FillPatternType所以可以不指定
        //contentWriteCellStyle.setFillPatternType(FillPatternType.SOLID_FOREGROUND);
        // 背景绿色
        //contentWriteCellStyle.setFillForegroundColor(IndexedColors.GREEN.getIndex());
        WriteFont contentWriteFont = new WriteFont();
        // 字体大小
        contentWriteFont.setFontHeightInPoints((short) 12);
        contentWriteCellStyle.setWriteFont(contentWriteFont);
        // 这个策略是 头是头的样式 内容是内容的样式 其他的策略可以自己实现
        return new HorizontalCellStyleStrategy(headWriteCellStyle, contentWriteCellStyle);
    }
}
