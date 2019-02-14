package com.maobw1985.myokhttpplus.response;

import com.maobw1985.myokhttpplus.utils.Utils;

import okhttp3.Response;
import okhttp3.ResponseBody;

public abstract class RawResponseHandler implements IResponseHandler {

    @Override
    public void onSuccess(final Response response) {
        final ResponseBody responseBody = response.body();
        String responseBodyStr = "";
        try{
            responseBodyStr = responseBody.string();
        }catch (Exception e) {
            Utils.mDeliver.post(new Runnable() {
                @Override
                public void run() {
                    onFailure(response.code(),"can not read response body");
                }
            });
            return;
        }finally {
            if(responseBody != null)
                responseBody.close();
        }

        final String finalResponseBodyStr = responseBodyStr;
        Utils.mDeliver.post(new Runnable() {
            @Override
            public void run() {
                onSuccess(response.code(),finalResponseBodyStr);
            }
        });
    }

    @Override
    public void onProgress(long currentBytes, long totalBytes) {

    }

    public abstract void onSuccess(int statusCode, String response);
}
