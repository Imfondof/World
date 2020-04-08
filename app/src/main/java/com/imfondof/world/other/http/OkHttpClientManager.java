package com.imfondof.world.other.http;

import android.os.Build;
import android.os.Handler;
import android.os.Looper;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.imfondof.world.other.net.ResultCallback;
import com.imfondof.world.utils.LogUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Modifier;
import java.net.FileNameMap;
import java.net.URLConnection;
import java.util.Collections;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Protocol;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Imfondof on 2020/1/9 15:57
 * <p>
 * OkHttp的封装类:https://blog.csdn.net/lmj623565791/article/details/47911083
 */
public class OkHttpClientManager {
    private static final String TAG = "NetFactory_OkHM";
    public static final int DEF_TIME_OUT = 30;
    private static OkHttpClientManager mInstance;
    private OkHttpClient mOkHttpClient;
    private OkHttpClient mOKGetHttpClient;
    private Handler mHandler;
    private Gson mGson;

    private OkHttpClientManager() {
        mOkHttpClient = new OkHttpClient.Builder()//添加cookie
                .retryOnConnectionFailure(false)
                .protocols(Collections.singletonList(Protocol.HTTP_1_1))
                .readTimeout(DEF_TIME_OUT, TimeUnit.SECONDS)
                .writeTimeout(DEF_TIME_OUT, TimeUnit.SECONDS)
                .build();
        mHandler = new Handler(Looper.getMainLooper());

        OkHttpClient.Builder builderGet = new OkHttpClient.Builder().retryOnConnectionFailure(true)
                .readTimeout(DEF_TIME_OUT, TimeUnit.SECONDS)
                .connectTimeout(DEF_TIME_OUT, TimeUnit.SECONDS)
                .writeTimeout(DEF_TIME_OUT, TimeUnit.SECONDS);

        mOKGetHttpClient = builderGet.build();
        int sdk = Build.VERSION.SDK_INT;
        if (sdk >= 23) {
            GsonBuilder gsonBuilder = new GsonBuilder().excludeFieldsWithModifiers(
                    Modifier.FINAL,
                    Modifier.TRANSIENT,
                    Modifier.STATIC);
            mGson = gsonBuilder.create();
        } else {
            mGson = new Gson();
        }
    }

    public Handler getHandler() {
        return mHandler;
    }

    /**
     * 普通请求
     *
     * @return
     */
    public OkHttpClient getOkHttpClient() {
        return mOkHttpClient;
    }

    /**
     * get请求
     *
     * @return
     */
    public OkHttpClient getOkHttpGetClient() {
        return mOKGetHttpClient;
    }

