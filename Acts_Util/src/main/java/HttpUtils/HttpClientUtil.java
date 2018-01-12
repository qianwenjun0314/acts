/**
 * Copyright (C), 2015-2018
 * FileName: HttpClientUtil
 * Author:   qianwenjun
 * Date:     2018/1/11 13:29
 * Description:
 */
package HttpUtils;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Map;

/**
 * 〈〉
 *
 * @author qianwenjun
 * @create 2018/1/11
 * @since 1.0.0
 */
public class HttpClientUtil {

    /**
     * 以json形式post http请求
     *
     * @param parameters
     * @param url
     * @return
     */
    public static HttpPost doPost(String parameters, String url) {
        HttpPost method = new HttpPost(url);
        if (parameters != null && !"".equals(parameters.trim())) {

            //set请求头信息
            method.addHeader("Content-type", "application/json; charset=utf-8");
            method.setHeader("Accept", "application/json");
            method.setHeader("xkzone","baseline");
            method.setEntity(new StringEntity(parameters, Charset.forName("UTF-8")));
        }
        return method;
    }

    public static CloseableHttpResponse doPostExcute(String parameters, String url, String cookie) {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpPost httpPost = doPost(parameters, url);
        httpPost.setHeader("cookie", cookie);
        try {
            CloseableHttpResponse response = httpClient.execute(httpPost);
            return response;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static CloseableHttpResponse doPostExcute(String parameters, String url) {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpPost httpPost = doPost(parameters, url);
        try {
            CloseableHttpResponse response = httpClient.execute(httpPost);
            return response;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * get http请求
     *
     * @param url
     * @return
     */
    public static HttpGet doGet(String url, Map<String, Object> params) {
        String body = null;
        for (Map.Entry<String, Object> param : params.entrySet()) {
            if (!url.contains("?"))
                url = url + "?" + param.getKey() + "=" + param.getValue().toString();
            else
                url = url + "&" + param.getKey() + "=" + param.getValue().toString();
        }
        System.out.println(url);
        //用get方法发送http请求
        HttpGet method = new HttpGet(url);

        return method;
    }

    public static CloseableHttpResponse doGetExcute(String url, Map<String, Object> params, String cookie) {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpGet method = doGet(url, params);
        method.setHeader("cookie", cookie);
        CloseableHttpResponse response = null;
        try {
            response = httpClient.execute(method);

        } catch (IOException e) {
            e.printStackTrace();
        }
        return response;
    }
}