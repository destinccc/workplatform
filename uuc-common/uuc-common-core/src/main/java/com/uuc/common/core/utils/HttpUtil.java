package com.uuc.common.core.utils;

import com.alibaba.fastjson.JSONObject;
import org.apache.http.HttpEntity;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.config.ConnectionConfig;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.config.SocketConfig;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.LayeredConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLContexts;
import org.apache.http.conn.ssl.TrustStrategy;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicHeader;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import javax.net.ssl.SSLContext;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;


/**
 * @program: http请求工具类
 * @Description
 * @Author Yph
 */
public class HttpUtil {
 
    public static String defaultEncoding = "utf-8";
 
    /**
     * @Description: 发送http post请求，并返回响应实体
     * @Author: Yph
     * @Param [url：url]
     * @Return: java.lang.String
     **/
    public static String postRequest(String url) {
        return postRequest(url, null, null);
    }
 
    /**
     * @Description: 发送http post请求，并返回响应实体
     * @Author: Yph
     * @Param [url：url, params：json对象格式参数]
     * @Return: java.lang.String
     **/
    public static String postRequest(String url, JSONObject params) {
        return postRequest(url, null, params);
    }
 
    /**
     * @Description: 发送http post请求，并返回响应实体
     * @Author: Yph
     * @Param [url：url, headersMap：请求头，params：json对象格式参数]
     * @Return: java.lang.String
     **/
    public static String postRequest(String url, Map<String, String> headersMap,
                                     JSONObject params) {
        String result = null;
        CloseableHttpClient httpClient = buildHttpClient();
        HttpPost httpPost = new HttpPost(url);
        //请求头
        if (null != headersMap && headersMap.size() > 0) {
            for (Entry<String, String> entry : headersMap.entrySet()) {
                String key = entry.getKey();
                String value = entry.getValue();
                httpPost.addHeader(new BasicHeader(key, value));
            }
        }
        //参数JSON格式
        if (null != params && params.size() > 0) {
            StringEntity stringEntity = new StringEntity(params.toString(), "utf-8");
            stringEntity.setContentEncoding(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
            httpPost.setEntity(stringEntity);
        }
 
        try {
            CloseableHttpResponse response = null;
            if (httpClient != null) {
                response = httpClient.execute(httpPost);
            }
            try {
                HttpEntity entity = null;
                if (response != null) {
                    entity = response.getEntity();
                }
                if (entity != null) {
                    result = EntityUtils.toString(entity,
                            Charset.forName(defaultEncoding));
                }
            } finally {
                if (response != null) {
                    response.close();
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            try {
                if (httpClient != null) {
                    httpClient.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
 
        return result;
    }
 
    /**
     * @Description: 发送http get请求，并返回响应实体
     * @Author: Yph
     * @Param [url]
     * @Return: java.lang.String
     **/
    public static String getRequest(String url) {
        return getRequest(url, null);
    }
 
 
    /**
     * @Description: 发送http get请求，并返回响应实体
     * @Author: Yph
     * @Param [url：url, params：参数]
     * @Return: java.lang.String
     **/
    public static String getRequest(String url, Map<String, Object> params) {
        return getRequest(url, null, params);
    }
 
    /**
     * @Description:
     * @Author: Yph
     * @Param [url：url, headersMap：请求头, params：参数]
     * @Return: java.lang.String
     **/
    public static String getRequest(String url, Map<String, String> headersMap, Map<String, Object> params) {
        String result = null;
        CloseableHttpClient httpClient = buildHttpClient();
        try {
            String apiUrl = url;
            if (null != params && params.size() > 0) {
                StringBuffer param = new StringBuffer();
                int i = 0;
                for (String key : params.keySet()) {
                    if (i == 0)
                        param.append("?");
                    else
                        param.append("&");
                    param.append(key).append("=").append(params.get(key));
                    i++;
                }
                apiUrl += URLEncoder.encode(param.toString(),"utf-8");
                System.out.println("请求的全路径为"+apiUrl);
            }
 
            HttpGet httpGet = new HttpGet(apiUrl);
            if (null != headersMap && headersMap.size() > 0) {
                for (Entry<String, String> entry : headersMap.entrySet()) {
                    String key = entry.getKey();
                    String value = entry.getValue();
                    httpGet.addHeader(new BasicHeader(key, value));
                }
            }
            CloseableHttpResponse response = httpClient.execute(httpGet);
            try {
                HttpEntity entity = response.getEntity();
                if (null != entity) {
                    result = EntityUtils.toString(entity, defaultEncoding);
                }
            } finally {
                response.close();
            }
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                httpClient.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return result;
    }
 
    /**
     * 创建httpclient
     */
    public static CloseableHttpClient buildHttpClient() {
        try {
            RegistryBuilder<ConnectionSocketFactory> builder = RegistryBuilder
                    .create();
            ConnectionSocketFactory factory = new PlainConnectionSocketFactory();
            builder.register("http", factory);
            KeyStore trustStore = KeyStore.getInstance(KeyStore
                    .getDefaultType());
            SSLContext context = SSLContexts.custom().useTLS()
                    .loadTrustMaterial(trustStore, new TrustStrategy() {
                        public boolean isTrusted(X509Certificate[] chain,
                                                 String authType) throws CertificateException {
                            return true;
                        }
                    }).build();
            LayeredConnectionSocketFactory sslFactory = new SSLConnectionSocketFactory(
                    context,
                    SSLConnectionSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
            builder.register("https", sslFactory);
            Registry<ConnectionSocketFactory> registry = builder.build();
            PoolingHttpClientConnectionManager manager = new PoolingHttpClientConnectionManager(
                    registry);
            ConnectionConfig connConfig = ConnectionConfig.custom()
                    .setCharset(Charset.forName(defaultEncoding)).build();
            SocketConfig socketConfig = SocketConfig.custom()
                    .setSoTimeout(100000).build();
            manager.setDefaultConnectionConfig(connConfig);
            manager.setDefaultSocketConfig(socketConfig);
            return HttpClientBuilder.create().setConnectionManager(manager)
                    .build();
        } catch (KeyStoreException e) {
            e.printStackTrace();
        } catch (KeyManagementException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }
 
    public static void main(String[] args) {
        String url = "http://127.0.0.1:8080/ListAll";
 
        JSONObject params = new JSONObject();
        params.put("userId", "1");
        params.put("pageNo", 1);
        params.put("pageSize", 10);
        System.out.println(params);
 
        Map<String, String> heardMap = new HashMap<String, String>();
        heardMap.put("X-Access-Token", "1");
 
        String result = HttpUtil.postRequest(url, heardMap, params);
        System.out.println("响应--->>> " + result);
    }
 
    /**
     * @Description: 发送post请求
     * @Author: Yph
     * @Param [url：url, jsonObject：参数(json类型), encoding：编码格式]
     * @Return: java.lang.String
     **/
    public static String send(String url, JSONObject jsonObject, String encoding) throws ParseException, IOException {
        String body = "";
 
        //创建httpclient对象
        CloseableHttpClient client = HttpClients.createDefault();
        //创建post方式请求对象
        HttpPost httpPost = new HttpPost(url);
 
        //装填参数
        StringEntity s = new StringEntity(jsonObject.toString(), "utf-8");
        s.setContentEncoding(new BasicHeader(HTTP.CONTENT_TYPE,
                "application/json"));
        //设置参数到请求对象中
        httpPost.setEntity(s);
        System.out.println("请求地址：" + url);
        
        //设置header信息
        //指定报文头【Content-type】、【User-Agent】
        //httpPost.setHeader("Content-type", "application/x-www-form-urlencoded");
 
        httpPost.setHeader("Content-type", "application/json");
        httpPost.setHeader("User-Agent", "Mozilla/4.0 (compatible; MSIE 5.0; Windows NT; DigExt)");
 
        //执行请求操作，并拿到结果（同步阻塞）
        CloseableHttpResponse response = client.execute(httpPost);
        //获取结果实体
        HttpEntity entity = response.getEntity();
        if (entity != null) {
            //按指定编码转换结果实体为String类型
            body = EntityUtils.toString(entity, encoding);
        }
        EntityUtils.consume(entity);
        //释放链接
        response.close();
        return body;
    }
 
}