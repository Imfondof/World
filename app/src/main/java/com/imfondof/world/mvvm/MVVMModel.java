package com.imfondof.world.mvvm;

import com.imfondof.world.mvvm.bean.Account;
import com.imfondof.world.mvvm.callback.CallBack;

import java.util.Random;

/**
 * Imfondof on 2020/1/3 10:06
 * description:
 */
public class MVVMModel {
    public void getAccountData(String accountName, CallBack callBack) {
        Random random = new Random();
        boolean isSuccess = random.nextBoolean();
        if (isSuccess) {
            Account account = new Account(accountName,100);
            callBack.onSuccess(account);
        } else {
            callBack.onFailed();
        }
    }
}
