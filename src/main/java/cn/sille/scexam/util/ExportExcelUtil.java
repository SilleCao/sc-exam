package cn.sille.scexam.util;

import cn.sille.scexam.util.excel.ExcelData;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.xssf.usermodel.*;
import org.apache.poi.xssf.usermodel.extensions.XSSFCellBorder;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletResponse;
import java.awt.Color;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author Sille_Cao
 * @date 2018/11/9 10:53
 */
@Component
public class ExportExcelUtil {

    /**
     * 导出Excel方法
     * @param response 返回数据
     * @param fileName 导出文件的名称
     * @param data
     * @throws Exception
     */
    public static void exportExcel(HttpServletResponse response, String fileName, ExcelData data) throws Exception {
        // 设置header
        response.setHeader("content-Type", "application/vnd.ms-excel;charset=utf-8");
        response.setCharacterEncoding("utf-8");
        // 下载文件的名称
        response.setHeader("Content-Disposition", "attachment;filename="+URLEncoder.encode(fileName, "utf-8"));
        exportExcel(data, response.getOutputStream());
    }

    /**
     * 设置Sheet 名称
     * @param data
     * @param out
     * @throws Exception
     */
    private static void exportExcel(ExcelData data, OutputStream out) throws Exception {

        XSSFWorkbook wb = new XSSFWorkbook();
        try {
            String sheetName = data.getSheetName();
            if (null == sheetName) {
                sheetName = "Sheet1";
            }
            XSSFSheet sheet = wb.createSheet(sheetName);
            writeExcel(wb, sheet, data);

            wb.write(out);
        } finally {

        }
    }

    /**
     * 写入表头和内容
     * @param wb
     * @param sheet
     * @param data
     */
    private static void writeExcel(XSSFWorkbook wb, Sheet sheet, ExcelData data) {

        int rowIndex = 0;
        //标题栏
        rowIndex = writeTitlesToExcel(wb, sheet, data.getTitles());
        String[] keys = data.getKeys().toArray(new String[]{});
        //Beanutil 类转换
        writeRowsToExcel(wb, sheet, BeanUtil.convertListMap(data.getRows(),true),keys, rowIndex);
        autoSizeColumns(sheet, data.getTitles().size() + 1);

    }

    /**
     * 写入表头
     * @param wb
     * @param sheet
     * @param titles
     * @return
     */
    private static int writeTitlesToExcel(XSSFWorkbook wb, Sheet sheet, List<String> titles) {
        int rowIndex = 0;
        int colIndex = 0;

        Font titleFont = wb.createFont();
        titleFont.setFontName("simsun");
        ((XSSFFont) titleFont).setBold(true);
        titleFont.setColor(IndexedColors.BLACK.index);

        XSSFCellStyle titleStyle = wb.createCellStyle();
        titleStyle.setAlignment(XSSFCellStyle.ALIGN_CENTER);
        titleStyle.setVerticalAlignment(XSSFCellStyle.VERTICAL_CENTER);
        titleStyle.setFillForegroundColor(new XSSFColor(new Color(182, 184, 192)));
        titleStyle.setFillPattern(XSSFCellStyle.SOLID_FOREGROUND);
        titleStyle.setFont(titleFont);
        setBorder(titleStyle, BorderStyle.THIN, new XSSFColor(new Color(0, 0, 0)));

        Row titleRow = sheet.createRow(rowIndex);
        colIndex = 0;

        for (String field : titles) {
            Cell cell = titleRow.createCell(colIndex);
            cell.setCellValue(field);
            cell.setCellStyle(titleStyle);
            colIndex++;
        }

        rowIndex++;
        return rowIndex;
    }

    /**
     * 写入内容
     * @param wb
     * @param sheet
     * @param rows
     * @param keys
     * @param rowIndex
     * @return
     */
    private static int writeRowsToExcel(XSSFWorkbook wb, Sheet sheet, List<Map<String, Object>> rows, String[] keys, int rowIndex) {
        int colIndex = 0;

        Font dataFont = wb.createFont();
        dataFont.setFontName("simsun");
        // dataFont.setFontHeightInPoints((short) 14);
        dataFont.setColor(IndexedColors.BLACK.index);

        XSSFCellStyle dataStyle = wb.createCellStyle();
        dataStyle.setAlignment(XSSFCellStyle.ALIGN_CENTER);
        dataStyle.setVerticalAlignment(XSSFCellStyle.VERTICAL_CENTER);
        dataStyle.setFont(dataFont);
        setBorder(dataStyle, BorderStyle.THIN, new XSSFColor(new Color(68, 171, 103)));

        for (Map<String, Object> rowData : rows) {
            Row dataRow = sheet.createRow(rowIndex);
            // dataRow.setHeightInPoints(25);
            colIndex = 0;
            for (int k = 0; k < keys.length; k++) {
                Object objValue = rowData.get(keys[k]);
                Cell cell = dataRow.createCell(colIndex);
                String value = "";
                if (objValue != null) {
                    //如果是时间，则把时间戳转换为字符串格式
                    if (keys[k].toLowerCase().contains("time") && objValue != null) {
                        try {
                            value = DateUtil.convertDateToString((Date)objValue, "yyyy-MM-dd");
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else {
                        value = objValue.toString();
                    }
                    cell.setCellValue(value);
                }else {
                    cell.setCellValue("");
                }
                colIndex++;
            }
            rowIndex++;
        }
        return rowIndex;
    }

    /**
     * 自动设置行宽
     * @param sheet
     * @param columnNumber
     */
    private static void autoSizeColumns(Sheet sheet, int columnNumber) {

        for (int i = 0; i < columnNumber; i++) {
            int orgWidth = sheet.getColumnWidth(i);
            sheet.autoSizeColumn(i, true);
            int newWidth = (int) (sheet.getColumnWidth(i) + 100);
            if (newWidth > orgWidth) {
                sheet.setColumnWidth(i, newWidth);
            } else {
                sheet.setColumnWidth(i, orgWidth);
            }
        }
    }

    /**
     * 设置格式
     * @param style
     * @param border
     * @param color
     */
    private static void setBorder(XSSFCellStyle style, BorderStyle border, XSSFColor color) {
        style.setBorderTop(border);
        style.setBorderLeft(border);
        style.setBorderRight(border);
        style.setBorderBottom(border);
        style.setBorderColor(XSSFCellBorder.BorderSide.TOP, color);
        style.setBorderColor(XSSFCellBorder.BorderSide.LEFT, color);
        style.setBorderColor(XSSFCellBorder.BorderSide.RIGHT, color);
        style.setBorderColor(XSSFCellBorder.BorderSide.BOTTOM, color);
    }
}

