/**
 * Copyright (C), 2015-2018
 * FileName: DB
 * Author:   qianwenjun
 * Date:     2018/1/12 15:10
 * Description:
 */
package db;

import java.util.List;
import java.util.Map;

/**
 * 〈〉
 *
 * @author qianwenjun
 * @create 2018/1/12
 * @since 1.0.0
 */
public class DB {

    public SqlMapper sqlMapper;

    public DB(SqlMapper sqlMapper){
        this.sqlMapper = sqlMapper;
    }

    /**
     * 查询单个记录 不带排序
     * @param schema
     * @param tableName
     * @param filedNames
     * @param values
     * @return
     */
    public Map<String,Object> selectOne(Schema schema, String tableName, String[] filedNames, Object[] values){
        return sqlMapper.selectList(SqlBuilder.selectSql(schema, tableName, filedNames, values, null)).get(0);
    }

    /**
     * 查询单个记录 带排序
     * @param schema
     * @param tableName
     * @param filedNames
     * @param values
     * @param orderBy
     * @return
     */
    public Map<String,Object> selectOne(Schema schema,String tableName,String[] filedNames,Object[] values,String[] orderBy){
        return sqlMapper.selectList(SqlBuilder.selectSql(schema, tableName, filedNames, values, orderBy)).get(0);
    }

    /**
     * 查询一个集合
     * @param schema
     * @param tableName
     * @param filedNames
     * @param values
     * @param orderBy
     * @return
     */
    public List<Map<String,Object>> selectList(Schema schema, String tableName, String[] filedNames, Object[] values, String[] orderBy){
        return sqlMapper.selectList(SqlBuilder.selectSql(schema, tableName, filedNames, values, orderBy));
    }

    /**
     * 更新数据
     * @param schema
     * @param tableName
     * @param filedNames1
     * @param values1
     * @param filedNames2
     * @param values2
     * @return
     */
    public boolean update(Schema schema,String tableName,String[] filedNames1,Object[] values1,String[] filedNames2,Object[] values2) {
        int result = sqlMapper.update(SqlBuilder.updateSql(schema, tableName, filedNames1, values1, filedNames2, values2));
        return result > 0 ? true : false;
    }

    /**
     * 删除数据
     * @param schema
     * @param tableName
     * @param filedNames
     * @param values
     * @return
     */
    public boolean delete(Schema schema,String tableName,String[] filedNames,Object[] values) {
        int result = sqlMapper.delete( SqlBuilder.deleteSql(schema, tableName, filedNames, values));
        return result > 0 ? true : false;
    }

    public boolean insert(Schema schema,String tableName,String[] filedNames,Object[] values){
        int result = sqlMapper.insert(SqlBuilder.insertSql(schema, tableName, filedNames, values));
        return result > 0 ? true : false;
    }

}