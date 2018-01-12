/**
 * Copyright (C), 2015-2017
 * FileName: HTTPReqGenUtil
 * Author:   qianwenjun
 * Date:     2017/12/20 15:37
 * Description:
 */
package utils;

import dataComponent.ReaderRecordHandler;
import ExcelUtils.LogUtil;
import com.jayway.restassured.response.Response;
import com.jayway.restassured.specification.RequestSpecification;
import org.apache.commons.io.IOUtils;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

import static com.jayway.restassured.RestAssured.given;

/**
 * 〈〉
 *
 * @author qianwenjun
 * @create 2017/12/20
 * @since 1.0.0
 */
public class HTTPReqGenUtil {

    private RequestSpecification reqSpec;

    //host+路径+请求类型+请求体body+请求头headers+请求cookie
    private String call_host = "";
    private String call_suffix = "";
    private String call_type = "";
    private String call_string = "";
    private String body = "";
    private Map<String,String> headers = new HashMap<String, String>();
    private HashMap<String,String> cookie_list = new HashMap<String, String>();

    public Map<String,String> getHeaders() {
        return headers;
    }

    public String getCallString() {
        return call_string;
    }

    public HTTPReqGenUtil() {
        reqSpec = given().relaxedHTTPSValidation();
    }

    public HTTPReqGenUtil(String proxy) {
        reqSpec = given().relaxedHTTPSValidation().proxy(proxy);
    }

    private  String[] tokenize_template(String template) {
        return template.split("(?=[<]{2})|(?<=[>]{2})");
    }

    public HTTPReqGenUtil generate_request(String template, ReaderRecordHandler record) throws Exception{
        return generate_request(template, (HashMap<String, String>) record.get_map());
    }
    /**
     *
     *
     * @param template String, should contain the full template.
     * @param record ReaderRecordHandler, the input data used to fill in replacement tags that exist in the template.
     * @return this Reference to this class, primarily to allow request generation and performance in one line
     * @throws Exception
     */

    public HTTPReqGenUtil generate_request(String template, HashMap<String,String> record) throws Exception {
        String filled_template = "";
        Boolean found_replacement = true;
        headers.clear();

        //将record里的对象字段转换成 template里的格式
        try {
            String[] tokens = tokenize_template(template);

            while(found_replacement) {
                found_replacement = false;
                filled_template = "";
                for(String item : tokens) {
                    if(item.startsWith("<<") && item.endsWith(">>")) {
                        found_replacement = true;
                        item = item.substring(2,item.length()-2);
                        if(!record.containsKey(item)) {
                            LogUtil.printConsoleLog("Template contained replacement string whose value did not exist in input record:[" + item + "]");
                        }
                        item = record.get(item);
                    }
                    filled_template += item;

                }

                tokens = tokenize_template(filled_template);
            }

        } catch (Exception e) {
            LogUtil.printConsoleLog("Problem performing replacements from template: ", e);
        }

        try {
            //文件内容一行一行读取，从BufferReader
            InputStream stream = IOUtils.toInputStream(filled_template,"UTF-8");
            BufferedReader in = new BufferedReader(new InputStreamReader(stream));
            String line = "";
            String[] line_token ;

            //读取第一行<<call_type>> <<call_suff>> HTTP/1.1
            line = in.readLine();
            line_token = line.split(" ");
            call_type = line_token[0];
            call_suffix = line_token[1];

            //读取第二行数据Host:<<host>>
            line = in.readLine();
            line_token = line.split(" ");
            call_host = line_token[1];

            call_string = call_host + call_suffix;

            //读取第三行到第五行数据
            //Authorization: <<AuthScheme>> <<AuthCreds>>
            //Accept: <<Accept>>
            //Content-type: <<Content-type>>
            line = in.readLine();
            while(line != null && !line.equals("")) {
                String lineP1 = line.substring(0,line.indexOf(":")).trim();
                String lineP2 = line.substring(line.indexOf(" "),line.length()).trim();

                headers.put(lineP1,lineP2);

                line = in.readLine();
            }

            //读取body的数据,body的数据和前面的数据有空行
            if(line != null && line.equals("")) {
                body = "";
                while((line =in.readLine()) != null && !line.equals("")) {
                    body += line;
                }
            }

        } catch(Exception e) {
            LogUtil.printConsoleLog("Problem setting request values from template: ", e);

        }

        return this;
    }

    public Response perform_request() throws Exception{
        Response response = null ;
        try {

            for(Map.Entry<String,String> entry : headers.entrySet()) {
                reqSpec.header(entry.getKey(),entry.getValue());
            }

            for(Map.Entry<String,String> entry : cookie_list.entrySet()) {
                reqSpec.cookie(entry.getKey(),entry.getValue());
            }

            if (call_type.equals("GET")) {
                response = reqSpec.get(call_string);
            } else if (call_type.equals("POST")) {
                response = reqSpec.body(body).post(call_string);
            } else if (call_type.equals("PUT")) {
                response = reqSpec.body(body).put(call_string);
            } else if (call_type.equals("DELETE")) {
                response = reqSpec.delete(call_string);
            } else {
                LogUtil.printConsoleLog("Unknown call type: [" + call_type + "]");
            }
        } catch(Exception e) {
            LogUtil.printConsoleLog("Problem performing request: ", e);
        }

        return response;
    }
}