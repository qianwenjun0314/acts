package base; /**
 * Copyright (C), 2015-2018
 * FileName: base.ActsTestBase
 * Author:   qianwenjun
 * Date:     2018/1/12 13:42
 * Description:
 */

import db.DB;
import file.PropertiesFile;
import file.TextFile;
import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.testng.ITestResult;
import org.testng.Reporter;
import org.testng.annotations.*;
import utils.CsvDataUtil;

import java.io.File;
import java.util.Map;

/**
 * 〈测试基类-功能〉
 * 1、初始化，初始化一些关键字段信息
 * 2、读取数据源的配置文件
 *
 * @author qianwenjun
 * @create 2018/1/12
 * @since 1.0.0
 */
public class ActsTestBase {


    private static Logger logger = Logger.getLogger(ActsTestBase.class);

    //读取环境（int，online）和分区（baseline or 。。。）
    protected static String env = PropertiesFile.readFile("config/env.properties","test.env");
    protected static String xkzone = PropertiesFile.readFile("config/env.properties","test.xkzone");

    //保存解析的环境地址和秘钥
    protected static String address;
    protected static String key;

    // 是否开启https
    protected static boolean isHttps = false;

    //数据库操作组件
    public DB db;

    private static String currentTestClassName = null;


    public ActsTestBase() {
        getEnvInfo();
    }

    @BeforeClass
    public void getTestClassName() {

        ITestResult it = Reporter.getCurrentTestResult();
        currentTestClassName = it.getTestClass().getName();
    }

    @BeforeSuite
    public void setUp() throws Exception {
        logger.info("testCase开始执行.\r\n");
        logger.info("开始加载数据库操作组件.\r\n");
        ApplicationContext context = new ClassPathXmlApplicationContext("spring-common-test.xml");
        db = (DB)context.getBean("db");

    }

    @AfterSuite
    public void setDown() throws Exception {
        logger.info("本次回归测试结束.\r\n");
    }

    @DataProvider
    public Object[][] data() throws Exception {

        String[] tmpArr = currentTestClassName.split("\\.");
        logger.info("currentTestClassName===="+currentTestClassName);

        //String packageName = tmpArr[tmpArr.length - 2];
        String className = tmpArr[tmpArr.length - 1];

        logger.info("className===="+className);

        //直接从testdata下获取数据就行，少一些约束
        //return CSVData.getData("testData" + "/" + packageName + "/" + className + ".csv");
        return CsvDataUtil.getData("testdata" + "/" + className + ".csv");

    }

    /**
     * 参数处理器：
     * 需要动态赋值的参数值，使用占位符（格式:#$varname$）代替,在请求发出之前，解析占位符，将动态获取的值赋值给对应的参数；
     *
     * @param varsTable 变量列表：通过前置条件获取的值，在测试用例中会使用，先保存给指定的变量，变量名自定义（命名最好跟业务紧密结合）；key即为变量名
     * @param params    请求参数map，value中需要动态赋值的，使用占位符#$varname$，varname为varsTable中存在的key；
     *                  根据占位符中的varname，去varsTable中获取同名key对应的value值，替换掉占位符。
     */
    public void paramProcessor(Map<String, Object> params, Map<String, Object> varsTable) {
        for (Map.Entry<String, Object> param : params.entrySet()) {
            String tempChar = param.getValue().toString().trim();
            if (tempChar.startsWith("#$") && tempChar.endsWith("$")) {
                Object actValue = varsTable.get(tempChar.substring(2, tempChar.length() - 1));
                params.put(param.getKey(), actValue);
            }
        }
    }

    /**
     * 获取对应预期的Json字符串/数组
     * <p>
     * 从src/test/resources/expectResults目录中，找到对应的预期json字符串存放的文件，读取其内容；
     * 寻找文件的规则：找测试类名对应的目录，测试方法名对应的文件即可。下面是文件内容的说明：
     * 正常情况下：获取的json字符串格式跟接口返回的是一致的；
     * 有时候断言，需要过滤掉一部分字段，那么把需要过滤的字段，也组装到json字符串中；
     * eg：见下面的例子：
     * expect对应的value是接口的预期结果
     * skipKeys对应的value值是需要过滤掉的字段，用逗号分隔；
     * {
     * "expect":
     * {
     * "success": true,
     * "data": {
     * "phone": "10112340088",
     * "userPic": "http://test-store.ddyc.com/chewo/Avatar/4.png",
     * "userRegDate": 1474352950,
     * "userName": "10112",
     * "userId": 3691397,
     * "hasPayPassword": false,
     * "token": "A5A512458E5843C2951DDF8E0D653C37-1474352774147"
     * }
     * },
     * "skipKeys":"userRegDate,token"
     * }
     *
     * @param caseMethodName 测试方法名   （可以直接传入字符串，也可以通过Method参数动态获取方法名，见调用方）
     * @return 返回文件中的json字符串
     */
    public String getExceptJson(String caseMethodName) {
        String rootPath = new File(System.class.getResource("/").getPath()).getAbsolutePath();

        //调用该方法的子类的类名
        String clazz = this.getClass().getName();
        String[] temp = clazz.split("\\.");
        String path = rootPath + "/expectResults/" + temp[temp.length - 1] + "/";
        String fileName = path + caseMethodName + ".txt";
        String content = TextFile.read(fileName);
        System.out.println(fileName);
        if (content.indexOf("expect") < 0 || content.indexOf("skipKeys") < 0) {
            logger.info("预期结果格式填写不正确！key名为expect和skipKeys的字段必须存在.");
        }
        return content;
    }


    public static void getEnvInfo() {
        String startUrl = null;
        String fileName = "config/env.properties";

        if (!isHttps) {
            startUrl = PropertiesFile.readFile(fileName,".http.url");
        } else {
            startUrl = PropertiesFile.readFile(fileName,".https.url");
        }
        address = startUrl;
        key = PropertiesFile.readFile(fileName, env + ".key");
    }



    public static void main(String[] args) {
        logger.info("log test");
    }
}