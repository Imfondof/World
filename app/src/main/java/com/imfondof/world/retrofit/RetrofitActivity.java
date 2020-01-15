package com.imfondof.world.retrofit;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.imfondof.world.R;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

//retrofit框架练习
public class RetrofitActivity extends AppCompatActivity {

    private String BASE_URL = "https://www.wanandroid.com/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rxjava);
    }

    public void onAction(View view) {
        //创建Retrofit对象
        Retrofit retrofit = new Retrofit.Builder().baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())// 设置数据解析器
                .build();

        //获取UserMgService
        UserMgService userMgService = retrofit.create(UserMgService.class);

        //调用登录 login方法
        Call<UserInfoModel> call = userMgService.login("woaigeny", "woaigeny123");

        //发送同步请求(需要自己代码开启子线程)
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                Response<UserInfoModel> response = null;
//                try {
//                    response = call.execute();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//                Log.d("onAction", "onAction: " + response.body().code);
//            }
//        }).start();

        //发送异步请求
        call.enqueue(new Callback<UserInfoModel>() {
            @Override
            public void onResponse(Call<UserInfoModel> call, Response<UserInfoModel> response) {
                Log.d("onAction", "onAction: " + response.body().data.getUsername());
            }

            @Override
            public void onFailure(Call<UserInfoModel> call, Throwable t) {

            }
        });
    }
}
