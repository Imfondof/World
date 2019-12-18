package com.imfondof.world;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.imfondof.world.rank.RankActivity;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    Button rank;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        rank = findViewById(R.id.rank);
        rank.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rank:
                Intent intent = new Intent(this, RankActivity.class);
                startActivity(intent);
                break;
        }
    }
}
