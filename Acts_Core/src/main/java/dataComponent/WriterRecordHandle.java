/**
 * Copyright (C), 2015-2017
 * FileName: WriterRecordHandle
 * Author:   qianwenjun
 * Date:     2017/12/20 15:24
 * Description: data handle
 */
package dataComponent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 〈data handle〉
 *
 * @author qianwenjun
 * @create 2017/12/20
 * @since 1.0.0
 */
public class WriterRecordHandle {

    private enum HeadType {
        OUTPUT, COMPARSION, RESULT
    }

    public HeadType myType;
    public List<String> headers;

    public WriterRecordHandle(String typeName) {
        if(typeName.equals("Output")) {
            this.myType = HeadType.OUTPUT;
            headers = getOutputHeaders();
        }else if(typeName.equals("Comparsion")) {
            this.myType = HeadType.COMPARSION;
            headers = getComparsionOHeaders();
        } else if(typeName.equals("Result")) {
            this.myType = HeadType.RESULT;
            headers = getResultOHeaders();
        }
    }


    public static List<String> getOutputHeaders() {
        List<String> outputHeaders = new ArrayList<String>();
        outputHeaders.add("ID");
        outputHeaders.add("TestCase");
        outputHeaders.add("Response");
        return outputHeaders;
    }

    public static List<String> getResultOHeaders() {
        List<String> resultHeaders = new ArrayList<String>();
        resultHeaders.add("ID");
        resultHeaders.add("TestCase");
        resultHeaders.add("Result");
        return resultHeaders;
    }

    public static List<String> getComparsionOHeaders() {
        List<String> comparsionHeaders = new ArrayList<String>();
        comparsionHeaders.add("ID");
        comparsionHeaders.add("TestCase");
        comparsionHeaders.add("AssertField");
        comparsionHeaders.add("ExceptedValue");
        comparsionHeaders.add("ActualValue");

        return comparsionHeaders;
    }

    public static Map<String,String> getOutputRecord(String ID, String TestCase, String Response) {
        Map<String,String> outputRecord = new HashMap<String, String>();
        outputRecord.put("ID",ID);
        outputRecord.put("TestCase",TestCase);
        outputRecord.put("Response",Response);
        return outputRecord;
    }

    public static Map<String,String> getResultRecord(String ID,String TestCase, String Result) {
        Map<String,String> outputRecord = new HashMap<String, String>();
        outputRecord.put("ID",ID);
        outputRecord.put("TestCase",TestCase);
        outputRecord.put("Result",Result);
        return outputRecord;
    }

    public static Map<String,String> getComparsionRecord(String ID, String TestCase, String AssertField, String ExceptedValue,String ActualValue) {
        Map<String,String> comparsionRecord = new HashMap<String, String>();
        comparsionRecord.put("ID", ID);
        comparsionRecord.put("TestCase",TestCase);
        comparsionRecord.put("AssertField",AssertField);
        comparsionRecord.put("ExceptedValue",ExceptedValue);
        comparsionRecord.put("ActualValue",ActualValue);
        return comparsionRecord;
    }
}