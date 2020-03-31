package com.imfondof.world.other.mvvm;

import android.app.Application;
import android.view.View;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;
import androidx.databinding.library.baseAdapters.BR;

import com.imfondof.world.other.mvvm.bean.Account;
import com.imfondof.world.other.mvvm.callback.CallBack;

/**
 * Imfondof on 2020/1/3 10:06
 * description:
 */
public class MVVMViewModel extends BaseObservable {
    MVVMModel mvvmModel;
    private String userInput;   //获取用户输入
    private String result;      //显示结果

    /**
     * 一般传入application对象，方便在viewmodel使用（比如sharedpreferences）
     * @param application
     */
    public MVVMViewModel(Application application) {
        mvvmModel = new MVVMModel();
    }

    public void getData(View view) {
        mvvmModel.getAccountData(userInput, new CallBack() {

            @Override
            public void onSuccess(Account account) {
                String info = account.getName() + "|" + account.getLevel();
                setResult(info);
            }

            @Override
            public void onFailed() {
                setResult("shibai");
            }
        });
    }

    @Bindable
    public String getUserInput() {
        return userInput;
    }

    public void setUserInput(String userInput) {
        this.userInput = userInput;
        notifyPropertyChanged(BR.userInput);
    }

    @Bindable
    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
        notifyPropertyChanged(BR.result);
    }
}
