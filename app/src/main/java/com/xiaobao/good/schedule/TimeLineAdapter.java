package com.xiaobao.good.schedule;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.xiaobao.good.R;
import com.xiaobao.good.common.StringUtils;

import java.util.List;

public class TimeLineAdapter extends BaseAdapter {
    private  Context context;
    List<VisitRecords.DataBean.RecordsBean> recordsBeans;

    // 适配器的构造函数，把要适配的数据传入这里
    public TimeLineAdapter(List<VisitRecords.DataBean.RecordsBean> recordsBeans, Context context) {
        this.recordsBeans = recordsBeans;
        this.context = context;
    }

    @Override
    public int getCount() {
        return recordsBeans.size();
    }

    @Override
    public Object getItem(int i) {
        return recordsBeans.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        VisitRecords.DataBean.RecordsBean s = recordsBeans.get(position);
        View view;
        ViewHolder viewHolder;
        if (convertView == null) {
            view = LayoutInflater.from(context).inflate(R.layout.time_line_list_item, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.tvTimeLine = view.findViewById(R.id.tv_time_line);
            viewHolder.tvPurpose = view.findViewById(R.id.tv_purpose);
            viewHolder.ivPurpose = view.findViewById(R.id.iv_purpose);
            viewHolder.ivVoice = view.findViewById(R.id.iv_voice);
            view.setTag(viewHolder);
        } else {
            view = convertView;
            viewHolder = (ViewHolder) view.getTag();
        }
        if (s.getPurpose().equals("展业")) {
            viewHolder.ivPurpose.setImageResource(R.mipmap.business_icon);
        }
        if (s.getPurpose().equals("送礼品")) {
            viewHolder.ivPurpose.setImageResource(R.mipmap.give_icon);
        }
        if (s.getPurpose().equals("递送保单")) {
            viewHolder.ivPurpose.setImageResource(R.mipmap.delivery_icon);
        }
        if (s.getPurpose().equals("微信聊天")) {
            viewHolder.ivPurpose.setImageResource(R.mipmap.we_chat_icon);
        }
        if (StringUtils.isNotEmpty(s.getVisit_time())) {
            try {
                String[] times = s.getVisit_time().split(" ")[0].split("-");
                viewHolder.tvTimeLine.setText(times[2] + "/" + times[1] + "月");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        viewHolder.tvPurpose.setText("拜访此客户的,目的：" + s.getPurpose());

        if (!s.getVoices().isEmpty()) {
            viewHolder.ivVoice.setVisibility(View.VISIBLE);
        } else {
            viewHolder.ivVoice.setVisibility(View.INVISIBLE);
        }
        return view;
    }

    // 定义一个内部类，用于对控件的实例进行缓存
    class ViewHolder {
        TextView tvTimeLine;
        TextView tvPurpose;
        ImageView ivPurpose;
        ImageView ivVoice;
    }
}