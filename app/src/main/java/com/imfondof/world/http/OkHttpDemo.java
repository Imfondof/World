package com.imfondof.world.http;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.util.Log;
import android.view.View;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.FormBody;
import okhttp3.HttpUrl;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Imfondof on 2020/1/10 20:27
 * hyman的慕课教程
 */

/**
 * 封装步骤总结：
 * 1 拿到OkHttpClient对象
 * 2 构造Request
 * 2.1 构造RequestBody（继承基类RequestBody即可）
 * 2.2 包装RequestBody
 * 3 Call -> execute
 */
public class OkHttpDemo {
    OkHttpClient okHttpClient;
    String mBaseUrl = "http://www.jianshu.com/";

    public OkHttpDemo() {
        init();
    }

    private void init() {
        okHttpClient = new OkHttpClient.Builder()
                .cookieJar(new CookieJar() {//cookie持久化
                    private final HashMap<HttpUrl, List<Cookie>> cookieStore = new HashMap<>();

                    @Override
                    public void saveFromResponse(HttpUrl url, List<Cookie> cookies) {
                        cookieStore.put(url, cookies);
                    }

                    @Override
                    public List<Cookie> loadForRequest(HttpUrl url) {
                        List<Cookie> cookies = cookieStore.get(url);
                        return cookies != null ? cookies : new ArrayList<Cookie>();
                    }
                })
                .build();
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
        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)    //千万记得设置MediaType
                .addFormDataPart("username", "name")
                .addFormDataPart("passwprd", "pwd")
                .addFormDataPart("mphoto", "t.jpg", RequestBody.create(MediaType.parse("application/octet-stream"), file))//name代表的事表单的表单域 即key
                .build();
        CountingRequestBody countingRequestBody = new CountingRequestBody(requestBody, new CountingRequestBody.Listener() {//上传进度
            @Override
            public void onRequestProgress(long byteWritten, long contentLength) {
                Log.d("onRequestProgress", "onRequestProgress: " + byteWritten + "/" + contentLength);
            }
        });
        Request request = new Request.Builder()
                .url(mBaseUrl + "uploadInfo")
                .post(countingRequestBody)
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
                long total = response.body().contentLength();//文件总长度
                long sum = 0;
                InputStream is = response.body().byteStream();
                int len = 0;
                File file = new File(Environment.getExternalStorageState(), "hyman.jpg");
                byte[] buf = new byte[128];
                FileOutputStream fos = new FileOutputStream(file);
                while ((len = is.read(buf)) != -1) {
                    fos.write(buf, 0, len);

//                    sum += len;//前端显示 下载进度
//                    long finalSum = sum;
//                    runOnUiThread(new Runnable() {
//                        @Override
//                        public void run() {
//                            tv.setText(finalSum + "/" + total);
//                        }
//                    });
                }
                fos.flush();//记得try catch
                fos.close();
                is.close();
                Log.d("onResponse", "download success");
            }
        });
    }

    /**
     * post 加载图片
     */
    public void doDownloadImg(View view) {
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
                //注意 bitmap可能会很大，要压缩
                //方法一：下载到本地，进行采样 BitmapFactory.Options

                //方法二：decodeStream里设置采样
                Bitmap bitmap = BitmapFactory.decodeStream(is);
//                is.mark(1000);//这两个不一定能请求成功
//                is.reset();

//                runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        mIv.setImageBitmap(bitmap);
//                    }
//                });
            }
        });
    }

    //追踪进度问题


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
