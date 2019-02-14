package com.maobw1985.sample;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.maobw1985.myokhttpplus.MyOkHttpPlus;
import com.maobw1985.myokhttpplus.response.RawResponseHandler;

public class MainActivity extends AppCompatActivity {

    private final static String TAG = MainActivity.class.getSimpleName();

    private Button btnGet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btnGet = findViewById(R.id.btnGet);

        btnGet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MyOkHttpPlus.getInstance().get()
                        .url("http://www.baidu.com")
                        .tag("TEST")
                        .enqueue(new RawResponseHandler() {
                            @Override
                            public void onSuccess(int statusCode, String response) {
                                Log.d(TAG, response);
                            }

                            @Override
                            public void onFailure(int statusCode, String errorMsg) {
                                Log.d(TAG, errorMsg);
                            }
                        });
            }
        });
    }
}
