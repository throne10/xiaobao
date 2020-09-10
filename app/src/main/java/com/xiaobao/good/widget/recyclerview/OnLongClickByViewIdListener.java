package com.xiaobao.good.widget.recyclerview;

import android.view.View;

public interface OnLongClickByViewIdListener<T> {
    public void longClickByViewId(View view, T t, int position);
}
