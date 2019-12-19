package com.imfondof.world;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.imfondof.world.floatingactionmenu.FloatingActionMenuActivity;
import com.imfondof.world.rank.RankActivity;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    Button rank,floatingAtionMenu;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        rank = findViewById(R.id.rank);
        floatingAtionMenu = findViewById(R.id.floatingActionMenu);

        rank.setOnClickListener(this);
        floatingAtionMenu.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rank:
                Intent intent = new Intent(this, RankActivity.class);
                startActivity(intent);
                break;
            case R.id.floatingActionMenu:
                Intent intentFloat = new Intent(this, FloatingActionMenuActivity.class);
                startActivity(intentFloat);
                break;
        }
    }
}
