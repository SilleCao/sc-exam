package cn.sille.scexam.util.excel;

import java.io.Serializable;
import java.util.List;

/**
 * @author Sille_Cao
 * @date 2018/11/9 10:52
 */

public class ExcelData implements Serializable {

    // 表头导航列名称
    private List<String> titles;

    //对应数据的列字段名称
    private List<String> keys;

    // 数据
    private List<?> rows;

    // 页签名称
    private String sheetName;

    public ExcelData() {
    }

    public ExcelData(List<String> titles, List<String> keys, List<?> rows, String sheetName) {
        this.titles = titles;
        this.keys = keys;
        this.rows = rows;
        this.sheetName = sheetName;
    }

    public List<String> getTitles() {
        return titles;
    }

    public void setTitles(List<String> titles) {
        this.titles = titles;
    }

    public List<String> getKeys() {
        return keys;
    }

    public void setKeys(List<String> keys) {
        this.keys = keys;
    }

    public List<?> getRows() {
        return rows;
    }

    public void setRows(List<?> rows) {
        this.rows = rows;
    }

    public String getSheetName() {
        return sheetName;
    }

    public void setSheetName(String sheetName) {
        this.sheetName = sheetName;
    }
}