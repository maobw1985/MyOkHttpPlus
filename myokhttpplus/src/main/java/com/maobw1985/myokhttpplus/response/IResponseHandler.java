package com.maobw1985.myokhttpplus.response;

import okhttp3.Response;

public interface IResponseHandler {
    void onSuccess(Response response);
    void onFailure(int statusCode, String errorMsg);
    void onProgress(long currentBytes, long totalBytes);
}
