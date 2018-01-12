package HttpTest; /**
 * Copyright (C), 2015-2017
 * FileName: HttpTest.HTTPReqGenUtilTest
 * Author:   qianwenjun
 * Date:     2017/12/20 15:42
 * Description:
 */

import dataComponent.DataReader;
import dataComponent.DataWriter;
import dataComponent.ReaderRecordHandler;
import dataComponent.WriterRecordHandle;
import ExcelUtils.SheetUtil;
import utils.HTTPReqGenUtil;
import com.jayway.restassured.response.Response;
import org.apache.commons.io.IOUtils;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.json.JSONException;
import org.skyscreamer.jsonassert.JSONCompare;
import org.skyscreamer.jsonassert.JSONCompareMode;
import org.skyscreamer.jsonassert.JSONCompareResult;
import org.testng.Assert;
import org.testng.ITestContext;
import org.testng.annotations.*;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 〈〉
 *
 * @author qianwenjun
 * @create 2017/12/20
 * @since 1.0.0
 */
public class HTTPReqGenUtilTest {

    private Response response;
    private DataReader myInputData;
    private DataReader myBaselineData;
    private String template;

    public String getTestName() {
        return "API Test";
    }

    String filePath = "/Users/qianwenjun/HttpReqGenTest.xlsx";

    XSSFWorkbook wb = null;
    XSSFSheet inputSheet = null;
    XSSFSheet baselineSheet = null;
    XSSFSheet outputSheet = null;
    XSSFSheet comparsionSheet = null;
    XSSFSheet resultSheet = null;

    private double totalcase = 0;
    private double failedcase = 0;
    private String startTime = "";
    private String endTime = "";

