package com.imfondof.world.http;

import android.os.Handler;
import android.os.Looper;

import com.google.gson.Gson;
import com.google.gson.internal.$Gson$Types;

import java.io.IOException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Map;
import java.util.Set;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * zhaishuo on 2020/1/9 15:57
 * description:
 */
public class OkHttpClientManager {
    private static OkHttpClientManager mInstance;
    private OkHttpClient mOkHttpClient;
    private Handler mHandler;
    private Gson mGson;

    private OkHttpClientManager() {
        mOkHttpClient = new OkHttpClient();
        //cookie
        mHandler = new Handler(Looper.getMainLooper());
        mGson = new Gson();
    }

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
     * 异步的get
     *
     */


    /**
     * 异步的post请求
     *
     * @param url
     * @param callback
     * @param params
     */
    private void postAsyn(String url, final ResultCallback callback, Param... params) {
        Request request = buildPostRequest(url, params);
        deliveryResult(request, callback);
    }

    /**
     * 异步的post请求
     *
     * @param url
     * @param callback
     * @param parms
     */
    private void postAsyn2(String url, final ResultCallback callback, Map<String, String> parms) {
        Param[] paramsArr = map2Params(parms);
        Request request = buildPostRequest(url, paramsArr);
        deliveryResult(request, callback);
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

    public static abstract class ResultCallback<T> {
        Type mType;

        public ResultCallback() {
            mType = getSuperclassTypeParameter(getClass());
        }

        static Type getSuperclassTypeParameter(Class<?> subclass) {
            Type superclass = subclass.getGenericSuperclass();
            if (superclass instanceof Class) {
                throw new RuntimeException("Missing type parameter.");
            }
            ParameterizedType parameterized = (ParameterizedType) superclass;
            return $Gson$Types.canonicalize(parameterized.getActualTypeArguments()[0]);
        }

        public abstract void onError(Request request, Exception e);

        public abstract void onResponse(T response);
    }

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
}
