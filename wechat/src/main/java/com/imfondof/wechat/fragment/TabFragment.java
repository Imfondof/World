package com.imfondof.wechat.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.imfondof.wechat.R;

public class TabFragment extends Fragment {

    public static final String BUNDLE_KEY_TITLE = "key_title";
    private TextView mTvTitle;
    private String mTitle;
    private OnTitleClickListener mListener;

    public void setOnTitleClickListener(OnTitleClickListener listener) {
        mListener = listener;
    }

    public interface OnTitleClickListener {
        void onClick(String title);
    }

    public static TabFragment newInstance(String title) {
        Bundle bundle = new Bundle();
        bundle.putString(BUNDLE_KEY_TITLE, title);
        TabFragment tabFragment = new TabFragment();
        tabFragment.setArguments(bundle);
        return tabFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //为什么使用bundle？因为避免由于内存不足造成的fragment重建 进一步导致title没有的bug，arguments在fragment创建和销毁有很重要的作用
        Bundle arguments = getArguments();
        if (arguments != null) {
            mTitle = arguments.getString(BUNDLE_KEY_TITLE, "");
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragement_tag, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mTvTitle = view.findViewById(R.id.tv_title);
        mTvTitle.setText(mTitle);

        mTvTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //fragment与activity通信
                //写法1
//                MainActivity activity = (MainActivity) getActivity();
//                activity.changeWechatTab("微信0");
                //写法2
//                Activity activity1 = getActivity();
//                if (activity1 instanceof MainActivity) {
//                    ((MainActivity) activity1).changeWechatTab("微信0");
//                }
                //写法3 fragment与activity通信 不是 fragment调用activity的方法，而是fragment产生事件，让activity自己去响应【记住fragment是可复用的单元】
                if (mListener != null) {
                    mListener.onClick("微信0");
                }
            }
        });
    }

    public void changeTitle(String title) {
        if (!isAdded()) {//一定要添加判断  要不然可能会造成空指针错误
            return;
        }
        mTvTitle.setText(title);
    }
}
