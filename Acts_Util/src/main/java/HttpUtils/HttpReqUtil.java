/**
 * Copyright (C), 2015-2017
 * FileName: HttpReqUtil
 * Author:   qianwenjun
 * Date:     2017/12/20 15:38
 * Description:
 */
package HttpUtils;

import ExcelUtils.LogUtil;
import com.google.gson.JsonObject;
import org.springframework.util.StringUtils;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Map;

/**
 * 〈http请求工具类〉
 * 1. 创建HttpClient对象。
 * 2. 创建请求方法的实例，并指定请求URL。如果需要发送GET请求，创建HttpGet对象；如果需要发送POST请求，创建HttpPost对象。
 * 3. 如果需要发送请求参数，可调用HttpGet、HttpPost共同的setParams(HetpParams params)方法来添加请求参数；对于HttpPost对象而言，也可调用setEntity(HttpEntity entity)方法来设置请求参数。
 * 4. 调用HttpClient对象的execute(HttpUriRequest request)发送请求，该方法返回一个HttpResponse。
 * 5. 调用HttpResponse的getAllHeaders()、getHeaders(String name)等方法可获取服务器的响应头；调用HttpResponse的getEntity()方法可获取HttpEntity对象，该对象包装了服务器的响应内容。程序可通过该对象获取服务器的响应内容。
 * 6. 释放连接。无论执行方法是否成功，都必须释放连接
 *
 * @author qianwenjun
 * @create 2017/12/20
 * @since 1.0.0
 */
public class HttpReqUtil {

    private static final Logger logger = LoggerFactory.getLogger(HttpReqUtil.class);

    /**
     * 1、新建httpclient对象
     * 2、新建httppost请求对象，入参url
     * 3、设置header（cookie，accept，content-type），parameters-entity）
     *
     * @param url
     * @param parameters
     * @param cookie
     * @return
     */
    public CloseableHttpResponse doHttpPost (String url, JsonObject parameters, String cookie) {

        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost(url);
        //当调用doHttpPost方法的参数不是jsonObject形式，可以先强转成jsonObject
        if (!StringUtils.isEmpty(parameters)) {

            httpPost.setHeader("Content-type","application/json; charset=utf-8");
            httpPost.setHeader("Accept","application/json");
            httpPost.setHeader("cookie",cookie);

            StringEntity entity = new StringEntity(parameters.toString(), Charset.forName("UTF-8"));
            entity.setContentEncoding("utf-8");
            entity.setContentType("application/json");
            httpPost.setEntity(entity);

        }
        try {
            CloseableHttpResponse response = httpClient.execute(httpPost);
            if(response.getStatusLine().getStatusCode()== 200 ) {
                return response;

            }
        } catch (IOException e) {
            e.printStackTrace();
//            logger.error("post请求提交失败,错误信息为:"+ e.getMessage());
            LogUtil.printErrorLog("post请求提交失败,错误信息为:", e.getMessage());
        }
        return null;
    }

    public  CloseableHttpResponse doHttpGet (String url , Map<String,Object> params , String cookie) {
        CloseableHttpClient httpClient  = HttpClients.createDefault();
        //拼接参数
        for (Map.Entry<String,Object> param : params.entrySet()) {
            if (!url.contains("?")) {
                url = url + "?" + param.getKey() + "=" + param.getValue();
            } else {
                url = url + "&" + param.getKey() + "=" + param.getValue();
            }
        }



        System.out.println(url);
        HttpGet httpGet = new HttpGet(url);
        httpGet.setHeader("cookie",cookie);

        CloseableHttpResponse response = null;
        try {
            response = httpClient.execute(httpGet);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return response;

    }


}