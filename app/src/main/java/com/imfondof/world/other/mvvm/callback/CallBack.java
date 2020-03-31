package com.imfondof.world.other.mvvm.callback;
import com.imfondof.world.other.mvvm.bean.Account;

/**
 * Imfondof on 2020/1/3 10:10
 * description:
 */
public interface CallBack {
    void onSuccess(Account account);

    void onFailed();
}
