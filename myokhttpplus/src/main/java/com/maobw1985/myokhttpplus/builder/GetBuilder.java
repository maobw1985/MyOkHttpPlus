package com.maobw1985.myokhttpplus.builder;

import android.text.TextUtils;

import com.maobw1985.myokhttpplus.MyOkHttpPlus;
import com.maobw1985.myokhttpplus.callback.MyCallback;
import com.maobw1985.myokhttpplus.response.IResponseHandler;

import java.util.Map;

import okhttp3.Request;

public class GetBuilder extends OkHttpRequestBuilderHasParam<GetBuilder> {
    public GetBuilder(MyOkHttpPlus myOkHttpPlus) {
        super(myOkHttpPlus);
    }

    @Override
    public void enqueue(IResponseHandler responseHandler) {
        try{
            if(TextUtils.isEmpty(mUrl))
                throw new IllegalArgumentException("url not be null or empty");

            if(params != null && params.size() > 0){
                mUrl = appendParams(mUrl,params);
            }

            Request.Builder builder = new Request.Builder().url(mUrl).get();
            appendHeaders(builder,headers);

            if(mTag != null)
                builder.tag(mTag);

            Request request = builder.build();
            myOkHttpPlus.getOkHttpClient()
                    .newCall(request)
                    .enqueue(new MyCallback(responseHandler));
        }catch (Exception e) {
            if(responseHandler != null)
                responseHandler.onFailure(0,e.getMessage());
        }
    }

    private String appendParams(String url, Map<String, String> params) {
        StringBuilder sb = new StringBuilder();
        sb.append(url + "?");
        if (params != null && !params.isEmpty()) {
            for (String key : params.keySet()) {
                sb.append(key).append("=").append(params.get(key)).append("&");
            }
        }

        sb = sb.deleteCharAt(sb.length() - 1);
        return sb.toString();
    }
}
