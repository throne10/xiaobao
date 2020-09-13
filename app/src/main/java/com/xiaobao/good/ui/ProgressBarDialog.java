package com.xiaobao.good.ui;

import android.app.AlertDialog;
import android.content.Context;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import com.xiaobao.good.R;

public class ProgressBarDialog {
    Context context;
    String dots = "";
    AlertDialog ad;
    int dotlength = 5;
    TextView tv_dialog_tip;
    private ImageView im_dialog_wifestate;
    String Title = "";

    public ProgressBarDialog(Context context) {
        this.context = context;
        ad = new AlertDialog.Builder(context).create();
        ad.show();
        Window window = ad.getWindow();
        window.setContentView(R.layout.progressbardialog);
        initView(window);
    }

    /**
     * 初始化组件
     * @date 2014-6-4下午2:12:00 
     * @version：  V3.0.0327.1
     */
    private void initView(Window window) {
        tv_dialog_tip = (TextView) window.findViewById(R.id.tv_dialog_tip);
    }

    /**
     * 设置标题
     * @date 2014-6-4下午3:09:45 
     * @version：  V3.0.0327.1
     */
    public void setTitle(final String title) {
        this.Title = title;
        tv_dialog_tip.setText(Title);
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

    /**
     * 设置图片显示
     */
    public void setTopImageVisibility() {
        im_dialog_wifestate.setVisibility(View.VISIBLE);
    }

    public void setTitleTextColor(int color) {
        tv_dialog_tip.setTextColor(color);
    }
}