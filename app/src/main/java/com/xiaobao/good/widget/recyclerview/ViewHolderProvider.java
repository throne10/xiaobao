package com.xiaobao.good.widget.recyclerview;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

public abstract class ViewHolderProvider<Model,VH extends RecyclerViewHolder> {

    protected OnClickByViewIdListener mOnClickByViewIdListener;
    protected OnLongClickByViewIdListener mOnLongClickByViewListener;

    public VH onCreateViewHolder(@NonNull LayoutInflater layoutInflater,
                                          @NonNull ViewGroup parent,
                                          @NonNull OnClickByViewIdListener onClickByViewIdListener,
                                          @NonNull OnLongClickByViewIdListener onLongClickByViewIdListener){
        this.mOnClickByViewIdListener = onClickByViewIdListener;
        this.mOnLongClickByViewListener = onLongClickByViewIdListener;
        return (VH)new RecyclerViewHolder(layoutInflater.inflate(setLayout(), parent, false));
    }

    public abstract int setLayout();

    public abstract void onBindViewHolder(Model model, VH viewHolder,int position);
}
