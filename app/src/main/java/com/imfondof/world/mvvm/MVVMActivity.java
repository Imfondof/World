package com.imfondof.world.mvvm;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.os.Bundle;

import com.imfondof.world.R;
import com.imfondof.world.databinding.ActivityMvvmBinding;

public class MVVMActivity extends AppCompatActivity {

    private ActivityMvvmBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_mvvm);
        MVVMViewModel mvvmViewModel = new MVVMViewModel(getApplication());
        binding.setViewmedel(mvvmViewModel);
    }
}
