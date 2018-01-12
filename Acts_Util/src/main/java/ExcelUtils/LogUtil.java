/**
 * Copyright (C), 2015-2017
 * FileName: LogUtil
 * Author:   qianwenjun
 * Date:     2017/12/20 15:27
 * Description:
 */
package ExcelUtils;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.helpers.MessageFormatter;

/**
 * 〈〉
 *
 * @author qianwenjun
 * @create 2017/12/20
 * @since 1.0.0
 */
public class LogUtil {

    private static final Logger LOGGER = LoggerFactory.getLogger(LogUtil.class);

    public static final String LOG_INFO_LEVEL = "INFO";
    public static final String LOG_DEBUG_LEVEL = "DEBUG";
    public static final String LOG_WARN_LEVEL = "WARN";
    public static final String LOG_ERROR_LEVEL = "ERROR";

    private static final String LOG_INTERVAL_TOKEN = "|";
    private static final String LOG_PLACE_TOKEN = "{}";

    /**
     * 按照level指定日志级别打印日志
     * @param key  自行指定该业务日志的关键识别信息，原则上不能为空，为空时不影响日志打印
     * @param level  设置日志级别，可选INFO、DEBUG、WARN、ERROR，如果level字段为空，默认打印info级别日志
     * @param args  可变参数列表，按需设置要打印的数据，可以是数值、对象等类型，也可以是异常堆栈
     */
    public static void printLog(String level,  String key, Object... args){
        String formattedLog = getFormattedLog( key, args);
        if(StringUtils.isBlank(level) || LOG_INFO_LEVEL.equalsIgnoreCase(level)){
            LOGGER.info(formattedLog);
        }else if(LOG_DEBUG_LEVEL.equalsIgnoreCase(level)){
            LOGGER.debug(formattedLog);
        }else if(LOG_WARN_LEVEL.equalsIgnoreCase(level)){
            LOGGER.warn(formattedLog);
        }else if(LOG_ERROR_LEVEL.equalsIgnoreCase(level)){
            LOGGER.error(formattedLog);
        }else{
            LOGGER.info(formattedLog);
        }
    }

    /**
     * 打印info级别日志
     * @param key  自行指定该业务日志的关键识别信息，原则上不能为空，为空时不影响日志打印
     * @param args  可变参数列表，按需设置要打印的数据，可以是数值、对象等类型，也可以是异常堆栈
     *  异常堆栈建议使用LOGGER直接打印
     */
    public static void printInfoLog( String key, Object... args){
        String formattedLog = getFormattedLog( key, args);
        LOGGER.info(formattedLog);
    }

    public static void printConsoleLog( Object... args){
        StringBuilder sb = new StringBuilder();
        for(int i=0; i<args.length; i++){
            sb.append(com.alibaba.fastjson.JSON.toJSONString(args[i]));
        }
        System.out.println(sb.toString());
    }

    public static void printDebugLog( String key, Object... args){
        String formattedLog = getFormattedLog( key, args);
        LOGGER.debug(formattedLog);
    }

    public static void printWarnLog( String key, Object... args){
        String formattedLog = getFormattedLog( key, args);
        LOGGER.warn(formattedLog);
    }

    public static void printErrorLog( String key, Object... args){
        String formattedLog = getFormattedLog( key, args);
        LOGGER.error(formattedLog);
    }

    private static String getFormattedLog( String key, Object... args){
        if(args==null){
            return "";
        }

        StringBuilder sb = new StringBuilder();
        sb.append(LOG_INTERVAL_TOKEN).append(LOG_PLACE_TOKEN).append(LOG_INTERVAL_TOKEN).append(LOG_PLACE_TOKEN);

        int argsSize = args.length;
        Object[] argArray = new Object[argsSize+2];
        argArray[0]=key;
        for(int i=0; i<argsSize; i++){
            sb.append(LOG_INTERVAL_TOKEN).append(LOG_PLACE_TOKEN);
            argArray[i+1]=args[i];
        }
        sb.append(LOG_INTERVAL_TOKEN);
        String messagePattern = sb.toString();

        String formattedLog = MessageFormatter.arrayFormat(messagePattern, argArray).getMessage();
        return formattedLog;
    }
}