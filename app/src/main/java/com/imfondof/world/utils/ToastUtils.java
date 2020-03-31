package com.imfondof.world.utils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.imfondof.world.R;

/**
 * Imfondof on 2020/3/31
 * 自定义的toast，80%黑色背景
 */
public class ToastUtils {
    public static void showMsg(Context context, String str, int duration, int gravity, int xOffset, int yOffset) {
        Toast toast = new Toast(context);
        View layout = LayoutInflater.from(context).inflate(R.layout.toast, null);
        TextView textView = layout.findViewById(R.id.toast_tv);
        textView.setText(str);
        toast.setView(layout);
        toast.setDuration(duration);
        toast.setGravity(gravity, xOffset, yOffset);
        toast.show();
    }
}
