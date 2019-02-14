package com.maobw1985.myokhttpplus;

import com.maobw1985.myokhttpplus.builder.GetBuilder;
import com.maobw1985.myokhttpplus.builder.PostBuilder;
import com.maobw1985.myokhttpplus.certification.OkHttpCerManager;

import java.io.InputStream;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;

import okhttp3.Call;
import okhttp3.CookieJar;
import okhttp3.Dispatcher;
import okhttp3.Headers;
import okhttp3.OkHttpClient;

public class MyOkHttpPlus {

    private OkHttpClient okHttpClient;
    private MyOkHttpPlusConfiguration configuration;

    private MyOkHttpPlus(){}

    static class MyOkHttpPlusHolder {
        public static MyOkHttpPlus INST = new MyOkHttpPlus();
    }

    public static MyOkHttpPlus getInstance() {
        return MyOkHttpPlusHolder.INST;
    }

    public synchronized void init(MyOkHttpPlusConfiguration conf) {
        this.configuration = conf;

        long timeout = configuration.getTimeout();
        if(timeout < 1)
            timeout = 30 * 1000;
        final OkHttpClient.Builder builder = new OkHttpClient.Builder()
                .connectTimeout(timeout, TimeUnit.MILLISECONDS)
                .readTimeout(timeout,TimeUnit.MILLISECONDS)
                .writeTimeout(timeout,TimeUnit.MILLISECONDS);

        //增加主机检验
        if(configuration.getHostnameVerifier() != null)
            builder.hostnameVerifier(configuration.getHostnameVerifier());

        List<InputStream> certificateList = configuration.getCertificateList();
        if(certificateList != null && certificateList.size() > 0) {
            final OkHttpCerManager cerManager = new OkHttpCerManager(builder);
            cerManager.setCertificates(certificateList);
        }

        //增加Cookies支持
        CookieJar cookieJar = configuration.getCookieJar();
        if(cookieJar != null)
            builder.cookieJar(cookieJar);

        //开启缓存支持
        if(configuration.getCache() != null) {
            builder.cache(configuration.getCache());
        }

        //开启OkHttp校验 用于HTTP CODE = 401
        if(configuration.getAuthenticator() != null)
            builder.authenticator(configuration.getAuthenticator());

        if(configuration.getCertificatePinner() != null)
            builder.certificatePinner(configuration.getCertificatePinner());

        //重定向
        builder.followRedirects(configuration.isFollowRedirects());
        builder.followSslRedirects(configuration.isFollowSslRedirects());

        if(configuration.getSslSocketFactory() != null)
            builder.sslSocketFactory(configuration.getSslSocketFactory());

        if(configuration.getDispatcher() != null)
            builder.dispatcher(configuration.getDispatcher());

        builder.retryOnConnectionFailure(configuration.isRetryOnConnectionFailure());
        if(configuration.getProxy() != null)
            builder.proxy(configuration.getProxy());

        //设置应用拦截器
        if(configuration.getInterceptorList() != null)
            builder.interceptors().addAll(configuration.getInterceptorList());
        //设置网络拦截器
        if(configuration.getNetworkInterceptorList() != null)
            builder.networkInterceptors().addAll(configuration.getNetworkInterceptorList());

        okHttpClient = builder.build();
    }

    public GetBuilder get(){
        return new GetBuilder(this);
    }

    public PostBuilder post() {
        return new PostBuilder(this);
    }


    /**
     * 更新通用请求参数
     * @param key key
     * @param value value
     */
    public void updateCommonParams(String key,String value) {
        Map<String,String> commonParam = configuration.getCommonParams();
        if(commonParam != null) {
            commonParam.put(key,value);
        }
    }

    /**
     * 更新通用Header
     * @param key key
     * @param value value
     */
    public void updateCommonHeader(String key,String value) {
        Headers headers = configuration.getCommonHeaders();
        if(headers == null)
            headers = new Headers.Builder().build();
        configuration.commonHeaders = headers.newBuilder().set(key,value).build();
    }

    public OkHttpClient getOkHttpClient() {
        return okHttpClient;
    }

    public OkHttpClient.Builder getOkHttpClientBuilder() {
        return okHttpClient.newBuilder();
    }

    public Map<String, String> getCommonParams() {
        return configuration.getCommonParams();
    }

    public List<InputStream> getCertificateList() {
        return configuration.getCertificateList();
    }

    public HostnameVerifier getHostnameVerifier() {
        return configuration.getHostnameVerifier();
    }

    public long getTimeout() {
        return configuration.getTimeout();
    }

    public Headers getCommonHeaders() {
        return configuration.getCommonHeaders();
    }

    /**
     * do cacel by tag
     * @param tag tag
     */
    public void cancel(Object tag) {
        Dispatcher dispatcher = okHttpClient.dispatcher();
        for (Call call : dispatcher.queuedCalls()) {
            if (tag.equals(call.request().tag())) {
                call.cancel();
            }
        }
        for (Call call : dispatcher.runningCalls()) {
            if (tag.equals(call.request().tag())) {
                call.cancel();
            }
        }
    }
}
