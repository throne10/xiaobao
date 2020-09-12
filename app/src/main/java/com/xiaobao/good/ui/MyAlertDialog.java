package com.xiaobao.good.ui;


import android.content.Context;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.xiaobao.good.R;

public class MyAlertDialog {
    Context context;
    android.app.AlertDialog ad;
    TextView tv_dialog_tip;
    //    TextView messageView;
    private RelativeLayout rl_dialog_positive;
    private RelativeLayout rl_dialog_negative;
    private TextView tv_dialog_positive;
    private TextView tv_dialog_negative;
    private ImageView iv_dialog_divider;

    public MyAlertDialog(Context context) {
        this.context = context;
        ad = new android.app.AlertDialog.Builder(context).create();
        ad.show();
        Window window = ad.getWindow();
        window.setContentView(R.layout.dialog);
        initView(window);
    }

    /**
     * 初始化组件
     * @date 2014-6-4下午2:12:00 
     * @version：  V3.0.0327.1
     */
    private void initView(Window window) {

        tv_dialog_tip = (TextView) window.findViewById(R.id.tv_dialog_tip);
        rl_dialog_positive = (RelativeLayout) window.findViewById(R.id.rl_dialog_positive);
        rl_dialog_negative = (RelativeLayout) window.findViewById(R.id.rl_dialog_negative);
        tv_dialog_positive = (TextView) window.findViewById(R.id.tv_dialog_positive);
        tv_dialog_negative = (TextView) window.findViewById(R.id.tv_dialog_negative);
        iv_dialog_divider = (ImageView) window.findViewById(R.id.iv_dialog_divider);

        iv_dialog_divider.setVisibility(View.GONE);
        rl_dialog_positive.setVisibility(View.GONE);
        rl_dialog_negative.setVisibility(View.GONE);
    }

    /**
     * 设置标题
     * @date 2014-6-4下午3:09:45 
     * @version：  V3.0.0327.1
     */
    public void setTitle(String title) {
        tv_dialog_tip.setText(title);
    }

    /**
     * 是否可以被取消
     * @param true:可以返回键退出，false:禁止退出
     * @date 2014-6-4下午3:32:09 
     * @version：  V3.0.0327.1
     */
    public void setCancelable(boolean cancelable) {
        ad.setCancelable(cancelable);
    }

    /**
     * 设置按钮
     * @param text
     * @param listener
     */
    public void setPositiveButton(String text, final View.OnClickListener listener) {
        rl_dialog_positive.setVisibility(View.VISIBLE);
        if (rl_dialog_positive.getVisibility() == View.VISIBLE && rl_dialog_negative.getVisibility() == View.VISIBLE) {
            iv_dialog_divider.setVisibility(View.VISIBLE);
        }
        tv_dialog_positive.setText(text);
        rl_dialog_positive.setOnClickListener(listener);
    }

    /**
     * 设置按钮
     * @param text
     * @param listener
     */
    public void setNegativeButton(String text, final View.OnClickListener listener) {
        rl_dialog_negative.setVisibility(View.VISIBLE);
        if (rl_dialog_positive.getVisibility() == View.VISIBLE && rl_dialog_negative.getVisibility() == View.VISIBLE) {
            iv_dialog_divider.setVisibility(View.VISIBLE);
        }
        tv_dialog_negative.setText(text);
        rl_dialog_negative.setOnClickListener(listener);
    }

    /**
     * 设置按钮字体颜色
     * @param text
     * @param listener
     */
    public void setNegativeButtonTextColor(int color) {
        tv_dialog_negative.setTextColor(color);
    }

    /**
     * 关闭对话框
     */
    public void dismiss() {
        try {
            if (ad != null && ad.isShowing())
                ad.dismiss();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setTitleTextColor(int color) {
        tv_dialog_tip.setTextColor(color);
    }
}