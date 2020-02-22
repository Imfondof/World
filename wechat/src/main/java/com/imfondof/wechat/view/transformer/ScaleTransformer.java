package com.imfondof.wechat.view.transformer;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.ViewPager;

public class ScaleTransformer implements ViewPager.PageTransformer {

    private static final float MIN_SCALE = 0.75f;
    private static final float MINE_ALPHA = 0.5f;

    @Override
    public void transformPage(@NonNull View page, float position) {
        //a->b
        //a  position  (0,-1)
        //b  position  (1,0)

        //b->a
        //a  position  (-1,0,)
        //b  position  (0,1)

        //[,-1]
        if (position < -1) {
            page.setScaleX(MIN_SCALE);
            page.setScaleY(MIN_SCALE);
            page.setAlpha(MINE_ALPHA);
        } else if (position <= 1) {
            //[-1,1]
            //左边的页面
            if (position < 0) {
                //a->b positon 0，-1
                //[1,0.75f]
                float scaleA = MIN_SCALE + (1 - MIN_SCALE) * (1 + position);
                page.setScaleX(scaleA);
                page.setScaleY(scaleA);

                float alphaA = MINE_ALPHA + (1 - MINE_ALPHA) * (1 + position);
                page.setAlpha(alphaA);

                //b->a position -1,0
                //[0.75f,1]
            } else {
                //右边的
                //a->b positon 1,0
                //[0.75f.1]
                float scaleB = MIN_SCALE + (1 - MIN_SCALE) * (1 - position);
                page.setScaleX(scaleB);
                page.setScaleY(scaleB);

                float alphaB = MINE_ALPHA + (1 - MINE_ALPHA) * (1 - position);
                page.setAlpha(alphaB);

                //b->a position 0,1
                //[1,0.75f]
            }

        } else {
            //(1,]
            page.setScaleX(MIN_SCALE);
            page.setScaleY(MIN_SCALE);
            page.setAlpha(MINE_ALPHA);
        }
    }
}
