package com.imfondof.world.other.net;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;

import com.imfondof.world.utils.LogUtils;

import okhttp3.MediaType;
import okhttp3.Request;

/**
 * Imfondof on 2020/4/8
 */
public class NetFactory {
    private static final String TAG = NetFactory.class.getSimpleName();
    private static NetFactory instance = new NetFactory();
    private static Handler mHandler;

    public static NetFactory getInstance() {
        mHandler = new Handler(Looper.getMainLooper());
        return instance;
    }

    public <T> void get(Context context, BaseRequestCfg request, final BaseCallback<T> callback) {
        request(context, HttpType.HTTP_GET, request, callback);
    }

    public <T> void post(Context context, BaseRequestCfg request, final BaseCallback<T> callback) {
        request(context, HttpType.HTTP_POST, request, callback);
    }

    public <T> void put(Context context, BaseRequestCfg request, final BaseCallback<T> callback) {
        request(context, HttpType.HTTP_PUT, request, callback);
    }

    private <T> void request(final Context context, HttpType httpType, final BaseRequestCfg request, final BaseCallback<T> callback) {
        if (context == null || TextUtils.isEmpty(request.targetUrl)) {
            return;
        }

        LogUtils.d(TAG, "" + request.targetUrl);
        final String json = GsonFactory.getInstance().toJson(request);

        request.startTime = System.currentTimeMillis();
        OkHttpRequest.Builder builder = new OkHttpRequest.Builder().url(request.targetUrl)
                .mediaType(MediaType.parse("application/json; charset=utf-8"))
                .tag(request.targetUrl);

        ResultCallback resultCallback = new ResultCallback<String>() {
            @Override
            public void onError(Request request, Exception e) {
                if (callback != null) {
                    LogUtils.d(TAG, "callback != null" + BaseCallback.ERROR + " " + e.toString());
                    callback.requestFailed(BaseCallback.ERROR, e);
                }
            }

            @Override
            public void onResponse(String response) {
                if (callback != null) {
                    LogUtils.d(TAG, "onResponse callback != null" + response);
                    mHandler.post(() -> {
                        try {
                            callback.requestSucceed((T) response);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    });
                }
            }
        };

        if (HttpType.HTTP_GET == httpType) {
            builder.get(resultCallback);
        } else if (HttpType.HTTP_POST == httpType) {
            builder.content(json);
            builder.post(resultCallback, request.connectOutTime);
        } else if (HttpType.HTTP_PUT == httpType) {
            builder.content(json);
            builder.put(resultCallback);
        }
    }

    public <T> void processResponse(final Context context, final String response, final Class<T> reqClass,
                                    final BaseCallback<T> callback, final BaseRequestCfg request) throws Exception {
        if (TextUtils.isEmpty(response)) {
            callback.requestFailed(BaseCallback.ERROR, "");
        } else {
            final T requestObject = GsonFactory.getInstance().parseJson(context, response, reqClass, request.needAES, request.targetUrl);
            mHandler.post(() -> {
                if (requestObject instanceof BaseResult) {
                    BaseResult result = (BaseResult) requestObject;
                    if (result.code != 0 && !TextUtils.isEmpty(result.msg)) {
                        if (callback != null) {
                            callback.requestFailed(result.code, result.msg);
                        }
                        return;
                    }
                    request.endTime = System.currentTimeMillis();
                }
            });
        }
    }
}
