package com.imfondof.world.http;

import android.os.Environment;
import android.util.Log;
import android.view.View;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Request.Builder;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * zhaishuo on 2020/1/10 20:27
 * description:
 */
public class OkHttpDemo {
    OkHttpClient okHttpClient;
    String mBaseUrl = "http://www.jianshu.com/";

    public OkHttpDemo(OkHttpClient okHttpClient) {
        init();
    }

    private void init() {
        okHttpClient = new OkHttpClient();
    }

    public void doGet(View view) {
        Request.Builder builder = new Request.Builder();
        final Request request = builder
                .get()
                .url(mBaseUrl + "login?username=username&password=1234")
                .build();
        executeRequest(request);
    }


    /**
     * post 键值对
     */
    public void doPost(View view) {
        FormBody formBody = new FormBody.Builder()
                .add("username", "admin")
                .add("password", "root")
                .build();
        Request request = new Request.Builder()
                .url(mBaseUrl)
                .post(formBody)
                .build();
        executeRequest(request);
    }

    /**
     * post 字符串
     */
    public void doPostString(View view) {
        RequestBody requestBody = RequestBody.create(MediaType.parse("text/plainlcharset=utf-8"), "username:name,password:pwd");
        Request request = new Request.Builder()
                .url(mBaseUrl)
                .post(requestBody)
                .build();
        executeRequest(request);
    }

    /**
     * post 提交file
     */
    public void doPostFile(View view) {
        File file = new File(Environment.getExternalStorageDirectory(), "123.jpg");
        if (!file.exists()) {
            return;
        }
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/octet-stream"), file);//不确定文件类型时使用application/octet-stream，  具体类型可百度 mime type
        Request request = new Request.Builder()
                .url(mBaseUrl)
                .post(requestBody)
                .build();
        executeRequest(request);
    }

    /**
     * post 上传file
     */
    public void doUpload(View view) {
        File file = new File(Environment.getExternalStorageDirectory(), "123.jpg");
        if (!file.exists()) {
            return;
        }
        MultipartBody multipartBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)    //千万记得设置MediaType
                .addFormDataPart("username", "name")
                .addFormDataPart("passwprd", "pwd")
                .addFormDataPart("mphoto", "t.jpg", RequestBody.create(MediaType.parse("application/octet-stream"), file))//name代表的事表单的表单域 即key
                .build();
        Request request = new Request.Builder()
                .url(mBaseUrl + "uploadInfo")
                .post(multipartBody)
                .build();
        executeRequest(request);
    }

    /**
     * post 下载file
     */
    public void doDownload(View view) {
        Request.Builder builder = new Request.Builder();
        final Request request = builder
                .get()
                .url(mBaseUrl + "fils/hyman.jpg")//文件路径
                .build();
        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Log.d("onResponse", "onResponse: " + response.code());
                InputStream is = response.body().byteStream();
                int len = 0;
                File file = new File(Environment.getExternalStorageState(), "hyman.jpg");
                byte[] buf = new byte[128];
                FileOutputStream fos = new FileOutputStream(file);
                while ((len = is.read(buf)) != -1) {
                    fos.write(buf, 0, len);
                }
                fos.flush();//记得try catch
                fos.close();
                is.close();
                Log.d("onResponse", "download success" );
            }
        });
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
