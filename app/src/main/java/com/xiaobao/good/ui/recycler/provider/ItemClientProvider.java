package com.xiaobao.good.ui.recycler.provider;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.widget.TextView;

import com.google.gson.Gson;
import com.xiaobao.good.R;
import com.xiaobao.good.schedule.ScheduleActivity;
import com.xiaobao.good.ui.recycler.model.ItemClient;
import com.xiaobao.good.widget.recyclerview.RecyclerViewHolder;
import com.xiaobao.good.widget.recyclerview.ViewHolderProvider;

/**
 * 类名称：ItemClientProvider 类描述： 创建人：L.C.W 创建时间：2020/9/10.21:28 修改人：L.C.W 修改时间：2020/9/10.21:28 修改备注：
 */
public class ItemClientProvider extends ViewHolderProvider<ItemClient, RecyclerViewHolder> {

    private Context context;

    public ItemClientProvider(Context context) {
        this.context = context;
    }

    @Override
    public int setLayout() {
        return R.layout.layout_item_client;
    }

    @Override
    public void onBindViewHolder(
            ItemClient itemClient, RecyclerViewHolder viewHolder, int position) {
        viewHolder.setTVText(R.id.tv_name, itemClient.clientsBean.getClient_name());
        String type = itemClient.clientsBean.getClient_type();
        viewHolder.setTVText(R.id.tv_type, type);
        TextView tvType = viewHolder.getViewById(R.id.tv_type);
        if ("A".equalsIgnoreCase(type)) {
            tvType.setTextColor(Color.parseColor("#2496d5"));
        } else if ("P".equalsIgnoreCase(type)) {
            tvType.setTextColor(Color.YELLOW);
        } else if ("C".equalsIgnoreCase(type)) {
            tvType.setTextColor(Color.GREEN);
        }
    }
}
