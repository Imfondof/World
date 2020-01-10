package com.imfondof.world.http;

import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * zhaishuo on 2020/1/10 20:27
 * description:
 */
public class OkHttpDemo {
    OkHttpClient okHttpClient;

    public OkHttpDemo(OkHttpClient okHttpClient) {
        init();
    }

    private void init() {
        okHttpClient = new OkHttpClient();
    }

    /**
     * post 键值对
     */
    public void doPost() {
        FormBody formBody = new FormBody.Builder()
                .add("username", "admin")
                .add("password", "root")
                .build();
        Request request = new Request.Builder()
                .url("http://www.jianshu.com/")
                .post(formBody)
                .build();
        executeRequest(request);
    }

    /**
     * post 字符串
     */
    public void doPostString() {
        RequestBody requestBody = RequestBody.create(MediaType.parse("text/plainlcharset=utf-8"), "username:name,password:pwd");
        Request request = new Request.Builder()
                .url("http://www.jianshu.com/")
                .post(requestBody)
                .build();
        executeRequest(request);
    }

    /**
     * post 文件
     */
    public void doPostFile() {
        File file = new File(Environment.getExternalStorageDirectory(), "123.jpg");
        if (!file.exists()) {
            return;
        }
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/octet-stream"), file);//不确定文件类型时使用application/octet-stream，  具体类型可百度 mime type
        Request request = new Request.Builder()
                .url("http://www.jianshu.com/")
                .post(requestBody)
                .build();
        executeRequest(request);
    }


    private void executeRequest(Request request) {
        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Log.d("onResponse", "onResponse: " + response.code());
            }
        });
    }
}
