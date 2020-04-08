package com.imfondof.world.other.net;

import java.lang.reflect.Type;

import okhttp3.Request;

/**
 * Imfondof on 2020/4/8
 */
public abstract class ResultCallback<T> {
    public Type mType;

    public abstract void onError(Request request, Exception e);

    public abstract void onResponse(T response);

    public void inProgress(float progress) {
    }

    public static final ResultCallback<String> DEFAULT_RESULT_CALLBACK = new ResultCallback<String>() {
        @Override
        public void onError(Request request, Exception e) {

        }

        @Override
        public void onResponse(String response) {

        }
    };
}