    /**
     * 异步请求
     *
     * @param request
     * @param callback
     */
    public void execute(final Request request, ResultCallback callback) {
        if (callback == null) callback = ResultCallback.DEFAULT_RESULT_CALLBACK;
        final ResultCallback resCallBack = callback;
        if ("GET".equals(request.method())) {
            mOKGetHttpClient.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(final Call call, final IOException e) {
                    //TODO when cancel , should do?
                    LogUtils.d(TAG, "" + e.toString());
                    sendFailResultCallback(call.request(), e, resCallBack);
                }

                @Override
                public void onResponse(Call call, final Response response) {
                    if (response.code() >= 400 && response.code() <= 599) {
                        LogUtils.d(TAG, "" + response);
                        try {
                            sendFailResultCallback(request, new RuntimeException(response.body().string()), resCallBack);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        return;
                    }

                    try {
                        final String string = response.body().string();
                        if (resCallBack.mType == String.class) {
                            sendSuccessResultCallback(string, resCallBack);
                        } else {
                            Object o = mGson.fromJson(string, resCallBack.mType);
                            sendSuccessResultCallback(o, resCallBack);
                        }
                    } catch (Exception e) {
                        sendFailResultCallback(response.request(), e, resCallBack);
                    } catch (OutOfMemoryError e) {
                        sendSuccessResultCallback("", resCallBack);
                    }
                }
            });
        } else {
            mOkHttpClient.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(final Call call, final IOException e) {
                    //TODO when cancel , should do?
                    sendFailResultCallback(call.request(), e, resCallBack);
                }

                @Override
                public void onResponse(Call call, final Response response) {
                    if (response.code() >= 400 && response.code() <= 599) {
                        try {
                            sendFailResultCallback(request, new RuntimeException(call.request().url().toString() + "\n" + response.body().string()), resCallBack);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        return;
                    }

                    try {
                        final String string = response.body().string();
                        if (resCallBack.mType == String.class) {
                            sendSuccessResultCallback(string, resCallBack);
                        } else {
                            Object o = mGson.fromJson(string, resCallBack.mType);
                            sendSuccessResultCallback(o, resCallBack);
                        }
                    } catch (Exception e) {
                        sendFailResultCallback(response.request(), e, resCallBack);
                    } catch (OutOfMemoryError e) {
                        sendSuccessResultCallback("", resCallBack);
                    }
                }
            });
        }
    }

    public void sendFailResultCallback(final Request request, final Exception e, final ResultCallback callback) {
        if (callback == null) return;

        mHandler.post(new Runnable() {
            @Override
            public void run() {
                callback.onError(request, e);
            }
        });
    }


    //***********************************************************************
    public static OkHttpClientManager getInstance() {
        if (mInstance == null) {
            synchronized (OkHttpClientManager.class) {
                if (mInstance == null) {
                    mInstance = new OkHttpClientManager();
                }
            }
        }
        return mInstance;
    }

    /**
     * 同步的get
     */
    private Response _get(String url) throws IOException {
        final Request request = new Request.Builder().url(url).build();
        Call call = mOkHttpClient.newCall(request);
        Response response = call.execute();
        return response;
    }

    /**
     * 同步的get
     */
    private String _getAsString(String url) throws IOException {
        Response response = _get(url);
        return response.body().string();
    }

    /**
     * 异步的get
     */
    private void _getAsy(String url, final ResultCallback callback) {
        final Request request = new Request.Builder().url(url).build();
        deliveryResult(request, callback);
    }


    /**
     * 异步的post请求
     *
     * @param url
     * @param callback
     * @param params
     */
    private void _postAsyn(String url, final ResultCallback callback, Param... params) {
        Request request = buildPostRequest(url, params);
        deliveryResult(request, callback);
    }

    /**
     * 同步的post 请求
     *
     * @param url
     * @param params
     */
    private Response _post(String url, Param... params) throws IOException {
        Request request = buildPostRequest(url, params);
        Response response = mOkHttpClient.newCall(request).execute();
        return response;
    }

    /**
     * 同步的post 请求
     *
     * @param url
     * @param params
     */
    private String _postAsString(String url, Param... params) throws IOException {
        Response response = _post(url, params);
        return response.body().string();
    }

    /**
     * 异步的post请求
     *
     * @param url
     * @param callback
     * @param parms
     */
    private void _postAsyn(String url, final ResultCallback callback, Map<String, String> parms) {
        Param[] paramsArr = map2Params(parms);
        Request request = buildPostRequest(url, paramsArr);
        deliveryResult(request, callback);
    }

    /**
     * 异步的post请求
     *
     * @param url
     * @param callback
     * @param parms
     */
    private void _postAsyn2(String url, final ResultCallback callback, Param... parms) {
        Request request = buildPostRequest(url, parms);
        deliveryResult(request, callback);
    }

    /**
     * 同步基于post的文件上传
     *
     * @param params
     * @return
     */
    private Response _post(String url, File[] files, String[] fileKeys, Param... params) throws IOException {
        Request request = buildMultipartFormRequest(url, files, fileKeys, params);
        return mOkHttpClient.newCall(request).execute();
    }

    private Response _post(String url, File file, String fileKey) throws IOException {
        Request request = buildMultipartFormRequest(url, new File[]{file}, new String[]{fileKey}, null);
        return mOkHttpClient.newCall(request).execute();
    }

    private Response _post(String url, File file, String fileKey, Param... params) throws IOException {
        Request request = buildMultipartFormRequest(url, new File[]{file}, new String[]{fileKey}, params);
        return mOkHttpClient.newCall(request).execute();
    }

    /**
     * 异步下载文件
     *
     * @param url
     * @param destFileDir 本地文件存储的文件夹
     * @param callback
     */
    private void _downloadAsyn(final String url, final String destFileDir, final ResultCallback callback) {
        final Request request = new Request.Builder()
                .url(url)
                .build();
        final Call call = mOkHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                sendFailedStringCallback(request, e, callback);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                InputStream is = null;
                byte[] buf = new byte[2048];
                int len = 0;
                FileOutputStream fos = null;
                try {
                    is = response.body().byteStream();
                    File file = new File(destFileDir, getFileName(url));
                    fos = new FileOutputStream(file);
                    while ((len = is.read(buf)) != -1) {
                        fos.write(buf, 0, len);
                    }
                    fos.flush();
                    //如果下载文件成功，第一个参数为文件的绝对路径
                    sendSuccessResultCallback(file.getAbsolutePath(), callback);
                } catch (IOException e) {
                    sendFailedStringCallback(response.request(), e, callback);
                } finally {
                    try {
                        if (is != null) is.close();
                    } catch (IOException e) {
                    }
                    try {
                        if (fos != null) fos.close();
                    } catch (IOException e) {
                    }
                }
            }
        });
    }

    private void sendFailedStringCallback(final Request request, final Exception e, final ResultCallback callback) {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                if (callback != null)
                    callback.onError(request, e);
            }
        });
    }

    private String getFileName(String path) {
        int separatorIndex = path.lastIndexOf("/");
        return (separatorIndex < 0) ? path : path.substring(separatorIndex + 1, path.length());
    }

    private Request buildMultipartFormRequest(String url, File[] files,
                                              String[] fileKeys, Param[] params) {
        params = validateParam(params);

        MultipartBody.Builder builder = new MultipartBody.Builder();
        //千万记得设置MediaType

        for (Param param : params) {
            builder.addPart(Headers.of("Content-Disposition", "form-data; name=\"" + param.key + "\""),
                    RequestBody.create(null, param.value));
        }
        if (files != null) {
            RequestBody fileBody = null;
            for (int i = 0; i < files.length; i++) {
                File file = files[i];
                String fileName = file.getName();
                fileBody = RequestBody.create(MediaType.parse(guessMimeType(fileName)), file);
                //TODO 根据文件名设置contentType
                builder.addPart(Headers.of("Content-Disposition",
                        "form-data; name=\"" + fileKeys[i] + "\"; filename=\"" + fileName + "\""),
                        fileBody);
            }
        }

        RequestBody requestBody = builder.build();
        return new Request.Builder()
                .url(url)
                .post(requestBody)
                .build();
    }

    private String guessMimeType(String path) {
        FileNameMap fileNameMap = URLConnection.getFileNameMap();
        String contentTypeFor = fileNameMap.getContentTypeFor(path);
        if (contentTypeFor == null) {
            contentTypeFor = "application/octet-stream";
        }
        return contentTypeFor;
    }

    private Param[] validateParam(Param[] params) {
        if (params == null)
            return new Param[0];
        else return params;
    }


    private Request buildPostRequest(String url, Param[] params) {
        if (params == null) {
            params = new Param[0];
        }
        FormBody.Builder body = new FormBody.Builder();
        for (Param param : params) {
            body.add(param.key, param.value);
        }
        RequestBody requestBody = body.build();
        return new Request.Builder()
                .url(url)
                .post(requestBody)
                .build();
    }

