package com.imfondof.world.utils;

import android.animation.ValueAnimator;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;

import com.nineoldandroids.animation.Animator;
import com.nineoldandroids.animation.AnimatorListenerAdapter;
import com.nineoldandroids.animation.ObjectAnimator;

/**
 * Imfondof on 2020/1/15 14:32
 */
public class AnimUtil {

    /**
     * 上下跳动的动画
     *
     * @param view
     * @param times 动画的次数（-1代表无数次 0代表不显示动画）
     */
    public static void floatAnim(View view, int times) {
        ObjectAnimator translationY = ObjectAnimator.ofFloat(view, "translationY", 0, 30f, 0);
        translationY.setDuration(1500);
        translationY.setRepeatCount(times);
        translationY.setRepeatMode(ValueAnimator.RESTART);
        translationY.setInterpolator(new AccelerateDecelerateInterpolator());
        translationY.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
            }

            @Override
            public void onAnimationStart(Animator animation) {
                super.onAnimationStart(animation);
            }
        });
        translationY.start();
    }
}
