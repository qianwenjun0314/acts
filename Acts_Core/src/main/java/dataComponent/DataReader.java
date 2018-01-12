/**
 * Copyright (C), 2015-2017
 * FileName: DataReader
 * Author:   qianwenjun
 * Date:     2017/12/20 15:13
 * Description: 表格数据读取
 */
package dataComponent;


import ExcelUtils.LogUtil;
import org.apache.log4j.Logger;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * 〈表格数据读取〉
 *
 * @author qianwenjun
 * @create 2017/12/20
 * @since 1.0.0
 */
public class DataReader {



    protected static final Logger logger = Logger.getLogger(DataReader.class);

    private HashMap<String, ReaderRecordHandler> map = new HashMap<String, ReaderRecordHandler>();

    private Boolean byColumnName = false;
    private Boolean byRowKey = false;
    private List<String> headers = new ArrayList<String>();

    private Integer size = 0;

    //构造函数
    public DataReader() {
    }

    /**
     * @param sheet 获取表格的sheet
     * @param has_headers 是否有列名字
     * @param has_key_column 是否有数据
     * @param key_column 有行数据的，每一列的值
     * @return
     */
    public DataReader (XSSFSheet sheet, Boolean has_headers, Boolean has_key_column, Integer key_column) {

        //行
        XSSFRow myRow = null;
        HashMap<String, String> myList;
        size = 0;
        //TODO 不理解这个byColumnName的值具体含义
        this.byColumnName = has_headers;
        this.byRowKey = has_key_column;

        //如果有列名字，则获取每一行的数据
        //先获取第一行，在遍历取得列名称，给到headers（list）---列名称
        try {
            if (byColumnName) {
                myRow = sheet.getRow(0);
                for (Cell cell : myRow) {
                    headers.add(cell.getStringCellValue());
                }
                size = 1;
            }
            //循环取得第二行至末尾的数据,n行数据就会有n个myList
            for (; (myRow = sheet.getRow(size)) != null; size++) {
                myList = new HashMap<String, String>();

                if (byColumnName) {
                    //遍历每一行的每一个cell的数据，当每一行的末尾有空串的时候，要注意赋空串
                    for (int col = 0; col < headers.size(); col++) {
                        if (col < myRow.getLastCellNum()) {
                            myList.put(headers.get(col), getSheetCellValue(myRow.getCell(col)));
                        } else {
                            myList.put(headers.get(col), "");
                        }
                    }
                } else {
                    for (int col = 0; col < myRow.getLastCellNum(); col++) {
                        myList.put(Integer.toString(col), getSheetCellValue(myRow.getCell(col)));
                    }
                }

                //key_column是每行数据的一个唯一键值
                if (byRowKey) {
                    if (myList.size() == 2 && key_column == 0) {
                        map.put(getSheetCellValue(myRow.getCell(key_column)), new ReaderRecordHandler(myList.get(1)));
                    } else if (myList.size() == 2 && key_column == 1) {
                        map.put(getSheetCellValue(myRow.getCell(key_column)), new ReaderRecordHandler(myList.get(0)));
                    } else {
                        map.put(getSheetCellValue(myRow.getCell(key_column)), new ReaderRecordHandler(myList));
                    }
                } else {
                    map.put(Integer.toString(size), new ReaderRecordHandler(myList));
                }
            }


        } catch (Exception e) {
            LogUtil.printConsoleLog("Exception while loading data from Excel sheet:", e.getMessage());
        }
    }
    /**
     * 用来获取cell的具体值，获取前先将cell类型转换成String类型的
     *
     * @param cell
     * @param
     * @return
     */
    private String getSheetCellValue (XSSFCell cell) {
        String value = "";

        try {
            cell.setCellType(Cell.CELL_TYPE_STRING);
            value = cell.getStringCellValue();
        } catch (NullPointerException npe) {
            return "";
        }
        return value;
    }
    /**
     *
     * @return HashMap of key-value pairs representing the specified record.
     */
    public HashMap<String,ReaderRecordHandler> get_map() {
        return map;
    }

    public ReaderRecordHandler get_record(String record) {
        ReaderRecordHandler result = new ReaderRecordHandler();

        if(map.containsKey(record)) {
            result = map.get(record);
        }
        return result;
    }
}