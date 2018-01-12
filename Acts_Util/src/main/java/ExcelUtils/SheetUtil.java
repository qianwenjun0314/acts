package ExcelUtils; /**
 * Copyright (C), 2015-2017
 * FileName: ExcelUtils.SheetUtil
 * Author:   qianwenjun
 * Date:     2017/12/20 14:59
 * Description: excel的sheet处理工具类
 */

import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 * 〈excel的sheet处理工具类〉
 *
 * @author qianwenjun
 * @create 2017/12/20
 * @since 1.0.0
 */
public class SheetUtil {
    /**
     * 删除指定的sheet
     *
     * @param wb
     * @param sheetName
     * @return
     */
    public static void removeSheetByName(XSSFWorkbook wb, String sheetName) {

        try {
            wb.removeSheetAt(wb.getSheetIndex(sheetName));
        } catch(Exception e) {
            e.printStackTrace();
        }
    }
}