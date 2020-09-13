package com.xiaobao.good.record.fragment;

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

import com.xiaobao.good.AudioRecordDetailActivity;
import com.xiaobao.good.R;
import com.xiaobao.good.db.AbstractAppDatabase;
import com.xiaobao.good.log.LogUtil;
import com.xiaobao.good.record.RecordDetailItem;
import com.xiaobao.good.record.adapter.OnlineRecordAdpater;
import com.xiaobao.good.schedule.VisitRecords;
import com.xiaobao.good.ui.MyAlertDialog;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class CloudRecordFragment extends Fragment {

    private static final String TAG = "RecordFragment_RF";
    private AudioRecordDetailActivity mActivity;

    ListView listView;

    OnlineRecordAdpater onlineRecordAdpater;


    int visitId;

    @Override
    public void onAttach(@NonNull Context context) {

        mActivity = (AudioRecordDetailActivity) context;

        super.onAttach(context);
    }


    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.layout_online_record, container, false);

        listView = view.findViewById(R.id.lv_onlinerecord);
        /** 获取数据，展示 */
        Bundle bundle = getArguments();
        List<RecordDetailItem> list = (List<RecordDetailItem>) bundle.get("data");

        visitId = bundle.getInt("visitId", -1);

        Log.i("RecordDetailItem", "size :" + list);

        onlineRecordAdpater = new OnlineRecordAdpater(mActivity, list, visitId);
        listView.setAdapter(onlineRecordAdpater);

        listView.setOnItemClickListener(
                (parent, view1, position, id) -> {
                    Log.i("RecordDetailItem", "position :" + position);
                    onlineRecordAdpater.setSelectItem(position);
                    onlineRecordAdpater.notifyDataSetChanged();
                });

        listView.setOnItemLongClickListener(
                new AdapterView.OnItemLongClickListener() {
                    @Override
                    public boolean onItemLongClick(
                            AdapterView<?> parent, View view, int position, long id) {
                        if (list != null && list.size() > position) {
                            RecordDetailItem recordDetailItem = list.get(position);
                            if ("1".equals(recordDetailItem.getType())) {
                                showDialog(position, recordDetailItem);
                            }
                        }

                        return false;
                    }
                });

        return view;
    }

    public void showDialog(int position, RecordDetailItem recordDetailItem) {
        MyAlertDialog myAlertDialog = new MyAlertDialog(getActivity());
        myAlertDialog.setTitle("是否删除本地音频");
        myAlertDialog.setCancelable(false);
        myAlertDialog.setPositiveButton(
                "取消",
                view -> {
                    myAlertDialog.dismiss();
                });
        myAlertDialog.setNegativeButton(
                "删除",
                view -> {
                    try {
                        File file = new File(recordDetailItem.getFilePath());
                        if (file.exists()) {
                            boolean delete = file.delete();
                            if (delete) {
                                AbstractAppDatabase.getDbDateHelper()
                                        .getRecordHistoryDao()
                                        .deleteByFilePath(recordDetailItem.getFilePath());
                                onlineRecordAdpater.removeItem(position);
                                onlineRecordAdpater.notifyDataSetChanged();
                                myAlertDialog.dismiss();
                            }
                        }
                    } catch (Exception e) {
                        LogUtil.d(TAG, "delete local record e : " + e.toString());
                    }
                });
    }

    public static CloudRecordFragment newInstance() {

        Bundle args = new Bundle();
        CloudRecordFragment fragment = new CloudRecordFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        onlineRecordAdpater.stopPaly();
        Log.i("yxd", "onDestroy");
    }

    public void reflesh(VisitRecords.DataBean.RecordsBean recordsBean) {

        List<RecordDetailItem> list = new ArrayList<>();
        if (recordsBean != null) {
            List<VisitRecords.DataBean.RecordsBean.VoicesBean> voicesList = recordsBean.getVoices();
            RecordDetailItem recordDetailItem;
            for (VisitRecords.DataBean.RecordsBean.VoicesBean v : voicesList) {
                recordDetailItem = new RecordDetailItem();
                recordDetailItem.setType("0");
                recordDetailItem.setFilePath("http://ineutech.com:60003/xiaobaovisit/visit_voice/" + v.getVoice_file());
                recordDetailItem.setExtra(v.getVoice_id() + "");
                list.add(recordDetailItem);
            }


            onlineRecordAdpater = new OnlineRecordAdpater(mActivity, list, visitId);


            onlineRecordAdpater.notifyDataSetChanged();
        }

    }
}
