package com.xiaobao.good.record.fragment;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.xiaobao.good.R;
import com.xiaobao.good.record.RecordDetailItem;
import com.xiaobao.good.record.adapter.OnlineRecordAdpater;

import java.util.List;

public class RecordFragment extends Fragment {


    private Activity mActivity;

    ListView listView;

    OnlineRecordAdpater onlineRecordAdpater;

    @Override
    public void onAttach(@NonNull Context context) {

        mActivity = (Activity) context;

        super.onAttach(context);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.layout_online_record, container, false);


        listView = view.findViewById(R.id.lv_onlinerecord);
        /**
         * 获取数据，展示
         */

        Bundle bundle = getArguments();
        List<RecordDetailItem> list = (List<RecordDetailItem>) bundle.get("data");

        int visitId = bundle.getInt("visitId", -1);


        Log.i("RecordDetailItem", "size :" + list.size());


        onlineRecordAdpater = new OnlineRecordAdpater(mActivity, list, visitId );
        listView.setAdapter(onlineRecordAdpater);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


                list.get(position);
            }
        });


        return view;
    }


    public static RecordFragment newInstance() {

        Bundle args = new Bundle();
        RecordFragment fragment = new RecordFragment();
        fragment.setArguments(args);
        return fragment;
    }
}
