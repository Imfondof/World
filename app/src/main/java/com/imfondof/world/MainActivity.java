package com.imfondof.world;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import com.imfondof.world.retrofit.RetrofitActivity;
import com.imfondof.world.floatingactionmenu.FloatingActionMenuActivity;
import com.imfondof.world.mvp.TasksActivity;
import com.imfondof.world.mvvm.MVVMActivity;
import com.imfondof.world.rank.RankActivity;

public class MainActivity extends AppCompatActivity {

    Button btn_rank, btn_float, btn_mvp, btn_mvvm, btn_retrofit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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
}
