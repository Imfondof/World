package com.imfondof.world.other.net;

/**
 * Imfondof on 2020/4/8
 */
public interface BaseCallback<T> {
    int ERROR = -99;//系统错误
    int NULL = -100;//无返回数据

    void requestSucceed(T result) throws Exception;

    void requestFailed(int errorCode, Object error);
}
