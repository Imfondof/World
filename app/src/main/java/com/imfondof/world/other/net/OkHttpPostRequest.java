package com.imfondof.world.other.net;

import android.text.TextUtils;
import android.util.Log;

import com.imfondof.world.utils.LogUtils;

import java.io.File;
import java.util.Map;

import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.Request;
import okhttp3.RequestBody;

/**
 * Imfondof on 2020/4/8
 */
public class OkHttpPostRequest extends OkHttpRequest {

    protected OkHttpPostRequest(String url, Object tag, Map<String, String> params, Map<String, String> headers, MediaType mediaType, String content, byte[] bytes, File file, Map<String, File> files) {
        super(url, tag, params, headers);
        this.mediaType = mediaType;
        this.content = content;
        this.bytes = bytes;
        this.file = file;
        this.files = files;
    }

    protected OkHttpPostRequest(String url, Object tag, Map<String, String> params, Map<String, String> headers, MediaType mediaType, String content, byte[] bytes, File file, Map<String, File> files, int connectOutTime) {
        super(url, tag, params, headers, connectOutTime);
        this.mediaType = mediaType;
        this.content = content;
        this.bytes = bytes;
        this.file = file;
        this.files = files;
    }

    @Override
    protected Request buildRequest() {
        if (TextUtils.isEmpty(url)) {
            throw new IllegalArgumentException("url can not be empty!");
        }
        Request.Builder builder = new Request.Builder();
        appendHeaders(builder, headers);
        builder.url(url).tag(tag).post(requestBody);
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
            case TYPE_MULTIPART:
                requestBody = buildMultipartFormRequest();
                break;
        }
        return requestBody;
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

    protected void validParams() {
        int count = 0;
        if (params != null && !params.isEmpty() && files != null && !files.isEmpty()) {
            type = TYPE_MULTIPART;
            count++;
        } else {
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
        }

        if (count <= 0 || count > 1) {
            throw new IllegalArgumentException("the params , content , file , bytes must has one and only one .");
        }
    }

    protected void appendHeaders(Request.Builder builder, Map<String, String> headers) {
        if (builder == null) {
            throw new IllegalArgumentException("builder can not be empty!");
        }

        Headers.Builder headerBuilder = new Headers.Builder();
        if (headers == null || headers.isEmpty()) return;

        for (String key : headers.keySet()) {
            headerBuilder.add(key, headers.get(key));
        }
        builder.headers(headerBuilder.build());
    }

    private RequestBody buildMultipartFormRequest() {

        MultipartBody.Builder builder = new MultipartBody.Builder()
                .setType(MultipartBody.FORM);

        if (params != null && !params.isEmpty()) {
            for (String key : params.keySet()) {
                builder.addFormDataPart(key, params.get(key));
            }
        }
        if (files != null && !files.isEmpty()) {
            RequestBody fileBody = null;
            for (String key : files.keySet()) {
                File file = files.get(key);
                String fileName = file.getName();
                fileBody = RequestBody.create(mediaType != null ? mediaType : MEDIA_TYPE_STREAM, file);
                //TODO 根据文件名设置contentType
                builder.addPart(Headers.of("Content-Disposition",
                        "form-data; name=\"" + key + "\"; filename=\"" + fileName + "\""),
                        fileBody);
            }
        }
        return builder.build();
    }
}
