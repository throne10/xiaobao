package com.xiaobao.good.widget.recyclerview;

import android.graphics.Bitmap;
import android.text.SpannableStringBuilder;
import android.util.SparseArray;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

public class RecyclerViewHolder extends RecyclerView.ViewHolder {

    private final SparseArray<View> mViews;
    private View mConvertView;

    public RecyclerViewHolder(View view) {
        super(view);
        this.mViews = new SparseArray<View>();
        mConvertView = view;
    }

    /**
     * 初始化ViewHolder
     *
     * @return
     */
    public static RecyclerViewHolder getInstance(View view) {
        RecyclerViewHolder mRecyclerViewHolder = new RecyclerViewHolder(view);
        return mRecyclerViewHolder;
    }

    /**
     * 通过viewId获取View
     *
     * @param viewId
     * @param <T>
     * @return
     */
    public <T extends View> T getViewById(int viewId) {
        View view = (View) mViews.get(viewId);
        if (view == null) {
            view = mConvertView.findViewById(viewId);
            mViews.put(viewId, view);
        }
        return (T) view;
    }

    /**
     * 获取VoncerView
     *
     * @return
     */
    public View getConvertView() {
        return mConvertView;
    }

    /**
     * setTextView's Text
     *
     * @param viewId
     * @param text
     * @return
     */
    public RecyclerViewHolder setTVText(int viewId, SpannableStringBuilder text) {
        TextView tv = getViewById(viewId);
        if(null == text){
            tv.setText("");
        }else{
            tv.setText(text);
        }
        return this;
    }

    /**
     * setTextView's Text
     *
     * @param viewId
     * @param text
     * @return
     */
    public RecyclerViewHolder setTVText(int viewId, String text) {
        TextView tv = getViewById(viewId);
        if(null == text){
            tv.setText("");
        }else{
            tv.setText(text);
        }
        return this;
    }

    /**
     * setEditText's Text
     *
     * @param viewId
     * @param text
     * @return
     */
    public RecyclerViewHolder setEVText(int viewId, String text) {
        EditText tv = getViewById(viewId);
        if(null == text){
            tv.setText("");
        }else{
            tv.setText(text);
        }
        return this;
    }

    /**
     * setButton's Text
     *
     * @param viewId
     * @param text
     * @return
     */
    public RecyclerViewHolder setBtnText(int viewId, String text) {
        Button btn = getViewById(viewId);
        if(null == text){
            btn.setText("");
        }else{
            btn.setText(text);
        }
        return this;
    }

    /**
     * 根据resouceId设置image
     *
     * @param viewId
     * @param resId
     * @return
     */
    public RecyclerViewHolder setImgResource(int viewId, int resId) {
        ImageView imgView = getViewById(viewId);
        imgView.setImageResource(resId);
        return this;
    }

    /**
     * 根据bitmap设置image
     *
     * @param viewId
     * @param bitmap
     * @return
     */
    public RecyclerViewHolder setImgBitmap(int viewId, Bitmap bitmap) {
        ImageView imgView = getViewById(viewId);
        imgView.setImageBitmap(bitmap);
        return this;
    }

    /**
     * 根据resource设置imgBackground
     *
     * @param viewId
     * @param imageRes
     * @return
     */
    public RecyclerViewHolder setImgBackgroundResource(int viewId, int imageRes) {
        ImageView imgView = getViewById(viewId);
        imgView.setBackgroundResource(imageRes);
        return this;
    }
}
