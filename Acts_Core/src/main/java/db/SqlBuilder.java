/**
 * Copyright (C), 2015-2018
 * FileName: SqlBuilder
 * Author:   qianwenjun
 * Date:     2018/1/13 14:10
 * Description:
 */
package db;

import org.apache.ibatis.jdbc.SQL;

import java.math.BigDecimal;
import java.util.Date;
import java.util.logging.Logger;

/**
 * 〈〉
 *
 * @author qianwenjun
 * @create 2018/1/13
 * @since 1.0.0
 */
public class SqlBuilder {

    transient static Logger logger = Logger.getLogger(SqlBuilder.class.getName());

    public static String insertSql(Schema schema,String tableName,String[] filedNames,Object[] values){
        if(filedNames == null || filedNames.length == 0 ||
                values == null || values.length == 0 ||
                filedNames.length != values.length) {
            logger.info("参数有误");
            return null;
        }
        return new SQL(){{
            INSERT_INTO(schema+"."+tableName);
            for (int i = 0; i <filedNames.length ; i++) {
                VALUES(filedNames[i], valueInstanceOf(values[i]));
            }}
        }.toString();
    }


    /**
     * 查询sql组装
     * @param schema 数据库
     * @param tableName 表
     * @param filedNames 字段名
     * @param values 字段值
     * @param orderBy 排序字段
     * @return
     */
    public static String selectSql(Schema schema,String tableName,String[] filedNames,Object[] values,String[] orderBy){
        if(filedNames == null || filedNames.length == 0 ||
                values == null || values.length == 0 ||
                filedNames.length != values.length) {
            logger.info("参数有误");
            return null;
        }

        return new SQL(){{
            SELECT("*");
            FROM(schema + "." + tableName);
            for (int i =0;i<filedNames.length;i++){
                WHERE(filedNames[i] + "=" + valueInstanceOf(values[i]));
            }
            if(orderBy!=null && orderBy.length>0) {
                for (int i =0;i<orderBy.length;i++) {
                    ORDER_BY(orderBy[i]);
                }
            }
        }}.toString();
    }

    /**
     * 更新sql组装
     * @param schema 数据库
     * @param tableName 表
     * @param filedNames1 字段名
     * @param values1 字段值
     * @param filedNames2 条件字段名
     * @param values2 条件字段值
     * @return
     */
    public static String updateSql(Schema schema,String tableName,String[] filedNames1,Object[] values1,String[] filedNames2,Object[] values2){
        if(filedNames1 == null || filedNames1.length == 0 ||
                values1 == null || values1.length == 0 ||
                filedNames2 == null || filedNames2.length == 0 ||
                values2 == null || values2.length == 0 ||
                filedNames1.length != values1.length ||
                filedNames2.length != values2.length
                ) {
            logger.info("参数有误");
            return null;
        }

        logger.info(new SQL(){{
            UPDATE(schema +"."+ tableName);
            for (int i =0;i<filedNames1.length;i++){
                SET(filedNames1[i] + "=" + valueInstanceOf(values1[i]));
            }
            for (int i =0;i<filedNames2.length;i++){
                WHERE(filedNames2[i] + "=" + valueInstanceOf(values2[i]));
            }
        }}.toString());

        return new SQL(){{
            UPDATE(schema +"."+ tableName);
            for (int i =0;i<filedNames1.length;i++){
                SET(filedNames1[i] + "=" + valueInstanceOf(values1[i]));
            }
            for (int i =0;i<filedNames2.length;i++){
                WHERE(filedNames2[i] + "=" + valueInstanceOf(values2[i]));
            }
        }}.toString();
    }

    /**
     * 删除sql组装
     * @param schema 数据库
     * @param tableName 表
     * @param filedNames 字段名
     * @param values 字段对应值
     * @return
     */
    public static String deleteSql(Schema schema,String tableName,String[] filedNames,Object[] values) {
        if(filedNames == null || filedNames.length == 0 ||
                values == null || values.length == 0 ||
                filedNames.length != values.length) {
            logger.info("参数有误");
            return null;
        }

        return new SQL(){{
            DELETE_FROM(schema +"."+ tableName);
            for (int i =0;i<filedNames.length;i++){
                WHERE(filedNames[i] + "=" + valueInstanceOf(values[i]));
            }
        }}.toString();
    }

    private static String valueInstanceOf(Object values){
        if(values instanceof String) {
            return "'" + values + "'";
        }
        if(values instanceof Integer) {
            return values + "";
        }
        if(values instanceof Double) {
            return values + "";
        }
        if(values instanceof Float) {
            return values + "";
        }
        if(values instanceof Long) {
            return values + "";
        }
        if(values instanceof Boolean) {
            return values + "";
        }
        if(values instanceof Date) {
            return "'" + values + "'";
        }
        if(values instanceof BigDecimal) {
            return values + "";
        }
        return null;
    }


    public static void main(String[] args) {
//        String sel = selectSql(Schema.morder,"car_order",new String[]{"order_id","order_desc"},
//                new Object[]{111,"测试"},new String[]{"orderid","id"});
//        System.out.println(sel);
//
//        String del = deleteSql(Schema.morder,"car_order",new String[]{"order_id","order_desc"},
//                new Object[]{111,"测试"});
//        System.out.println(del);
//
//        String update = updateSql(Schema.morder,"car_order",new String[]{"order_id","order_desc"},
//                new Object[]{111,"测试"},new String[]{"order_id1","order_desc1"},
//                new Object[]{1111,"测试1"});
//        System.out.println(update);

        String insertsql = insertSql(Schema.morder,"car_order",new String[]{"order_id","order_desc"},
                new Object[]{111,"测试"});
        System.out.println(insertsql);

    }
}