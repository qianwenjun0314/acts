/**
 * Copyright (C), 2015-2018
 * FileName: Report
 * Author:   qianwenjun
 * Date:     2018/1/12 15:52
 * Description:
 */
package report;

import org.apache.log4j.Logger;
import org.testng.*;
import org.testng.xml.XmlSuite;

import java.io.FileNotFoundException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 〈〉
 *
 * @author qianwenjun
 * @create 2018/1/12
 * @since 1.0.0
 */
public class Report implements IReporter{
    private static Logger logger = Logger.getLogger(Report.class);
    @Override
    public void generateReport(List<XmlSuite> xmlSuites, List<ISuite> suites, String outputDirectory) {
        List<ITestResult> list = new ArrayList<ITestResult>();
        for (ISuite suite : suites) {
            Map<String, ISuiteResult> suiteResults = suite.getResults();
            for (ISuiteResult suiteResult : suiteResults.values()) {
                ITestContext testContext = suiteResult.getTestContext();
                IResultMap passedTests = testContext.getPassedTests();
                IResultMap failedTests = testContext.getFailedTests();
                IResultMap skippedTests = testContext.getSkippedTests();
                IResultMap failedConfig = testContext.getFailedConfigurations();
                list.addAll(this.listTestResult(passedTests));
                list.addAll(this.listTestResult(failedTests));
                list.addAll(this.listTestResult(skippedTests));
                list.addAll(this.listTestResult(failedConfig));
            }
        }
        this.sort(list);
        this.outputResult(list);


    }

    private ArrayList<ITestResult> listTestResult(IResultMap resultMap){
        Set<ITestResult> results = resultMap.getAllResults();
        return new ArrayList<ITestResult>(results);
    }

    private void sort(List<ITestResult> list){
        Collections.sort(list, new Comparator<ITestResult>() {
            @Override
            public int compare(ITestResult r1, ITestResult r2) {
                if(r1.getStartMillis()>r2.getStartMillis()){
                    return 1;
                }else{
                    return -1;
                }
            }
        });
    }
    private String getStatus(int status){
        String statusString = null;
        switch (status) {
            case 1:
                statusString = "SUCCESS";
                break;
            case 2:
                statusString = "FAILURE";
                break;
            case 3:
                statusString = "SKIP";
                break;
            default:
                break;
        }
        return statusString;
    }

    private String formatDate(long date){
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return formatter.format(date);
    }

    private void outputResult(List<ITestResult> list) {
        StringBuffer sb = new StringBuffer();
        for (ITestResult result : list) {
            if(sb.length()!=0){
                sb.append("\r\n");
            }
            sb.append(result.getTestClass().getRealClass().getName())
                    .append(" ")
                    .append(result.getMethod().getMethodName())
                    .append(" ")
                    .append(this.formatDate(result.getStartMillis()))
                    .append(" ")
                    .append(result.getEndMillis()-result.getStartMillis())
                    .append("毫秒 ")
                    .append(this.getStatus(result.getStatus()));
        }
        try {
            logger.info("准备发送测试报告...");
            JavaMailWithAttachment mail = new JavaMailWithAttachment(false);
            mail.doSendHtmlEmail("自动化测试报告", sb.toString(), "youran@xiaokakeji.com", null);
            logger.info("测试报告发送成功！");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}
