package com.maobw1985.myokhttpplus.builder;

import com.maobw1985.myokhttpplus.MyOkHttpPlus;

import java.util.LinkedHashMap;
import java.util.Map;

public abstract class OkHttpRequestBuilderHasParam<T extends OkHttpRequestBuilder> extends OkHttpRequestBuilder<T> {

    protected Map<String,String> params;

    public OkHttpRequestBuilderHasParam(MyOkHttpPlus myOkHttpPlus) {
        super(myOkHttpPlus);
    }

    public T params(Map<String,String> params) {
        this.params = params;
        return (T) this;
    }

    public T params(String key,String value) {
        if(params == null)
            params = new LinkedHashMap<>();
        params.put(key,value);
        return (T) this;
    }

}
