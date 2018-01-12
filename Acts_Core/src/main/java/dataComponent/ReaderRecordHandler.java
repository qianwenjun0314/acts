/**
 * Copyright (C), 2015-2017
 * FileName: ReaderRecordHandler
 * Author:   qianwenjun
 * Date:     2017/12/20 15:23
 * Description: data handle
 */
package dataComponent;

import ExcelUtils.LogUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * 〈data handle〉
 *
 * @author qianwenjun
 * @create 2017/12/20
 * @since 1.0.0
 */
public class ReaderRecordHandler {

    private enum RecordType {
        VALUE, NAMED_MAP, INDEXED_LIST
    }

    private String single_value = "";
    private HashMap<String, String> named_value_map = new HashMap<String, String>();
    private List<String> indexed_value_list = new ArrayList<String>();
    private RecordType myType;

    //利用构造器创建这个record对象的时候，需要将myType附上对应的值
    public ReaderRecordHandler() {
        this("");
    }

    public ReaderRecordHandler(String value) {
        this.myType = RecordType.VALUE;
        this.single_value = value;
    }

    public ReaderRecordHandler(HashMap<String,String> map) {
        this.myType = RecordType.NAMED_MAP;
        this.named_value_map = map;
    }

    public ReaderRecordHandler(List<String> list) {
        this.myType = RecordType.INDEXED_LIST;
        this.indexed_value_list = list;
    }

    public HashMap<String,String> get_map() {
        return named_value_map;
    }

    //计算列对应的value数据大小
    public int size() {
        int result = 0;
        if(myType.equals(RecordType.VALUE)) {
            result = 1;
        } else if(myType.equals(RecordType.NAMED_MAP)) {
            result = named_value_map.size();
        } else if(myType.equals(RecordType.INDEXED_LIST)) {
            result = indexed_value_list.size();
        }
        return result;
    }

    //获取value，map和list三种对象的数据
    public String get() {
        String result = "";
        if(myType.equals(RecordType.VALUE)) {
            result = single_value;
        } else {
            LogUtil.printConsoleLog("Called get() on wrong type:" , myType.toString());
        }
        return result;
    }

    public String get(String key) {
        String result = "";
        if(myType.equals(RecordType.NAMED_MAP)) {
            result = named_value_map.get(key);
        }
        return result;
    }

    public String get(Integer index) {
        String result = "";
        if(myType.equals(RecordType.INDEXED_LIST)) {
            result = indexed_value_list.get(index);
        }
        return result;
    }

    //存放value，map和list三种类型数据到recordHandle
    public Boolean set(String value) {
        Boolean result = false;
        if(myType.equals(RecordType.VALUE)) {
            this.single_value = value;
            result = true;
        } else if(myType.equals(RecordType.INDEXED_LIST)) {
            this.indexed_value_list.add(value);
            result = true;
        }
        return result;
    }

    public Boolean set(String key,String value) {
        Boolean result = false;
        if(myType.equals(RecordType.NAMED_MAP)) {
            this.named_value_map.put(key,value);
            result = true;
        }
        return result;
    }

    public Boolean set(Integer index, String value) {
        Boolean result = false;
        if(myType.equals(RecordType.INDEXED_LIST)) {
            if(this.indexed_value_list.size() > index) {
                this.indexed_value_list.set(index, value);
                result = true;
            }
        }
        return result;
    }

    //判断是否有值(value类型，比较相等；list类型，比较包含这个值；map类型，比较是否包含这个key)
    public Boolean has(String value) {
        Boolean result = false;
        if(myType.equals(RecordType.VALUE) && this.single_value.equals(value)) {
            result =  true;
        } else if(myType.equals(RecordType.NAMED_MAP) && this.named_value_map.containsKey(value)) {
            result = true;
        } else if(myType.equals(RecordType.INDEXED_LIST) && this.indexed_value_list.contains(value)) {
            result = true;
        }
        return result;
    }

    //移除表格数据
    public Boolean remove(String value) {
        Boolean result = false;
        if(myType.equals(RecordType.VALUE) && this.single_value.equals(value)) {
            this.single_value = "";
            result = true;
        } else if(myType.equals(RecordType.NAMED_MAP) && this.named_value_map.containsKey(value)) {
            this.named_value_map.remove(value);
            result = true;
        } else if(myType.equals(RecordType.INDEXED_LIST) && this.indexed_value_list.contains(value)) {
            this.indexed_value_list.remove(value);
            result = true;
        }
        return result;
    }

    public Boolean remove(Integer index) {
        Boolean result = false;
        if(myType.equals(RecordType.INDEXED_LIST) && this.indexed_value_list.contains(index)) {
            this.indexed_value_list.remove(index);
            result = true;
        }
        return result;
    }
}