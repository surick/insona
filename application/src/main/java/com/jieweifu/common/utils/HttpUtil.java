package com.jieweifu.common.utils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Map;

import net.sf.json.JSONObject;
import org.apache.http.HttpEntity;
import org.apache.http.ParseException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.*;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

/**
 * Http工具集
 */
public class HttpUtil {

    private static final CloseableHttpClient httpclient = HttpClients.createDefault();

    /**
     * 发送HttpGet请求
     *
     * @param url
     * @return
     */
    public static JSONObject sendGet(String url, Map<String, String> headMap, Map<String, String> map) {

        StringBuilder params = new StringBuilder();
        HttpGet httpget = null;
        if (map != null) {
            map.forEach(
                    (s, s2) ->
                            params.append(s).append(":").append(s2).append("&&")
            );
            httpget = new HttpGet(url + "?" + params);
        } else {
            httpget = new HttpGet(url);
        }
        if (headMap != null) {
            headMap.forEach(
                    httpget::setHeader
            );
        }
        CloseableHttpResponse response = null;
        try {
            response = httpclient.execute(httpget);
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        return getResult(response);
    }

    /**
     * 发送参数的HttpPost请求
     *
     * @param url 请求链接
     * @return json
     */
    public static JSONObject sendPost(String url, Map<String, String> map, JSONObject json) {

        HttpPost httppost = new HttpPost(url);
        if (map != null) {
            map.forEach(
                    httppost::setHeader
            );
        }
        if (json != null) {
            StringEntity s = null;
            try {
                s = new StringEntity(json.toString());
                s.setContentType("application/json");
                s.setContentEncoding("UTF-8");
                System.out.println(s);
                httppost.setEntity(s);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        CloseableHttpResponse response = null;
        try {
            response = httpclient.execute(httppost);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return getJson(response);
    }

    /**
     * 发送HttpPut请求，参数为map
     *
     * @param url  请求链接
     * @param json json数据
     * @return json
     */
    public static JSONObject sendPut(String url, Map<String, String> map, JSONObject json) {

        HttpPut httpPut = new HttpPut(url);
        if (map != null) {
            map.forEach(
                    httpPut::setHeader
            );
        }

        if (json != null) {
            StringEntity s = null;
            try {
                s = new StringEntity(json.toString());
                s.setContentEncoding("UTF-8");
                s.setContentType("application/json");
                httpPut.setEntity(s);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        CloseableHttpResponse response = null;
        try {
            response = httpclient.execute(httpPut);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return getJson(response);

    }

    /**
     * 发起http请求
     *
     * @param url 请求链接
     * @param map 封装的map参数
     * @return json
     */
    public static JSONObject sendGet(String url, Map<String, String> map) {

        HttpGet httpGet = null;
        StringBuilder params = new StringBuilder();
        if (map != null) {
            map.forEach(
                    (s, s2) ->
                            params.append(s).append(":").append(s2).append("&&")
            );
            httpGet = new HttpGet(url + "?" + params);
        } else {
            httpGet = new HttpGet(url);
        }
        CloseableHttpResponse response = null;
        try {
            response = httpclient.execute(httpGet);
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        return getResult(response);

    }

    public static JSONObject sendDelete(String url, Map<String, String> map, JSONObject json) {

        HttpDeleteWithBody httpDelete = new HttpDeleteWithBody(url);
        if (map != null) {
            map.forEach(
                    httpDelete::setHeader
            );
        }
        if (json != null) {
            StringEntity s = null;
            try {
                s = new StringEntity(json.toString());
                s.setContentEncoding("utf-8");
                s.setContentType("application/json");
                httpDelete.setEntity(s);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        CloseableHttpResponse response = null;
        try {
            response = httpclient.execute(httpDelete);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return getJson(response);
    }

    /**
     * https请求跳过证书验证
     *
     * @return httpclient
     */
    private static HttpClient wrapClient() {
        try {
            SSLContext ctx = SSLContext.getInstance("TLS");
            X509TrustManager tm = new X509TrustManager() {
                public X509Certificate[] getAcceptedIssuers() {
                    return null;
                }

                public void checkClientTrusted(X509Certificate[] arg0,
                                               String arg1) throws CertificateException {
                }

                public void checkServerTrusted(X509Certificate[] arg0,
                                               String arg1) throws CertificateException {
                }
            };
            ctx.init(null, new TrustManager[]{tm}, null);
            SSLConnectionSocketFactory ssf = new SSLConnectionSocketFactory(
                    ctx, NoopHostnameVerifier.INSTANCE);
            CloseableHttpClient httpclient = null;
            httpclient = HttpClients.custom().setSSLSocketFactory(ssf).build();
            return httpclient;
        } catch (Exception e) {
            return HttpClients.createDefault();
        }
    }

    /**
     * https免证书get请求
     *
     * @param url 请求链接
     * @param map 封装的map数据
     * @return json
     */
    public static JSONObject getSSL(String url, Map<String, String> map1, Map<String, String> map) {
        CloseableHttpClient httpClient = null;
        HttpGet httpGet = null;
        httpClient = (CloseableHttpClient) wrapClient();
        RequestConfig requestConfig = RequestConfig.custom()
                .setSocketTimeout(4000).setConnectTimeout(4000).build();
        StringBuilder params = new StringBuilder();

        if (map != null) {
            map.forEach(
                    (s, s2) ->
                            params.append(s).append(":").append(s2).append("&&")
            );
            httpGet = new HttpGet(url + "?" + params);
        } else {
            httpGet = new HttpGet(url);
        }
        if (map1 != null) {
            map1.forEach(
                    httpGet::setHeader
            );
        }
        httpGet.setConfig(requestConfig);
        CloseableHttpResponse response = null;
        try {
            response = httpClient.execute(httpGet);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return getResult(response);

    }

    /**
     * post的https请求
     *
     * @param url
     * @param map
     * @param json
     * @return
     */
    public static JSONObject postSSL(String url, Map<String, String> map, JSONObject json) {
        CloseableHttpClient httpClient = null;
        HttpPost httpPost = new HttpPost(url);
        httpClient = (CloseableHttpClient) wrapClient();
        RequestConfig requestConfig = RequestConfig.custom()
                .setSocketTimeout(4000).setConnectTimeout(4000).build();
        if (map != null) {
            map.forEach(
                    httpPost::setHeader
            );
        }
        if (json != null) {
            StringEntity s = null;
            try {
                s = new StringEntity(json.toString());
                s.setContentType("application/json");
                s.setContentEncoding("utf-8");
                httpPost.setEntity(s);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        CloseableHttpResponse response = null;
        httpPost.setConfig(requestConfig);
        try {
            response = httpClient.execute(httpPost);

        } catch (IOException e) {
            e.printStackTrace();
        }
        return getJson(response);
    }

    /**
     * put的https请求
     *
     * @param url
     * @param map
     * @param json
     * @return
     */
    public static JSONObject putSSL(String url, Map<String, String> map, JSONObject json) {
        CloseableHttpClient httpClient = null;
        HttpPut httpPut = new HttpPut(url);
        httpClient = (CloseableHttpClient) wrapClient();
        RequestConfig requestConfig = RequestConfig.custom()
                .setSocketTimeout(4000).setConnectTimeout(4000).build();
        if (map != null) {
            map.forEach(
                    httpPut::setHeader
            );
        }
        StringEntity s = null;
        if (json != null) {
            try {
                s = new StringEntity(json.toString());
                s.setContentEncoding("utf-8");
                s.setContentType("application/json");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        httpPut.setEntity(s);
        CloseableHttpResponse response = null;
        httpPut.setConfig(requestConfig);
        try {
            response = httpClient.execute(httpPut);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return getJson(response);

    }

    /**
     * delete的https请求
     *
     * @param url
     * @param map
     * @param json
     * @return
     */
    public static JSONObject DeleteSSL(String url, Map<String, String> map, JSONObject json) {
        HttpDeleteWithBody httpDelete = new HttpDeleteWithBody(url);
        CloseableHttpClient httpClient = null;
        httpClient = (CloseableHttpClient) wrapClient();
        RequestConfig requestConfig = RequestConfig.custom()
                .setSocketTimeout(4000).setConnectTimeout(4000).build();
        if (map != null) {
            map.forEach(
                    httpDelete::setHeader
            );
        }
        if (json != null) {
            StringEntity s = null;
            try {
                s = new StringEntity(json.toString());
                s.setContentType("application/json");
                s.setContentEncoding("UTF-8");
                httpDelete.setEntity(s);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        CloseableHttpResponse response = null;
        httpDelete.setConfig(requestConfig);
        try {
            response = httpClient.execute(httpDelete);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return getJson(response);
    }

    /**
     * 封装结果集
     *
     * @param response 响应
     * @return json
     */
    private static JSONObject getResult(CloseableHttpResponse response) {
        JSONObject jsonObject = null;
        String result = "";
        try {
            HttpEntity entity = response.getEntity();

            if (entity != null) {
                result = EntityUtils.toString(entity);
                if (result.startsWith("[") && result.endsWith("]")) {
                    String object = "{'devices':" + result + "}";
                    jsonObject = JSONObject.fromObject(object.replaceAll("null", "''"));
                } else {
                    jsonObject = JSONObject.fromObject(result.replaceAll("null", "''"));
                }

            }
        } catch (ParseException | IOException e) {
            e.printStackTrace();
        } finally {
            try {
                response.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return jsonObject;
    }

    /**
     * @param response 响应
     * @return json
     */
    private static JSONObject getJson(CloseableHttpResponse response) {
        if (response != null) {
            HttpEntity entity1 = response.getEntity();
            JSONObject jsonObject = null;
            String result = "";
            try {
                result = EntityUtils.toString(entity1);
                if (result.startsWith("[") && result.endsWith("]")) {
                    String object = "{'devices':" + result + "}";
                    jsonObject = JSONObject.fromObject(object.replaceAll("null", "''"));
                } else {
                    if (result.equals(""))
                        result = "{}";
                    jsonObject = JSONObject.fromObject(result.replaceAll("null", "''"));
                }

            } catch (ParseException | IOException e) {
                e.printStackTrace();
            }
            return jsonObject;
        } else {
            return null;
        }
    }

    /**
     * 重写delete,使其可以传body参数
     */
    static class HttpDeleteWithBody extends HttpEntityEnclosingRequestBase {
        static final String METHOD_NAME = "DELETE";

        public String getMethod() {
            return METHOD_NAME;
        }

        HttpDeleteWithBody(final String uri) {
            super();
            setURI(URI.create(uri));
        }

        public HttpDeleteWithBody(final URI uri) {
            super();
            setURI(uri);
        }

        public HttpDeleteWithBody() {
            super();
        }
    }

}
  
