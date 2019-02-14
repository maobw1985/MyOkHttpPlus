package com.maobw1985.myokhttpplus.callback;

import com.maobw1985.myokhttpplus.response.IResponseHandler;
import com.maobw1985.myokhttpplus.utils.Utils;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class MyCallback implements Callback {

    private IResponseHandler responseHandler;

    public MyCallback(IResponseHandler handler) {
        this.responseHandler = handler;
    }

    @Override
    public void onFailure(Call call,final IOException e) {
        Utils.mDeliver.post(new Runnable() {
            @Override
            public void run() {
                if(responseHandler != null) {
                    responseHandler.onFailure(0, e.getMessage());
                }
            }
        });
    }

    @Override
    public void onResponse(Call call, final Response response) throws IOException {
        if(response.isSuccessful()) {
            if(responseHandler != null) {
                responseHandler.onSuccess(response);
            }
        }else{
            Utils.mDeliver.post(new Runnable() {
                @Override
                public void run() {
                    if(responseHandler != null)
                        responseHandler.onFailure(response.code(),"fail state = " + response.code());
                }
            });
        }
    }
}
