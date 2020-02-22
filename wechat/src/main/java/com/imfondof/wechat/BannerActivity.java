package com.imfondof.wechat;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.imfondof.wechat.view.transformer.RotateTransformer;

public class BannerActivity extends AppCompatActivity {
    private ViewPager mVpMain;
    private int[] mResIds = new int[]{
            0xffff0000,
            0xff00ff00,
            0xff0000ff,
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_banner);

        mVpMain = findViewById(R.id.vp_main);
        mVpMain.setOffscreenPageLimit(mResIds.length);
        mVpMain.setPageMargin(40);//设置了setPageTransformer，就不需要设置间隔了
        //如果只有banner，没有必要去新建fragment，只需要给它单击事件就好了
        mVpMain.setAdapter(new PagerAdapter() {
            @Override
            public int getCount() {
                return mResIds.length;
            }

            @Override
            public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
                return view == object;
            }

            @NonNull
            @Override
            public Object instantiateItem(@NonNull ViewGroup container, int position) {
                View view = new View(container.getContext());
                view.setBackgroundColor(mResIds[position]);
                container.addView(view);
                return view;
            }

            @Override
            public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
                container.removeView((View) object);
            }
        });

        mVpMain.setPageTransformer(true,new RotateTransformer());
    }
}
