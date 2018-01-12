/**
 * Copyright (C), 2015-2017
 * FileName: DataWriter
 * Author:   qianwenjun
 * Date:     2017/12/20 15:22
 * Description: excel数据写入
 */
package dataComponent;

import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 〈excel数据写入〉
 *
 * @author qianwenjun
 * @create 2017/12/20
 * @since 1.0.0
 */
public class DataWriter {

    private static WriterRecordHandle writerRecordHandle;
    private static Integer outputSize = 1;
    private static Integer resultSize = 1;
    private static Integer comparsion = 1;

    public static void writeOutputData(XSSFSheet outputSheet, String headeType, Map<String,String> outputRecord) {
        XSSFRow firstRow = outputSheet.getRow(0);
        if(firstRow != null) {
            XSSFRow myRow = outputSheet.createRow(outputSize);
            List<String> cellValues = new ArrayList<String>();
            for(int i = 0; i < firstRow.getLastCellNum(); i++) {
                cellValues.add(outputRecord.get(firstRow.getCell(i).toString()));
            }
            setSheetCellValue(myRow,cellValues);
            outputSize++;
        } else {
            setHeadersValue(outputSheet,headeType);
        }
    }

    public static  void writeResultData(XSSFSheet resultSheet, String headeType, Map<String,String> resultRecord, int i) {
        XSSFRow firstRow = resultSheet.getRow(0);
        if(i == 0) {
            if(firstRow != null) {
                XSSFRow myRow = resultSheet.createRow(resultSize);
                List<String> cellValues = new ArrayList<String>();
                for(int j = 0; j < firstRow.getLastCellNum(); j++) {
                    cellValues.add(resultRecord.get(firstRow.getCell(j).toString()));
                }
                setSheetCellValue(myRow,cellValues);
                resultSize++;
            }else {
                setHeadersValue(resultSheet,headeType);
            }
        } else if(i == 1) {
            if(firstRow != null) {
                XSSFRow myRow = resultSheet.createRow(resultSize);
                List<String> cellValues = new ArrayList<String>();
                for(int j = 0; j < firstRow.getLastCellNum(); j++) {
                    cellValues.add(resultRecord.get(firstRow.getCell(j).toString()));
                }
                setSheetCellValue(myRow,cellValues);
                resultSize++;
            }else {
                setHeadersValue(resultSheet,headeType);
            }
        }
    }

    public static void writeComparsionData(XSSFSheet comparsionSheet, String headeType, Map<String,String> comparsionRecord) {
        XSSFRow firstRow = comparsionSheet.getRow(0);
        if(firstRow != null) {
            XSSFRow myRow = comparsionSheet.createRow(comparsion);
            List<String> cellValues = new ArrayList<String>();
            for(int i = 0; i < firstRow.getLastCellNum(); i++) {
                cellValues.add(comparsionRecord.get(firstRow.getCell(i).toString()));
            }
            setSheetCellValue(myRow,cellValues);
            comparsion++;
        } else {
            setHeadersValue(comparsionSheet,headeType);
        }
    }

    public static void setHeadersValue(XSSFSheet sheet, String headType) {
        XSSFRow firstRow = sheet.getRow(0);
        if(firstRow != null) {
            return ;
        } else {
            firstRow = sheet.createRow(0);
            writerRecordHandle = new WriterRecordHandle(headType);
            setSheetCellValue(firstRow, writerRecordHandle.headers);
        }
    }

    public static void setSheetCellValue(XSSFRow row, List<String> values) {
        for(int col = 0; col < values.size(); col ++) {
            row.createCell((short) col).setCellValue(values.get(col));
        }
    }


}