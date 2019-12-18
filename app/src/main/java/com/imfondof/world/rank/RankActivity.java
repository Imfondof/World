package com.imfondof.world.rank;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.imfondof.world.R;
import com.imfondof.world.rank.fragment.RankPKFragment;
import com.imfondof.world.rank.fragment.RankResultFragment;
import com.imfondof.world.rank.fragment.RankTypeFragment;

import java.util.ArrayList;
import java.util.List;

public class RankActivity extends FragmentActivity implements View.OnClickListener {
    private ViewPager mViewPager;
    private FragmentPagerAdapter mAdapter;
    private List<Fragment> mFragment;

    private LinearLayout mll_notice;
    private LinearLayout mll_course;
    private LinearLayout mll_user;
    private ImageButton mll_noticeImg;
    private ImageButton mll_courseImg;
    private ImageButton mll_userImg;
    private TextView mll_noticetv;
    private TextView mll_coursetv;
    private TextView mll_usertv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_rank);

        initView();
        initEvents();
        setSelect(1);
    }

    private void initView() {
        mViewPager = findViewById(R.id.viewPager);
        mll_notice = findViewById(R.id.ll_notice);
        mll_course = findViewById(R.id.ll_course);
        mll_user = findViewById(R.id.ll_user);
        mll_noticeImg = findViewById(R.id.ll_notice_img);
        mll_courseImg = findViewById(R.id.ll_course_img);
        mll_userImg = findViewById(R.id.ll_user_img);

        mll_noticetv = findViewById(R.id.ll_notice_tv);
        mll_coursetv = findViewById(R.id.ll_course_tv);
        mll_usertv = findViewById(R.id.ll_user_tv);

        mFragment = new ArrayList<>();
        Fragment mtabnotice = new RankResultFragment();
        Fragment mtabcourse = new RankPKFragment();
        Fragment mtabuser = new RankTypeFragment();
        mFragment.add(mtabnotice);
        mFragment.add(mtabcourse);
        mFragment.add(mtabuser);

        mAdapter = new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public int getCount() {
                return mFragment.size();
            }

            @Override
            public Fragment getItem(int arg0) {
                return mFragment.get(arg0);
            }
        };
        mViewPager.setAdapter(mAdapter);
        mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageSelected(int arg0) {
                int currentItem = mViewPager.getCurrentItem();
                setTab(currentItem);
            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {

            }

            @Override
            public void onPageScrollStateChanged(int arg0) {

            }
        });
    }

    private void initEvents() {
        mll_notice.setOnClickListener(this);
        mll_course.setOnClickListener(this);
        mll_user.setOnClickListener(this);
    }

    private void setSelect(int i) {
        // 设置图片为亮色、切换内容区域
        setTab(i);
        mViewPager.setCurrentItem(i);
    }

    private void setTab(int i) {
        resetImg();
        switch (i) {
            case 0:
                mll_noticeImg.setImageResource(R.drawable.ic_rank_notice_light);
                mll_noticetv.setTextColor(0xff1afa29);
                break;
            case 1:
                mll_courseImg.setImageResource(R.drawable.ic_rank_course_light);
                mll_coursetv.setTextColor(0xff1afa29);
                break;
            case 2:
                mll_userImg.setImageResource(R.drawable.ic_rank_user_light);
                mll_usertv.setTextColor(0xff1afa29);
                break;
            default:
                break;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    public void onClick(View v) {
        resetImg();
        switch (v.getId()) {
            case R.id.ll_notice:
                setSelect(0);
                break;
            case R.id.ll_course:
                setSelect(1);
                break;
            case R.id.ll_user:
                setSelect(2);
                break;
            default:
                break;
        }
    }

    private void resetImg() {
        mll_noticeImg.setImageResource(R.drawable.ic_rank_notice);
        mll_noticetv.setTextColor(0xff000000);
        mll_courseImg.setImageResource(R.drawable.ic_rank_course);
        mll_coursetv.setTextColor(0xff000000);
        mll_userImg.setImageResource(R.drawable.ic_rank_user);
        mll_usertv.setTextColor(0xff000000);
    }
}
