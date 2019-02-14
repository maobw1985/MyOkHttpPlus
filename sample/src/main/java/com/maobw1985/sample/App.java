package com.maobw1985.sample;

import android.app.Application;

import com.maobw1985.myokhttpplus.MyOkHttpPlus;
import com.maobw1985.myokhttpplus.MyOkHttpPlusConfiguration;

public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        MyOkHttpPlusConfiguration configuration = new MyOkHttpPlusConfiguration.Builder()
                .setTimeout(60 * 1000)
                .setDebug(BuildConfig.DEBUG)
                .build();

        MyOkHttpPlus.getInstance().init(configuration);
    }
}
