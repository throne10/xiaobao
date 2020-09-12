package com.xiaobao.good.record.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

import com.xiaobao.good.R;
import com.xiaobao.good.record.RecordDetailItem;

import java.util.List;

public class OnlineRecordAdpater extends BaseAdapter {


    List<RecordDetailItem> list;

    Context context;

    public OnlineRecordAdpater(Context context, List<RecordDetailItem> list) {
        this.list = list;
        this.context = context;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.layout_record_detail_item, null);
            holder = new ViewHolder();
            holder.text = (TextView) convertView.findViewById(R.id.tv_record_name);
            holder.seekbar = (SeekBar) convertView.findViewById(R.id.seekbar);
            holder.detailOrUpload = (Button) convertView.findViewById(R.id.bt_detail_or_upload);
            holder.play = (Button) convertView.findViewById(R.id.bt_play);


            holder.detailOrUpload.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    /**
                     * 根据type,展示详情，还是上传
                     */


                }
            });


            holder.play.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {




                }
            });
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        RecordDetailItem recordDetailItem = list.get(position);
        holder.text.setText(recordDetailItem.getFilePath());
        String typeMsg = recordDetailItem.getType().equals("0") ? "详情" : "上传";
        holder.detailOrUpload.setText(typeMsg);
        return convertView;

    }


    static class ViewHolder {
        TextView text;
        SeekBar seekbar;
        Button detailOrUpload;
        Button play;

        String type;
        int position;


    }
}
