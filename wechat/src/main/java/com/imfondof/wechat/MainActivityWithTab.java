package com.imfondof.wechat;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.imfondof.wechat.fragment.TabFragment;
import com.imfondof.wechat.view.TabView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivityWithTab extends AppCompatActivity {
    public static final String BUNDLLE_KEY_POS = "BUNDLLE_KEY_POS";//用于恢复当前的fragment tab
    private int mCurTabPos;

    private ViewPager mVpMain;
    private List<String> mTitles = new ArrayList<>(Arrays.asList("微信", "通讯录", "发现", "我"));
    private TabView mBtnWechat, mBtnFriend, mBtnFind, mBtnMine;
    private List<TabView> mTabs = new ArrayList<>();
    private SparseArray<TabFragment> mFragments = new SparseArray<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_with_tab);

        if (savedInstanceState != null) {
            mCurTabPos = savedInstanceState.getInt(BUNDLLE_KEY_POS, 0);
        }

        initViews();
        initViewpagerAdapter();
        initEvent();
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState, @NonNull PersistableBundle outPersistentState) {
        outState.putInt(BUNDLLE_KEY_POS, mVpMain.getCurrentItem());
        super.onSaveInstanceState(outState, outPersistentState);
    }

    private void initEvent() {
        for (int i = 0; i < mTabs.size(); i++) {
            TabView tabView = mTabs.get(i);
            final int finalI = i;
            tabView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mVpMain.setCurrentItem(finalI, false);
                    setCurrentTab(finalI);
                }
            });
        }
    }

    private void initViewpagerAdapter() {
        mVpMain.setOffscreenPageLimit(mTitles.size());//设置page被缓存（4个tab只需要设置为2就可以了，设置为size省事）
        mVpMain.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {
            @NonNull
            @Override
            public Fragment getItem(int position) {
                TabFragment tabFragment = TabFragment.newInstance(mTitles.get(position));
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
        mVpMain.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                if (positionOffset > 0) {
                    TabView left = mTabs.get(position);
                    TabView right = mTabs.get(position + 1);

                    left.setProgress(1 - positionOffset);
                    right.setProgress(positionOffset);
                }
            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private void initViews() {
        mVpMain = findViewById(R.id.vp_main);
        mBtnWechat = findViewById(R.id.tab_wechat);
        mBtnFriend = findViewById(R.id.tab_friend);
        mBtnFind = findViewById(R.id.tab_find);
        mBtnMine = findViewById(R.id.tab_mine);

        mBtnWechat.setIconAndText(R.drawable.wechat, R.drawable.wechat_select, "微信");
        mBtnFriend.setIconAndText(R.drawable.friend, R.drawable.friend_select, "通讯录");
        mBtnFind.setIconAndText(R.drawable.find, R.drawable.find_select, "发现");
        mBtnMine.setIconAndText(R.drawable.mine, R.drawable.mine_select, "我");

        mTabs.add(mBtnWechat);
        mTabs.add(mBtnFriend);
        mTabs.add(mBtnFind);
        mTabs.add(mBtnMine);

        setCurrentTab(mCurTabPos);
    }

    private void setCurrentTab(int pos) {
        for (int i = 0; i < mTabs.size(); i++) {
            TabView tabView = mTabs.get(i);
            if (pos == i) {
                tabView.setProgress(1);
            } else {
                tabView.setProgress(0);
            }
        }
    }
}
