package com.maobw1985.myokhttpplus.builder;

import com.maobw1985.myokhttpplus.MyOkHttpPlus;
import com.maobw1985.myokhttpplus.response.IResponseHandler;

import java.util.LinkedHashMap;
import java.util.Map;

import okhttp3.Headers;
import okhttp3.Request;

public abstract class OkHttpRequestBuilder<T extends OkHttpRequestBuilder> {
    protected String mUrl;
    protected Object mTag;
    protected Map<String,String> headers;

    protected MyOkHttpPlus myOkHttpPlus;

    public OkHttpRequestBuilder(MyOkHttpPlus myOkHttpPlus) {
        this.myOkHttpPlus = myOkHttpPlus;
    }

    /**
     * 异步执行
     * @param responseHandler 自定义回调
     */
     public abstract void enqueue(final IResponseHandler responseHandler);

    /**
     * 设置URL
     * @param url
     * @return
     */
    public T url(String url){
        this.mUrl = url;
        return (T) this;
    }

    /**
     * 设置TAG
     * @param tag
     * @return
     */
    public T tag(Object tag) {
        this.mTag = tag;
        return (T) this;
    }

    /**
     * 设置请求头
     * @param headers
     * @return
     */
    public T headers(Map<String,String> headers) {
        this.headers = headers;
        return (T) this;
    }

    public T headers(String key,String value) {
        if(headers == null) {
            headers = new LinkedHashMap<>();
        }
        headers.put(key,value);
        return (T) this;
    }


    protected void appendHeaders(Request.Builder builder,Map<String,String> headers) {
        Headers.Builder headerBuilder = new Headers.Builder();
        //加入全局请求头部
        Headers commonHeaders = myOkHttpPlus.getCommonHeaders();
        if(commonHeaders != null && commonHeaders.size() > 0)
            headerBuilder.addAll(commonHeaders);

        //加入每个请求的请求头
        if(headers != null && !headers.isEmpty()) {
            for(String key : headers.keySet()) {
                headerBuilder.add(key,headers.get(key));
            }
        }
        builder.headers(headerBuilder.build());
    }

}
