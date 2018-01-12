/**
 * Copyright (C), 2015-2017
 * FileName: DoHttpGetTest
 * Author:   qianwenjun
 * Date:     2017/12/20 16:23
 * Description:
 */
package UtilTest;

import HttpUtils.HttpReqUtil;
import com.google.gson.JsonObject;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.util.EntityUtils;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

/**
 * 〈〉
 *
 * @author qianwenjun
 * @create 2017/12/20
 * @since 1.0.0
 */
public class DoHttpGetTest {

    @Test
    public void doHttpGet() throws Exception {

        HttpReqUtil util = new HttpReqUtil();

        Map<String,Object> params = new HashMap<String, Object>();
        String cookies = null;
        String url = "http://www.baidu.com";

        CloseableHttpResponse response = util.doHttpGet(url,params,cookies);
        HttpEntity entity = response.getEntity();
        System.out.println("response is " + response);
        System.out.println("entity is " + entity);

    }

    @Test
    public void doGetPartPics() throws Exception {
        HttpReqUtil util = new HttpReqUtil();

        Map<String,Object> params = new HashMap<String, Object>();
        params.put("carModelId","608");
        params.put("categoryId","9");
        params.put("categoryName","动力总成及组件");
        params.put("type","1");

        String url = "https://cs.ddyc.com/cloud-stock/epc/getPartPics";
        String cookie = null;
        CloseableHttpResponse response = util.doHttpGet(url,params,getLogonCookie());

        //判断response是否成功
        System.out.println("status:" + response.getStatusLine().getStatusCode());

        //把Response内的Entity内容转换成String
        String result = EntityUtils.toString(response.getEntity());
        System.out.println("result:" + result);

    }

    public String getLogonCookie() {
        String cookie = null;
        HttpReqUtil util = new HttpReqUtil();

        String url = "https://cs.ddyc.com/cloud-stock/user/login";
        JsonObject params = new JsonObject();
        params.addProperty("phone","13735532621");
        params.addProperty("password","xiaoka123");

        CloseableHttpResponse response = util.doHttpPost(url,params,cookie);
        if (response != null) {
            cookie = response.getFirstHeader("Set-Cookie").getValue();
        }
        return cookie;
    }
}