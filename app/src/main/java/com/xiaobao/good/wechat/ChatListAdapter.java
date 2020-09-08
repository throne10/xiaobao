package com.xiaobao.good.wechat;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.xiaobao.good.InitApplication;
import com.xiaobao.good.R;

import java.util.List;

public class ChatListAdapter extends BaseAdapter {
    List<String> chats;

    // 适配器的构造函数，把要适配的数据传入这里
    public ChatListAdapter(List<String> chats) {
        this.chats = chats;
    }

    @Override
    public int getCount() {
        return chats.size();
    }

    @Override
    public Object getItem(int i) {
        return chats.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        String s = chats.get(position);
        View view;
        ViewHolder viewHolder;
        if (convertView == null) {
            view = LayoutInflater.from(InitApplication.getContext()).inflate(R.layout.wechat_list_item, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.tvChat = view.findViewById(R.id.tv_chat);
            view.setTag(viewHolder);
        } else {
            view = convertView;
            viewHolder = (ViewHolder) view.getTag();
        }

        viewHolder.tvChat.setText(s);
        return view;
    }

    // 定义一个内部类，用于对控件的实例进行缓存
    class ViewHolder {
        TextView tvChat;
    }
}