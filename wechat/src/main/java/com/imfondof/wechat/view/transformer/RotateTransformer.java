package com.imfondof.wechat.view.transformer;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.ViewPager;

import com.imfondof.wechat.util.L;

public class RotateTransformer implements ViewPager.PageTransformer {

    private static final int MAX_ROTATE = 15;

    @Override
    public void transformPage(@NonNull View page, float position) {
        L.d("zh++"+position);
        //a->b
        //a  position  (0,-1)
        //b  position  (1,0)

        //b->a
        //a  position  (-1,0,)
        //b  position  (0,1)

        //[,-1]
        if (position < -1) {
            page.setRotation(-MAX_ROTATE);
            page.setPivotY(page.getHeight());
            page.setPivotX(page.getWidth());
        } else if (position <= 1) {
            //[-1,1]
            //左边的页面
            if (position < 0) {
                page.setPivotY(page.getHeight());
                page.setPivotX(0.5f * page.getWidth() * (1 - position));
                page.setRotation(MAX_ROTATE * position);
            } else {
                page.setPivotY(page.getHeight());
                page.setPivotX(0.5f * page.getWidth() * (1 - position));
                page.setRotation(MAX_ROTATE * position);
            }
        } else {
            //(1,]
            page.setRotation(MAX_ROTATE);
            page.setPivotY(page.getHeight());
            page.setPivotX(0);
        }
    }
}
