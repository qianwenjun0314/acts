/**
 * Copyright (C), 2015-2018
 * FileName: CsvDataUtil
 * Author:   qianwenjun
 * Date:     2018/1/12 15:19
 * Description:
 */
package utils;

import org.apache.log4j.Logger;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

/**
 * 〈〉
 *
 * @author qianwenjun
 * @create 2018/1/12
 * @since 1.0.0
 */
public class CsvDataUtil {

    private static Logger logger = Logger.getLogger(CsvDataUtil.class);

    public static Object[][] getData(String fileName) throws Exception {

        logger.info("fileName==="+fileName);
        Object[][] data = null;
        //String path = ClassLoader.getSystemResource(fileName).getPath();

        String path = CsvDataUtil.class.getResource("/").getPath();
        logger.info("path==="+path);

        File csvFile = new File(path+fileName);
        if(!csvFile.exists()) {
            logger.info("文件不存在："+csvFile.getAbsolutePath());
        }
        BufferedReader br = new BufferedReader(new FileReader(csvFile));
        List csvList = new ArrayList<String>();
        while (br.ready()) {
            csvList.add(br.readLine());
        }

        //用例行数，去掉一行标题
        int rowNum = csvList.size() - 1;
        //用例列数
        int columnNum = csvList.get(0).toString().split(",").length;
        data = new Object[rowNum][columnNum];

        int startRow = 1;
        int startCol = 0;
        //控制二维数组的下标
        int ci, cj;
        ci = 0;

        for (int i = startRow; i <= rowNum; i++, ci++) {
            cj = 0;
            String tmpStr = csvList.get(i).toString();
            Object[] tmpData = tmpStr.split(",");
            for (int j = startCol; j <= columnNum-1; j++, cj++) {
                data[ci][cj] = tmpData[j];
            }

        }
        return data;
    }
}