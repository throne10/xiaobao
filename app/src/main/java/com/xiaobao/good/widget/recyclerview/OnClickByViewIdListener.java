package com.xiaobao.good.widget.recyclerview;

import android.view.View;


public interface OnClickByViewIdListener<T> {
    public void clickByViewId(View view, T t, int position);
}
