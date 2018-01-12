/**
 * Copyright (C), 2015-2018
 * FileName: CollectionUtil
 * Author:   qianwenjun
 * Date:     2018/1/12 15:49
 * Description:
 */
package utils;

import com.google.gson.internal.LinkedTreeMap;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

/**
 * 〈对复杂集合进行排序处理 【ps：主要用于处理：Json字符串响应报文转换后得到的Map】〉
 *
 * @author qianwenjun
 * @create 2018/1/12
 * @since 1.0.0
 */
public class CollectionUtil {

    // 结合业务报文，目前针对Value最多只分析三层：Map/List/List, Map/List, Map/Map/List,
    // List,List/List,List/Map/List
    public static void sortedMap(Map<String, Object> map, String[] skipKeys) {
        //看看map是否包含需要跳过的key，如果是删除
        if(skipKeys != null){
            for(int j=0; j<skipKeys.length; j++){
                if(map.containsKey(skipKeys[j])){
                    map.remove(skipKeys[j]);
                }
            }
        }
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            Object value = entry.getValue();
            if (value instanceof List) { // value类型是List，进行排序
                sortedComplexList((List)value,skipKeys);

            } else if (value instanceof Map) { // value类型是Map
                Map<String, Object> temp = (Map<String, Object>) value;
                //看看map是否包含需要跳过的key，如果是删除
                if(skipKeys != null){
                    for(int j=0; j<skipKeys.length; j++){
                        if(temp.containsKey(skipKeys[j])){
                            temp.remove(skipKeys[j]);
                        }
                    }
                }
                for (Map.Entry<String, Object> element : temp.entrySet()) {
                    Object subValue = element.getValue();
                    if (subValue instanceof List) { // subValue类型是List，进行排序
                        sortedComplexList((List)subValue,skipKeys);
                    }
                }
            }
        }
    }

    private static <T> void sortedComplexList(List<T> list,String[] skipKeys) {
        // 先对最外层进行排序
        sortList(list);

        for (int i = 0; i < list.size(); i++) {
            Object elment = list.get(i);

            if (elment instanceof List) { // 对内层List进行排序
                sortList((List)elment);

            } else if (elment instanceof Map) {
                Map<String, Object> temp = (Map<String, Object>) elment;
                //看看map是否包含需要跳过的key，如果是删除
                if(skipKeys != null){
                    for(int j=0; j<skipKeys.length; j++){
                        if(temp.containsKey(skipKeys[j])){
                            temp.remove(skipKeys[j]);
                        }
                    }
                }
                for (Map.Entry<String, Object> entry : temp.entrySet()){
                    Object value = entry.getValue();
                    if (value instanceof List) { // value类型是List，进行排序
                        sortList((List)value);
                    }
                }
            }
        }
    }

    /**
     * 对List对象按照指定规则进行排序 （此处只处理：List中元素为：LinkedTreeMap类型的情况）
     * （使用gson.jar,从json支付串转换过来的List，其中的元素都是LinkedTreeMap类型）
     *
     * @param list
     *            List对象
     */
    private static <T> void sortList(List<T> list) {
        if (list == null || list.size() < 2) {
            return ;
        }

        Collections.sort(list, new Comparator<T>() {
            @Override
            public int compare(T o1, T o2) {

                int o1_id = 0;
                int o2_id = 0;

                if (o1 instanceof LinkedTreeMap) {
                    for(LinkedTreeMap.Entry<String,Object> temp: ((LinkedTreeMap<String,Object>)o1).entrySet()){
                        String kv = temp.getKey()+temp.getValue();
                        char[] kvArray = kv.toCharArray();
                        for (int i = 0; i < kvArray.length; i++) {
                            o1_id += kvArray[i];
                        }
                    }
                }

                if (o2 instanceof LinkedTreeMap) {
                    for(LinkedTreeMap.Entry<String,Object> temp: ((LinkedTreeMap<String,Object>)o2).entrySet()){
                        String kv = temp.getKey()+temp.getValue();
                        char[] kvArray = kv.toCharArray();
                        for (int i = 0; i < kvArray.length; i++) {
                            o2_id += kvArray[i];
                        }
                    }
                }

                if (o1_id != 0 && o2_id != 0) {
                    if (o2_id > o1_id) { // 小于0不变 （升序）
                        return -1;
                    } else if (o2_id < o1_id) { // 大于0交互位置
                        return 1;
                    } else {
                        return 0;
                    }
                }
                return 0; // 未知类型，无法比较大小
            }
        });
    }

}