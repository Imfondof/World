package com.imfondof.world;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.widget.Button;

import com.imfondof.world.other.retrofit.RetrofitActivity;
import com.imfondof.world.floatingactionmenu.FloatingActionMenuActivity;
import com.imfondof.world.other.mvp.TasksActivity;
import com.imfondof.world.other.mvvm.MVVMActivity;
import com.imfondof.world.rank.RankActivity;
import com.imfondof.world.utils.StatusBarUtil;

public class MainActivity extends AppCompatActivity {

    Button btn_rank, btn_float, btn_mvp, btn_mvvm, btn_retrofit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //配合xml的fakeStatusBarView，动态设置字体的颜色true为黑  false为白
        //（此方法应封装为基类的抽象方法，子类继承重写）
        StatusBarUtil.transparentStatusbarAndLayoutInsert(this, true);

        btn_rank = findViewById(R.id.btn_rank);
        setOnClick(this, btn_rank, R.id.btn_rank, RankActivity.class);
        setOnClick(this, btn_float, R.id.btn_float, FloatingActionMenuActivity.class);
        setOnClick(this, btn_mvp, R.id.btn_mvp, TasksActivity.class);
        setOnClick(this, btn_mvvm, R.id.btn_mvvm, MVVMActivity.class);
        setOnClick(this, btn_retrofit, R.id.btn_retrofit, RetrofitActivity.class);
    }

    private void setOnClick(Context context, Button button, int resId, Class aClass) {
        button = findViewById(resId);
        button.setOnClickListener(v -> {
            Intent intentRetrofit = new Intent(context, aClass);
            startActivity(intentRetrofit);
        });
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            // 不退出程序，进入后台
            moveTaskToBack(true);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