    /**
     * 读取Excel (WorkBook) 的 ‘Input’ 和 ‘Baseline’ sheet
     *
     * @param  path
     * @return
     */
    @BeforeTest
    @Parameters("WorkBook")
    public void setUp(String path) {
        filePath = path;
        try {
            wb = new XSSFWorkbook(new FileInputStream(filePath));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        inputSheet = wb.getSheet("Input");
        baselineSheet = wb.getSheet("Baseline");


        SheetUtil.removeSheetByName(wb, "Output");
        SheetUtil.removeSheetByName(wb, "Comparsion");
        SheetUtil.removeSheetByName(wb, "Result");

        outputSheet = wb.createSheet("Output");
        DataWriter.setHeadersValue(outputSheet,"Output");
        comparsionSheet = wb.createSheet("Comparsion");
        DataWriter.setHeadersValue(comparsionSheet,"Comparsion");
        resultSheet = wb.createSheet("Result");
        DataWriter.setHeadersValue(resultSheet,"Result");


        try {
            InputStream is = HTTPReqGenUtilTest.class.getClassLoader().getResourceAsStream("http_request_template.txt");
            template = IOUtils.toString(is,"UTF-8");

        } catch (Exception e) {
            Assert.fail("Problem fetching data from input file:" + e.getMessage());
        }

        SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        startTime = sf.format(new Date());
    }

    /**
     * TestNG的DataProvider,
     * 首先用DataReader构造函数，读取Excel中Input的数据，放入HashMap<String, ReaderRecordHandler>,
     * Map的key值就是test case的ID，value是RecordHandler对象，
     * 此对象中一个重要的成员属性就是input sheet里面 column和value 的键值对，
     * 遍历Map将test case ID 与 test case的value 即input sheet前两列的值放入List<Object[]> ，
     * 最后返回List的迭代器iterator (为了循环调用@Test方法)
     *
     * @param context
     * @return
     */
    @DataProvider(name = "WorkBookData")
    protected Iterator<Object[]> testProvider(ITestContext context) {
        List<Object[]> test_IDs = new ArrayList<Object[]>();

        myInputData = new DataReader(inputSheet,true,true,0);
        Map<String, ReaderRecordHandler> input = myInputData.get_map();

        //对input进行排序，根据test caseID
        List<Map.Entry<String,ReaderRecordHandler>> list = new ArrayList<Map.Entry<String, ReaderRecordHandler>>(input.entrySet());
        Collections.sort(list, new Comparator<Map.Entry<String, ReaderRecordHandler>>() {
            public int compare(Map.Entry<String, ReaderRecordHandler> o1, Map.Entry<String, ReaderRecordHandler> o2) {
                return o1.getKey().compareTo(o2.getKey());
            }
        });

        Map<String,ReaderRecordHandler> sortMap = new HashMap<String, ReaderRecordHandler>();
        for(Map.Entry<String,ReaderRecordHandler> mapping : list) {
            sortMap.put(mapping.getKey(),mapping.getValue());
        }

        //组装test_IDs（test case ID和test case 的value，即input sheet的前两列值放入list）
        for(Map.Entry<String,ReaderRecordHandler> entry : sortMap.entrySet()) {
            String test_ID = entry.getKey();
            String test_case = entry.getValue().get("TestCase");
            if(!test_ID.equals("") && !test_case.equals("")) {
                test_IDs.add(new Object[] {test_ID,test_case});
            }
            totalcase++;
        }

        myBaselineData = new DataReader(baselineSheet,true,true,0);

        return test_IDs.iterator();
    }

    @Test(dataProvider = "WorkBookData",description = "apiTest")
    public void api_test(String ID,String test_case) {
        HTTPReqGenUtil myReqGen = new HTTPReqGenUtil();
        try {

            myReqGen.generate_request(template,myInputData.get_record(ID));
            response = myReqGen.perform_request();
        } catch (Exception e) {
            Assert.fail("Problem using HTTPRequestGenerator to generate response: " + e.getMessage());
        }

        String baseline_message = myBaselineData.get_record(ID).get("Response");


        if(response.statusCode() == 200) {
            try {
                DataWriter.writeOutputData(outputSheet,"Output", WriterRecordHandle.getOutputRecord(ID,test_case,response.asString()));

                JSONCompareResult result = JSONCompare.compareJSON(baseline_message,response.asString(), JSONCompareMode.NON_EXTENSIBLE);
                if (!result.passed()) {
                    DataWriter.writeComparsionData(comparsionSheet,"Comparsion",
                            WriterRecordHandle.getComparsionRecord(ID,
                                    test_case,
                                    result.getFieldFailures().get(0).getField().toString(),
                                    result.getFieldFailures().get(0).getExpected().toString(),
                                    result.getFieldFailures().get(0).getActual().toString()));
                    for(int i=1; i < result.getFieldFailures().size(); i++ ) {
                        DataWriter.writeComparsionData(comparsionSheet,"Comparsion",
                                WriterRecordHandle.getComparsionRecord("","",
                                        result.getFieldFailures().get(i).getField().toString(),
                                        result.getFieldFailures().get(i).getExpected().toString(),
                                        result.getFieldFailures().get(i).getActual().toString()));
                    }
                    DataWriter.writeResultData(resultSheet,"Result", WriterRecordHandle.getResultRecord(ID, test_case,"false"),0);
                    failedcase++;
                } else {
                    DataWriter.writeResultData(resultSheet,"Result", WriterRecordHandle.getResultRecord(ID, test_case,"true"),0);
                }
            }catch (JSONException e) {
                DataWriter.writeComparsionData(comparsionSheet,"Comparsion", WriterRecordHandle.getComparsionRecord(ID,test_case,"Problem to assert Response and baseline messages: ",e.getMessage().toString(),""));
                DataWriter.writeResultData(resultSheet,"Result", WriterRecordHandle.getResultRecord(ID, test_case,"error"),0);
                failedcase++;
                Assert.fail("Problem to assert Response and baseline messages: " + e.getMessage());
            }
        }
    }

    @AfterTest
    public void teardown() {
        SimpleDateFormat sf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        endTime = sf.format(new Date());
        DataWriter.writeResultData(resultSheet,"Result", WriterRecordHandle.getResultRecord("","totalcase", String.valueOf(totalcase)),1);
        DataWriter.writeResultData(resultSheet,"Result", WriterRecordHandle.getResultRecord("","failedcase", String.valueOf(failedcase)),1);
        DataWriter.writeResultData(resultSheet,"Result", WriterRecordHandle.getResultRecord("","startTime", String.valueOf(startTime)),1);
        DataWriter.writeResultData(resultSheet,"Result", WriterRecordHandle.getResultRecord("","endTime", String.valueOf(endTime)),1);
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(filePath);
            wb.write(fileOutputStream);
            fileOutputStream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}