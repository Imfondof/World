package com.imfondof.world.other.net;

import android.text.TextUtils;

import com.imfondof.world.other.http.CountingRequestBody;

import java.io.File;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.Request;
import okhttp3.RequestBody;

/**
 * Imfondof on 2020/4/8
 */
public class OkHttpPutRequest extends OkHttpRequest {
    protected OkHttpPutRequest(String url, Object tag, Map<String, String> params, Map<String, String> headers, MediaType mediaType, String content, byte[] bytes, File file) {
        super(url, tag, params, headers);
        this.mediaType = mediaType;
        this.content = content;
        this.bytes = bytes;
        this.file = file;
    }

    protected void validParams() {
        int count = 0;
        if (params != null && !params.isEmpty()) {
            type = TYPE_PARAMS;
            count++;
        }
        if (content != null) {
            type = TYPE_STRING;
            count++;
        }
        if (bytes != null) {
            type = TYPE_BYTES;
            count++;
        }
        if (file != null) {
            type = TYPE_FILE;
            count++;
        }

        if (count <= 0 || count > 1) {
            throw new IllegalArgumentException("the params , content , file , bytes must has one and only one .");
        }
    }

    @Override
    protected Request buildRequest() {
        if (TextUtils.isEmpty(url)) {
            throw new IllegalArgumentException("url can not be empty!");
        }
        Request.Builder builder = new Request.Builder();
        appendHeaders(builder, headers);
        builder.url(url).tag(tag).put(requestBody);
        return builder.build();
    }

    @Override
    protected RequestBody buildRequestBody() {
        validParams();
        RequestBody requestBody = null;


        switch (type) {
            case TYPE_PARAMS:
                MultipartBody.Builder builder = new MultipartBody.Builder();
                addParams(builder, params);
                requestBody = builder.build();
                break;
            case TYPE_BYTES:
                requestBody = RequestBody.create(mediaType != null ? mediaType : MEDIA_TYPE_STREAM, bytes);
                break;
            case TYPE_FILE:
                requestBody = RequestBody.create(mediaType != null ? mediaType : MEDIA_TYPE_STREAM, file);
                break;
            case TYPE_STRING:
                requestBody = RequestBody.create(mediaType != null ? mediaType : MEDIA_TYPE_STRING, content);
                break;
        }
        return requestBody;
    }

    @Override
    protected RequestBody wrapRequestBody(RequestBody requestBody, final ResultCallback callback) {
        CountingRequestBody countingRequestBody = new CountingRequestBody(requestBody, new CountingRequestBody.Listener() {
            @Override
            public void onRequestProgress(final long bytesWritten, final long contentLength) {
                mOkHttpClientManager.getHandler().post(new Runnable() {
                    @Override
                    public void run() {
                        callback.inProgress(bytesWritten * 1.0f / contentLength);
                    }
                });

            }
        });
        return countingRequestBody;
    }

    private void addParams(MultipartBody.Builder builder, Map<String, String> params) {
        if (builder == null) {
            throw new IllegalArgumentException("builder can not be null .");
        }

        if (params != null && !params.isEmpty()) {
            for (String key : params.keySet()) {
                builder.addFormDataPart(key, params.get(key));
            }
        }
    }
}
