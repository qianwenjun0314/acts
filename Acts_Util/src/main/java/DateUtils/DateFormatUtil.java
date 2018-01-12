/**
 * Copyright (C), 2015-2017
 * FileName: DateFormatUtil
 * Author:   qianwenjun
 * Date:     2017/12/20 15:59
 * Description:
 */
package DateUtils;

import org.apache.log4j.Logger;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * 〈〉
 *
 * @author qianwenjun
 * @create 2017/12/20
 * @since 1.0.0
 */
public class DateFormatUtil {


    private static final Logger logger = Logger.getLogger(DateFormatUtil.class);

    public String TIME_FORMAT_A = "yyyy-MM-dd HH:mm:ss";
    public static final String TIME_FORMAT_B = "yyyyMMddHHmmss";
    public static final String TIME_FORMAT_C = "yyyy-MM-dd HH:mm:ss:SSS";
    public static final String TIME_FORMAT_D = "yyyyMMdd";
    public static final String TIME_FORMAT_E = "yyyy年MM月dd日";
    public static final String TIME_FORMAT_F = "yyyyMMddHHmm";
    public static final String TIME_FORMAT_G = "yyyy年MM月dd日HH时mm分ss秒";
    public static final String TIME_FORMAT_H = "yyyy-MM-dd HH:mm";
    public static final String TIME_FORMAT_I = "HH:mm:ss";
    public static final String TIME_FORMAT_J = "yyyyMMddHHmmssSSS";
    public static final String TIME_FORMAT_K = "yy-M-d";
    public static final String TIME_FORMAT_L = "HH:mm";
    public static final String TIME_FORMAT_M = "MM月dd日 HH:mm";
    public static final String DATE_FORMAT = "yyyy-MM-dd";
    public static final String DATE_FORMAT_A = "yyyy/MM/dd";
    public static final String DATE_FORMAT_H = "yyyy-MM-dd HH";
    public static final String YEAR_FORMAT = "yyyy";
    public static final String YEAR_MONTH_FORMAT = "yyyy-MM";
    public static final String MONTH_DAY_FORMAT = "MM-dd";
    public static final String MONTH_DAY_HOUR_MINUTE_FORMAT = "MM-dd HH:mm";
    public static final String FORMAT_1 = ",##0.00";
    public static final String FORMAT_2 = "0.00";
    public static final String FORMAT_3 = ",###";
    public static final String FIRST_TIME = "1970-01-01 00:00:00";

    public  String dateTrans (Date date, String format) {

        String datetrans = null;
        try {
            if (date != null) {
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
                return simpleDateFormat.format(date);
            }
        } catch (Exception e) {
            logger.error("date trans failue，the detail information is : {}"+ e.getMessage());
        }
        return datetrans;
    }

    public static void main (String[] args) {

        DateFormatUtil dateFormatUtil = new DateFormatUtil();

        Date d = new Date();
        logger.info(d.toString());
        System.out.println("system.out test,date :"+d.toString());
        logger.info("logger test"+ d.toString());
        Calendar c = Calendar.getInstance();
        logger.info("c 的结果是：{}"+c.toString());
        Long time = System.currentTimeMillis();
        logger.info("time 的结果是：{}"+time.toString());

        String date = dateFormatUtil.dateTrans(d,dateFormatUtil.TIME_FORMAT_A);
        logger.info("date 的结果是：{}"+date);

    }

}