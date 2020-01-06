package com.imfondof.world;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.imfondof.world.retrofit.RetrofitActivity;
import com.imfondof.world.floatingactionmenu.FloatingActionMenuActivity;
import com.imfondof.world.mvp.TasksActivity;
import com.imfondof.world.mvvm.MVVMActivity;
import com.imfondof.world.rank.RankActivity;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    Button rank,btn_float,btn_mvp,btn_mvvm,btn_retrofit;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        rank = findViewById(R.id.rank);
        btn_float = findViewById(R.id.btn_float);
        btn_mvp = findViewById(R.id.btn_mvp);
        btn_mvvm = findViewById(R.id.btn_mvvm);
        btn_retrofit = findViewById(R.id.btn_retrofit);

        rank.setOnClickListener(this);
        btn_float.setOnClickListener(this);
        btn_mvp.setOnClickListener(this);
        btn_mvvm.setOnClickListener(this);
        btn_retrofit.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rank:
                Intent intent = new Intent(this, RankActivity.class);
                startActivity(intent);
                break;
            case R.id.btn_float:
                Intent intentFloat = new Intent(this, FloatingActionMenuActivity.class);
                startActivity(intentFloat);
                break;
            case R.id.btn_mvp:
                Intent intentMvp = new Intent(this, TasksActivity.class);
                startActivity(intentMvp);
                break;
            case R.id.btn_mvvm:
                Intent intentMvvm = new Intent(this, MVVMActivity.class);
                startActivity(intentMvvm);
                break;
            case R.id.btn_retrofit:
                Intent intentRetrofit = new Intent(this, RetrofitActivity.class);
                startActivity(intentRetrofit);
                break;
        }
    }
}
