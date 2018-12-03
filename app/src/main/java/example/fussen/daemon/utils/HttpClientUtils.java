package example.fussen.daemon.utils;

import android.util.Log;

import org.apache.http.*;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLConnection;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by mrpan on 2018/8/4.
 */
public class HttpClientUtils {


    public static final String DEFAULT_CHARSET = "UTF-8";

    /**
     * GET请求
     *
     * @param url
     *            请求URL
     * @return
     * @author J 2016-1-12 上午2:49:52
     */
    public static HttpResult get(String url) {
        HttpResult httpResult = new HttpResult();
        String result = "";
        BufferedReader in = null;
        try {
            URL realUrl = new URL(url);
            // 打开和URL之间的连接
            URLConnection connection = realUrl.openConnection();
            // 设置通用的请求属性
            connection.setRequestProperty("accept", "*/*");
            connection.setRequestProperty("connection", "Keep-Alive");
            connection.setRequestProperty("contentType",
                    "application/json;UTF-8");
            connection.setRequestProperty("user-agent",
                    "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
            // 建立实际的连接
            connection.connect();
            // 获取所有响应头字段
            //Map<String, List<String>> map = connection.getHeaderFields();
            // 遍历所有的响应头字段
            /*
			 * for (String key : map.keySet()) { System.out.println(key + "--->"
			 * + map.get(key)); }
			 */
            // 定义 BufferedReader输入流来读取URL的响应
            in = new BufferedReader(new InputStreamReader(
                    connection.getInputStream(), "UTF-8"));
            String line;
            while ((line = in.readLine()) != null) {
                result += line;
            }
        } catch (Exception e) {
            System.out.println("发送GET请求出现异常！" + e);
            e.printStackTrace();
        }
        // 使用finally块来关闭输入流
        finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
        httpResult.setContent(result);
        httpResult.setStatusCode(HttpStatus.SC_OK);
        return httpResult;
    }

    /**
     * POST 请求
     *
     * @param url
     *            请求URL
     * @param params
     *            请求参数
     * @return
     * @author J 2016-1-12 上午2:48:59
     */
    public static HttpResult post(String url, Map<String, String> params,
                                  String contentType) {
        HttpResult httpResult = new HttpResult();
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            HttpPost postMethod = new HttpPost(url);
            HttpClientUtils.setRequestEntity(postMethod, params);

            HttpContext context = new BasicHttpContext();
            HttpResponse res = httpClient.execute(postMethod, context);
            if (res.getStatusLine().getStatusCode() != HttpStatus.SC_OK) {
                Log.e("请求出现异常，url={}, res=", new Object[] { url, res }.toString());
            }

            HttpEntity resEntity = res.getEntity();
            if (resEntity == null) {
                return httpResult;
            }
            if (resEntity.getContentType().getValue()
                    .indexOf(HttpResult.CONTENT_TYPE_HTML) != 0
                    && resEntity.getContentType().getValue()
                    .indexOf(HttpResult.CONTENT_TYPE_HTML_JSON) != 0) {
                byte[] stream = EntityUtils.toByteArray(resEntity);
                httpResult.setStream(stream);
            } else {
                String result = EntityUtils
                        .toString(resEntity, DEFAULT_CHARSET);
                httpResult.setContent(result);
            }
            httpResult.setStatusCode(HttpStatus.SC_OK);
            return httpResult;
        } catch (IOException e) {

        }
        return httpResult;
    }

    /**
     * POST 请求
     *
     * @param url
     *            请求URL
     * @param params
     *            请求参数
     * @return
     * @author J 2016-1-12 上午2:48:59
     */
    public static HttpResult post(String url, String params) {
        HttpResult httpResult = new HttpResult();
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            HttpPost postMethod = new HttpPost(url);
            HttpClientUtils.setRequestEntity(postMethod, params);

            HttpContext context = new BasicHttpContext();
            HttpResponse res = httpClient.execute(postMethod, context);
            if (res.getStatusLine().getStatusCode() != HttpStatus.SC_OK) {
                Log.e("请求出现异常，url={}, res=", new Object[] { url, res }.toString());
            }

            HttpEntity resEntity = res.getEntity();
            if (resEntity == null) {
                return httpResult;
            }
            if (resEntity.getContentType().getValue()
                    .indexOf(HttpResult.CONTENT_TYPE_HTML) != 0) {
                byte[] stream = EntityUtils.toByteArray(resEntity);
                httpResult.setStream(stream);
            } else {
                String result = EntityUtils
                        .toString(resEntity, DEFAULT_CHARSET);
                httpResult.setContent(result);
            }
            httpResult.setStatusCode(HttpStatus.SC_OK);
            return httpResult;
        } catch (IOException e) {
        }
        return httpResult;
    }

    /**
     * 设置请求参数
     *
     * @param httpPost
     * @param params
     * @throws UnsupportedEncodingException
     * @author J 2016-1-12 上午2:48:29
     */
    private static void setRequestEntity(HttpPost httpPost,
                                         Map<String, String> params) throws UnsupportedEncodingException {
        if (params == null || params.isEmpty()) {
            return;
        }

        List<NameValuePair> parameters = new ArrayList<NameValuePair>();
        Set<String> paramsKeys = params.keySet();
        for (String paramKey : paramsKeys) {
            parameters.add(new BasicNameValuePair(paramKey, params
                    .get(paramKey)));
        }
        httpPost.setEntity(new UrlEncodedFormEntity(parameters, DEFAULT_CHARSET));
    }

    public static String runGet(String url) {
        CloseableHttpClient client = HttpClients.createDefault();
        String result = null;
        try {
            HttpGet httpGet = new HttpGet(url);
            httpGet.addHeader("Content-Type", "text/html;charset=UTF-8");
            CloseableHttpResponse response = client.execute(httpGet);
            try {
                HttpEntity entity = response.getEntity();
                if (entity != null) {
                    result = EntityUtils.toString(entity);
                }
            } finally {
                response.close();
            }
        } catch (ClientProtocolException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            try {
                client.close();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        return result;
    }

    /**
     * 设置请求参数
     *
     * @param httpPost
     * @param params
     * @throws UnsupportedEncodingException
     * @author J 2016-1-12 上午2:48:29
     */
    private static void setRequestEntity(HttpPost httpPost, String params)
            throws UnsupportedEncodingException {
        if (params == null || "".equals(params)){
            return;
        }
        httpPost.setEntity(new StringEntity(params, DEFAULT_CHARSET));
    }

}