//    public static abstract class ResultCallback<T> {
//        Type mType;
//
//        public ResultCallback() {
//            mType = getSuperclassTypeParameter(getClass());
//        }
//
//        static Type getSuperclassTypeParameter(Class<?> subclass) {
//            Type superclass = subclass.getGenericSuperclass();
//            if (superclass instanceof Class) {
//                throw new RuntimeException("Missing type parameter.");
//            }
//            ParameterizedType parameterized = (ParameterizedType) superclass;
//            return $Gson$Types.canonicalize(parameterized.getActualTypeArguments()[0]);
//        }
//
//        public abstract void onError(Request request, Exception e);
//
//        public abstract void onResponse(T response);
//    }

    public static class Param {
        String key;
        String value;

        public Param() {
        }

        public Param(String key, String value) {
            this.key = key;
            this.value = value;
        }
    }

    private Param[] map2Params(Map<String, String> parms) {
        if (parms == null) {
            return new Param[0];
        }
        int size = parms.size();
        Param[] res = new Param[size];
        Set<Map.Entry<String, String>> entries = parms.entrySet();
        int i = 0;
        for (Map.Entry<String, String> entry : entries) {
            res[i++] = new Param(entry.getKey(), entry.getValue());
        }
        return res;
    }

    private void deliveryResult(Request request, final ResultCallback callback) {
        mOkHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                sendFailedStringCallBack(request, e, callback);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String str = response.body().string();
                if (callback.mType == String.class) {
                    sendSuccessResultCallback(str, callback);
                } else {
                    Object o = mGson.fromJson(str, callback.mType);
                    sendSuccessResultCallback(o, callback);
                }
            }
        });
    }

    private void sendSuccessResultCallback(final Object object, ResultCallback callback) {
        mHandler.post(() -> {
            if (callback != null) {
                LogUtils.d(TAG, object.toString());
                callback.onResponse(object);
            }
        });
    }

    private void sendFailedStringCallBack(Request request, IOException e, ResultCallback callback) {
        mHandler.post(() -> {
            if (callback != null) {
                callback.onError(request, e);
            }
        });
    }

    //对外暴露的方法
    //异步get
    public static Response getAsyn(String url) throws IOException {
        return getInstance()._get(url);
    }

    //异步get  String
    public static String getAsString(String url) throws IOException {
        return getInstance()._getAsString(url);
    }

    //异步get  有回调
    public static void getAsyn(String url, ResultCallback callback) {
        getInstance()._getAsy(url, callback);
    }

    //异步post 有回调
    public static void postAsyn(String url, final ResultCallback callback, Param... params) {
        getInstance()._postAsyn(url, callback, params);
    }

    public static Response post(String url, Param... params) throws IOException {
        return getInstance()._post(url, params);
    }

    public static String postAsString(String url, Param... params) throws IOException {
        return getInstance()._postAsString(url, params);
    }

    public static void postAsyn(String url, final ResultCallback callback, Map<String, String> params) {
        getInstance()._postAsyn(url, callback, params);
    }


    public static Response post(String url, File[] files, String[] fileKeys, Param... params) throws IOException {
        return getInstance()._post(url, files, fileKeys, params);
    }

    public static Response post(String url, File file, String fileKey) throws IOException {
        return getInstance()._post(url, file, fileKey);
    }

    public static Response post(String url, File file, String fileKey, Param... params) throws IOException {
        return getInstance()._post(url, file, fileKey, params);
    }

    public static void downloadAsyn(String url, String destDir, ResultCallback callback) {
        getInstance()._downloadAsyn(url, destDir, callback);
    }
}
