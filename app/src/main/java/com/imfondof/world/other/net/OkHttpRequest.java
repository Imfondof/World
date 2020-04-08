package com.imfondof.world.other.net;

import android.widget.ImageView;

import com.imfondof.world.other.http.OkHttpClientManager;

import java.io.File;
import java.util.IdentityHashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

/**
 * Imfondof on 2020/4/8
 */
public abstract class OkHttpRequest {
    protected String content;
    protected byte[] bytes;
    protected File file;
    protected Map<String, File> files;
    protected MediaType mediaType;
    protected int type = 0;
    protected static final int TYPE_PARAMS = 1;
    protected static final int TYPE_STRING = 2;
    protected static final int TYPE_BYTES = 3;
    protected static final int TYPE_FILE = 4;
    protected static final int TYPE_MULTIPART = 5;
    protected final MediaType MEDIA_TYPE_STREAM = MediaType.parse("application/octet-stream;charset=utf-8");
    protected final MediaType MEDIA_TYPE_STRING = MediaType.parse("text/plain;charset=utf-8");


    protected OkHttpClientManager mOkHttpClientManager = OkHttpClientManager.getInstance();
    protected OkHttpClient mOkHttpClient;
    protected OkHttpClient mOkGetHttpClient;
    protected RequestBody requestBody;
    protected Request request;
    protected String url;
    protected Object tag;
    protected Map<String, String> params;
    protected Map<String, String> headers;

    protected OkHttpRequest(String url, Object tag, Map<String, String> params, Map<String, String> headers) {
        this(url, tag, params, headers, OkHttpClientManager.DEF_TIME_OUT);
    }

    protected OkHttpRequest(String url, Object tag, Map<String, String> params, Map<String, String> headers, int connectOutTime) {
        mOkHttpClient = mOkHttpClientManager.getOkHttpClient();
        this.url = url;
        this.tag = tag;
        this.params = params;
        this.headers = headers;
        if (mOkHttpClient.readTimeoutMillis() != connectOutTime) {
            mOkHttpClient = mOkHttpClient.newBuilder().readTimeout(connectOutTime, TimeUnit.SECONDS)
                    .connectTimeout(connectOutTime, TimeUnit.SECONDS)
                    .writeTimeout(connectOutTime, TimeUnit.SECONDS).build();
        }
    }

    protected OkHttpRequest(String url, Object tag, Map<String, String> params, Map<String, String> headers, String requestType) {
        this(url, tag, params, headers, OkHttpClientManager.DEF_TIME_OUT, requestType);
    }

    protected OkHttpRequest(String url, Object tag, Map<String, String> params, Map<String, String> headers, int connectOutTime, String requestType) {
        this.url = url;
        this.tag = tag;
        this.params = params;
        this.headers = headers;
        if ("GET".equals(requestType)) {
            mOkGetHttpClient = mOkHttpClientManager.getOkHttpGetClient();
            if (mOkGetHttpClient.readTimeoutMillis() != connectOutTime) {
                mOkGetHttpClient = mOkGetHttpClient.newBuilder().readTimeout(connectOutTime, TimeUnit.SECONDS)
                        .connectTimeout(connectOutTime, TimeUnit.SECONDS)
                        .writeTimeout(connectOutTime, TimeUnit.SECONDS).build();
            }
        } else {
            mOkHttpClient = mOkHttpClientManager.getOkHttpClient();
            if (mOkHttpClient.readTimeoutMillis() != connectOutTime) {
                mOkHttpClient = mOkHttpClient.newBuilder().readTimeout(connectOutTime, TimeUnit.SECONDS)
                        .connectTimeout(connectOutTime, TimeUnit.SECONDS)
                        .writeTimeout(connectOutTime, TimeUnit.SECONDS).build();
            }
        }
    }

    protected abstract Request buildRequest();

    protected abstract RequestBody buildRequestBody();

    public OkHttpRequest get(ResultCallback callback) {
        OkHttpRequest request = new OkHttpGetRequest(url, tag, params, headers);
        request.invokeAsyn(callback);
        return request;
    }

    public void invokeAsyn(ResultCallback callback) {
        prepareInvoked(callback);
        mOkHttpClientManager.execute(request, callback);
    }

    protected void prepareInvoked(ResultCallback callback) {
        requestBody = buildRequestBody();
        requestBody = wrapRequestBody(requestBody, callback);
        request = buildRequest();
    }

