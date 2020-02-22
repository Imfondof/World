package com.imfondof.wechat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.imfondof.wechat.fragment.TabFragment;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private ViewPager mVpMain;
    private List<String> mTitles = new ArrayList<>(Arrays.asList("微信", "通讯录", "发现", "我"));
    private Button mBtnWechat, mBtnFriend, mBtnFind, mBtnMine;
    private SparseArray<TabFragment> mFragments = new SparseArray<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initViews();

        initViewpagerAdapter();
    }

    private void initViewpagerAdapter() {
        mVpMain.setOffscreenPageLimit(mTitles.size());//设置page被缓存（4个tab只需要设置为2就可以了，设置为size省事）
        mVpMain.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {
            @NonNull
            @Override
            public Fragment getItem(int position) {
                TabFragment tabFragment = TabFragment.newInstance(mTitles.get(position));
                tabFragment.setOnTitleClickListener(new TabFragment.OnTitleClickListener() {
                    @Override
                    public void onClick(String title) {
                        changeWechatTab(title);
                    }
                });
                return tabFragment;
            }

            @Override
            public int getCount() {
                return mTitles.size();
            }

            @NonNull
            @Override
            public Object instantiateItem(@NonNull ViewGroup container, int position) {
                TabFragment tabFragment = (TabFragment) super.instantiateItem(container, position);
                mFragments.put(position, tabFragment);
                return tabFragment;
            }

            @Override
            public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
                mFragments.remove(position);
                super.destroyItem(container, position, object);
            }
        });
    }

    private void initViews() {
        mVpMain = findViewById(R.id.vp_main);
        mBtnWechat = findViewById(R.id.btn_wechat);
        mBtnFriend = findViewById(R.id.btn_friend);
        mBtnFind = findViewById(R.id.btn_find);
        mBtnMine = findViewById(R.id.btn_mine);

        mBtnWechat.setOnClickListener(this);
        mBtnFriend.setOnClickListener(this);
        mBtnFind.setOnClickListener(this);
        mBtnMine.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_wechat:
                TabFragment tabFragment = mFragments.get(0);
                if (tabFragment != null) {
                    tabFragment.changeTitle("微信 changed");
                }
                break;
        }
    }

    public void changeWechatTab(String title) {
        mBtnWechat.setText(title);
    }
}
