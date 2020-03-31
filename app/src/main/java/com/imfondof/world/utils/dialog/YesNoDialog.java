package com.imfondof.world.utils.dialog;

import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.imfondof.world.R;
import com.imfondof.world.utils.DensityUtil;

/**
 * 两按钮
 * Created by Imfondof on 2019/3/30.
 * 标题、内容可以加粗
 * 可设置点击屏幕是否可取消
 * 背景圆角12dp
 */
public class YesNoDialog extends DialogFragment implements View.OnClickListener {

    private TextView title, content;
    private Button ok, cancel;
    public YesNoCallback yesNoCallback;
    private String title_str, content_str, no, yes;
    private boolean isOkBold, cancelable;
    private int textColor, start, end; //改变内容中部分字体的颜色
    private boolean changeColor;
    private boolean isFontBold;// 是否加粗content
    private boolean isTitleBold;//是否加粗title

    private DialogInterface.OnCancelListener cancelListener;


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        setStyle(DialogFragment.STYLE_NO_TITLE, R.style.up_dialog_theme);
//        setCancelable(true);
        return super.onCreateDialog(savedInstanceState);
    }

    private View rootView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (rootView != null) {
            ViewGroup parent = (ViewGroup) rootView.getParent();
            if (parent != null) {
                parent.removeView(rootView);
            }
        } else {
            rootView = inflater.inflate(R.layout.dialog_yes_no, container, false);
            initView(rootView);
        }
        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Dialog dialog = getDialog();
        if (dialog != null) {
            dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialog) {
                    dismissAllowingStateLoss();
                    if (cancelListener != null) {
                        cancelListener.onCancel(dialog);
                    }
                }
            });
        }
    }

    private void initView(View rootView) {
        Bundle args = getArguments();
        if (args == null)
            return;
        title_str = args.getString("title");
        content_str = args.getString("content");
        yes = args.getString("yes");
        isOkBold = args.getBoolean("isOkBold", false);
        no = args.getString("no");
        int gravity = args.getInt("content_gravity", Gravity.LEFT);
        cancelable = args.getBoolean("cancelable", true);
        setCancelable(cancelable);
        changeColor = args.getBoolean("changeColor", false);
        textColor = args.getInt("textColor");
        start = args.getInt("start");
        end = args.getInt("end");
        isFontBold = args.getBoolean("isFontBold");
        title = rootView.findViewById(R.id.dialog_yes_or_no_title);
        content = rootView.findViewById(R.id.dialog_yes_or_no_content);
        content.setGravity(gravity);
        ok = rootView.findViewById(R.id.dialog_yes_or_no_ok);
        cancel = rootView.findViewById(R.id.dialog_yes_or_no_cancel);
        ok.setOnClickListener(this);
        if (isOkBold) {
            ok.setTypeface(Typeface.DEFAULT_BOLD);
        } else {
            ok.setTypeface(Typeface.DEFAULT);
        }
        TextPaint tp = content.getPaint();
        TextPaint tpTitle = title.getPaint();
        if (isFontBold) {
            tp.setFakeBoldText(true);
        } else {
            tp.setFakeBoldText(false);
        }
        if (isTitleBold) {
            tpTitle.setFakeBoldText(true);
        } else {
            tpTitle.setFakeBoldText(false);
        }
        cancel.setOnClickListener(this);
        if (!TextUtils.isEmpty(no)) {
            cancel.setText(no);
            if (no.length() > 6) {
                cancel.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 14);
            }
        }
        if (!TextUtils.isEmpty(yes)) {
            ok.setText(yes);
            if (yes.length() > 6) {
                ok.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 14);
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (title_str == null) {
            title.setText("提示");
        } else if (title_str.equals("")) {
            title.setVisibility(View.GONE);
            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) title.getLayoutParams();
            params.setMargins(0, 0, 0, DensityUtil.dip2px(getContext(), 14));
            title.setLayoutParams(params);
        } else {
            title.setText(title_str);
        }
        if (!TextUtils.isEmpty(content_str)) {
            if (changeColor && start < content_str.length() && end <= content_str.length()) {
                SpannableString spannableString = new SpannableString(content_str);
                spannableString.setSpan(new ForegroundColorSpan(textColor), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                content.setText(spannableString);
            } else {
                content.setText(content_str);
            }
        }
    }

    public static final YesNoDialog newInstance(String content_str, YesNoCallback callback) {
        return newInstance("", content_str, "", "", callback);
    }

    public static final YesNoDialog newInstance(String title_str, String content_str) {
        return newInstance(title_str, content_str, "", "", null);
    }

    public static final YesNoDialog newInstance(String title_str, String content_str, YesNoCallback callback) {
        return newInstance(title_str, content_str, "", "", callback);
    }

    public static final YesNoDialog newInstance(String title_str, String content_str, String no, String yes) {
        return newInstance(title_str, content_str, no, yes, null);
    }

    public static final YesNoDialog newInstance(String title_str, String content_str, String no, String yes, YesNoCallback callback) {
        YesNoDialog fragment = new YesNoDialog();
        Bundle bundle = new Bundle();
        bundle.putString("title", title_str);
        bundle.putString("content", content_str);
        bundle.putString("yes", yes);
        bundle.putString("no", no);
        fragment.yesNoCallback = callback;
        fragment.setArguments(bundle);
        return fragment;
    }

    public static final YesNoDialog newInstance(boolean isTitleBold, String title_str, String content_str, String no, String yes, boolean cancelable, YesNoCallback callback) {
        YesNoDialog fragment = new YesNoDialog();
        Bundle bundle = new Bundle();
        bundle.putString("title", title_str);
        bundle.putString("content", content_str);
        bundle.putString("yes", yes);
        bundle.putString("no", no);
        bundle.putBoolean("isFontBold", isTitleBold);
        fragment.yesNoCallback = callback;
        fragment.setArguments(bundle);
        bundle.putBoolean("cancelable", cancelable);
        return fragment;
    }

    public static final YesNoDialog newInstance(String title_str, String content_str, String no, String yes, boolean isOkBold, YesNoCallback callback) {
        YesNoDialog fragment = new YesNoDialog();
        Bundle bundle = new Bundle();
        bundle.putString("title", title_str);
        bundle.putString("content", content_str);
        bundle.putBoolean("isOkBold", isOkBold);
        bundle.putString("yes", yes);
        bundle.putString("no", no);
        fragment.yesNoCallback = callback;
        fragment.setArguments(bundle);
        return fragment;
    }

    public static final YesNoDialog newInstance(String title_str, String content_str, int content_gravity, YesNoCallback callback) {
        return newInstance(title_str, content_str, "", "", content_gravity, callback);
    }

    public static final YesNoDialog newInstance(String title_str, String content_str, String no, String yes, int content_gravity, YesNoCallback callback) {
        YesNoDialog fragment = new YesNoDialog();
        Bundle bundle = new Bundle();
        bundle.putString("title", title_str);
        bundle.putString("content", content_str);
        bundle.putString("yes", yes);
        bundle.putString("no", no);
        bundle.putInt("content_gravity", content_gravity);
        fragment.yesNoCallback = callback;
        fragment.setArguments(bundle);
        return fragment;
    }

    public static final YesNoDialog newInstance(String title_str, String content_str, String no, String yes, int content_gravity, boolean cancelable, YesNoCallback callback) {
        YesNoDialog fragment = new YesNoDialog();
        Bundle bundle = new Bundle();
        bundle.putString("title", title_str);
        bundle.putString("content", content_str);
        bundle.putString("yes", yes);
        bundle.putString("no", no);
        bundle.putInt("content_gravity", content_gravity);
        bundle.putBoolean("cancelable", cancelable);
        fragment.yesNoCallback = callback;
        fragment.setArguments(bundle);
        return fragment;
    }

    public static final YesNoDialog newInstance(String title_str, String content_str, boolean changeColor, int textColor, int start, int end, String no, String yes, int content_gravity, boolean cancelable, YesNoCallback callback) {
        YesNoDialog fragment = new YesNoDialog();
        Bundle bundle = new Bundle();
        bundle.putString("title", title_str);
        bundle.putString("content", content_str);
        bundle.putString("yes", yes);
        bundle.putString("no", no);
        bundle.putInt("content_gravity", content_gravity);
        bundle.putBoolean("cancelable", cancelable);
        bundle.putBoolean("changeColor", changeColor);
        bundle.putInt("textColor", textColor);
        bundle.putInt("start", start);
        bundle.putInt("end", end);
        fragment.yesNoCallback = callback;
        fragment.setArguments(bundle);
        return fragment;
    }

    public static final YesNoDialog newInstance(String title_str, String content_str, int content_gravity, boolean isFontBold, YesNoCallback callback) {
        YesNoDialog fragment = new YesNoDialog();
        Bundle bundle = new Bundle();
        bundle.putString("title", title_str);
        bundle.putString("content", content_str);
        bundle.putString("yes", "");
        bundle.putString("no", "");
        bundle.putInt("content_gravity", content_gravity);
        bundle.putBoolean("isFontBold", isFontBold);
        fragment.yesNoCallback = callback;
        fragment.setArguments(bundle);
        return fragment;
    }

    public void setYesNoCallback(YesNoCallback yesNoCallback) {
        this.yesNoCallback = yesNoCallback;
    }


    public void setCancelListener(DialogInterface.OnCancelListener cancelListener) {
        this.cancelListener = cancelListener;
    }

    @Override
    public void onClick(View v) {
        dismissAllowingStateLoss();
        int id = v.getId();
        if (id == R.id.dialog_yes_or_no_ok) {
            if (yesNoCallback != null) {
                yesNoCallback.ok();
            }
        } else if (id == R.id.dialog_yes_or_no_cancel) {
            if (yesNoCallback != null) {
                yesNoCallback.cancel();
            }
        }
    }

    public interface YesNoCallback {
        public void ok();

        public void cancel();
    }
}