    protected RequestBody wrapRequestBody(RequestBody requestBody, final ResultCallback callback) {
        return requestBody;
    }

    protected void appendHeaders(Request.Builder builder, Map<String, String> headers)
    {
        if (builder == null)
        {
            throw new IllegalArgumentException("builder can not be empty!");
        }

        Headers.Builder headerBuilder = new Headers.Builder();
        if (headers == null || headers.isEmpty()) return;

        for (String key : headers.keySet())
        {
            headerBuilder.add(key, headers.get(key));
        }
        builder.headers(headerBuilder.build());
    }
    public static class Builder {
        private String url;
        private Object tag;
        private Map<String, String> headers;
        private Map<String, String> params;
        private Map<String, File> files;
        private MediaType mediaType;

        private String destFileDir;
        private String destFileName;

        private ImageView imageView;
        private int errorResId = -1;

        //for post
        private String content;
        private byte[] bytes;
        private File file;

        public Builder url(String url) {
            this.url = url;
            return this;
        }

        public Builder tag(Object tag) {
            this.tag = tag;
            return this;
        }

        public Builder params(Map<String, String> params) {
            this.params = params;
            return this;
        }

        public Builder addParams(String key, String val) {
            if (this.params == null) {
                params = new IdentityHashMap<>();
            }
            params.put(key, val);
            return this;
        }

        public Builder headers(Map<String, String> headers) {
            this.headers = headers;
            return this;
        }

        public Builder addHeader(String key, String val) {
            if (this.headers == null) {
                headers = new IdentityHashMap<>();
            }
            headers.put(key, val);
            return this;
        }


        public Builder files(Map<String, File> files) {
            this.files = files;
            return this;
        }

        public Builder file(File file) {
            this.file = file;
            return this;
        }

        public Builder destFileName(String destFileName) {
            this.destFileName = destFileName;
            return this;
        }

        public Builder destFileDir(String destFileDir) {
            this.destFileDir = destFileDir;
            return this;
        }


        public Builder imageView(ImageView imageView) {
            this.imageView = imageView;
            return this;
        }

        public Builder errResId(int errorResId) {
            this.errorResId = errorResId;
            return this;
        }

        public Builder content(String content) {
            this.content = content;
            return this;
        }

        public Builder mediaType(MediaType mediaType) {
            this.mediaType = mediaType;
            return this;
        }

//        public <T> T get(Class<T> clazz) throws IOException {
//            OkHttpRequest request = new OkHttpGetRequest(url, tag, params, headers);
//            return request.invoke(clazz);
//        }
//
        public OkHttpRequest get(ResultCallback callback) {
            OkHttpRequest request = new OkHttpGetRequest(url, tag, params, headers);
            request.invokeAsyn(callback);
            return request;
        }
//
//
//        public <T> T post(Class<T> clazz) throws IOException {
//            OkHttpRequest request = new OkHttpPostRequest(url, tag, params, headers, mediaType, content, bytes, file, files);
//            return request.invoke(clazz);
//        }
//
        public OkHttpRequest post(ResultCallback callback) {
            OkHttpRequest request = new OkHttpPostRequest(url, tag, params, headers, mediaType, content, bytes, file, files);
            request.invokeAsyn(callback);
            return request;
        }

        public OkHttpRequest post(ResultCallback callback, int connectOutTime) {
            OkHttpRequest request = new OkHttpPostRequest(url, tag, params, headers, mediaType, content, bytes, file, files, connectOutTime);
            request.invokeAsyn(callback);
            return request;
        }
//
//        public <T> T put(Class<T> clazz) throws IOException {
//            OkHttpRequest request = new OkHttpPutRequest(url, tag, params, headers, mediaType, content, bytes, file);
//            return request.invoke(clazz);
//        }
//
        public OkHttpRequest put(ResultCallback callback) {
            OkHttpRequest request = new OkHttpPutRequest(url, tag, params, headers, mediaType, content, bytes, file);
            request.invokeAsyn(callback);
            return request;
        }

//        public OkHttpRequest download(ResultCallback callback) {
//            OkHttpRequest request = new OkHttpDownloadRequest(url, tag, params, headers, destFileName, destFileDir);
//            request.invokeAsyn(callback);
//            return request;
//        }
//
//        public String download() throws IOException {
//            OkHttpRequest request = new OkHttpDownloadRequest(url, tag, params, headers, destFileName, destFileDir);
//            return request.invoke(String.class);
//        }
    }
}
