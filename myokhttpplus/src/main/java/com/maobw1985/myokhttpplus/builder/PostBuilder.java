package com.maobw1985.myokhttpplus.builder;

import android.text.TextUtils;

import com.maobw1985.myokhttpplus.MyOkHttpPlus;
import com.maobw1985.myokhttpplus.callback.MyCallback;
import com.maobw1985.myokhttpplus.response.IResponseHandler;

import java.util.Map;

import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;

public class PostBuilder extends OkHttpRequestBuilderHasParam<PostBuilder> {
    private String mJsonBody = "";

    public PostBuilder(MyOkHttpPlus myOkHttpPlus) {
        super(myOkHttpPlus);
    }

    public PostBuilder json(String jsonBody) {
        this.mJsonBody = jsonBody;
        return this;
    }

    @Override
    public void enqueue(final IResponseHandler responseHandler) {
        try{
            if(TextUtils.isEmpty(mUrl))
                throw new IllegalArgumentException("url can not be null or empty");

            Request.Builder builder = new Request.Builder().url(mUrl);
            appendHeaders(builder,headers);

            if(mTag != null)
                builder.tag(mTag);

            if(!TextUtils.isEmpty(mJsonBody)) {
                RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"),mJsonBody);
                builder.post(body);
            }else{
                //表单提交形式
                FormBody.Builder encodingBody = new FormBody.Builder();
                appendParams(encodingBody,params);
                builder.post(encodingBody.build());
            }

            Request request = builder.build();

            myOkHttpPlus.getOkHttpClient()
                    .newCall(request)
                    .enqueue(new MyCallback(responseHandler));
        }catch (Exception e) {
            if(responseHandler != null)
                responseHandler.onFailure(0,e.getMessage());
        }
    }

    private void appendParams(FormBody.Builder builder, Map<String, String> params) {
        if (params != null && !params.isEmpty()) {
            for (String key : params.keySet()) {
                builder.add(key, params.get(key));
            }
        }
    }
}
