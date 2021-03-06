package com.xiaobao.good;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager.widget.ViewPager;

import com.google.gson.Gson;
import com.xiaobao.good.db.AbstractAppDatabase;
import com.xiaobao.good.db.RecordHistoryBean;
import com.xiaobao.good.db.dao.RecordHistoryDao;
import com.xiaobao.good.log.LogUtil;
import com.xiaobao.good.record.RecordDetailItem;
import com.xiaobao.good.record.fragment.CloudRecordFragment;
import com.xiaobao.good.record.fragment.ContentFragmentPagerAdapter;
import com.xiaobao.good.record.fragment.RecordFragment;
import com.xiaobao.good.retrofit.RetrofitUtils;
import com.xiaobao.good.schedule.VisitRecords;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AudioRecordDetailActivity extends FragmentActivity implements View.OnClickListener {


    private static final String TAG = "record_detail";
    private ViewPager pager;
    private List<Fragment> list;

    private TextView tv_online_record;
    private TextView tv_local_record;

    ImageView iv_back;
    ImageView iv_add;

    RecordFragment recordFragment;
    CloudRecordFragment clouldRecordFragment;

    /**
     * 选中选项卡颜色
     */
    private int selectColor = Color.parseColor("#FF9100");
    /**
     * 未选中选项颜色
     */
    private int deselectTextColor = Color.parseColor("#ffffff");
    private VisitRecords.DataBean.RecordsBean recordsBean;

    private int visit_id;
    private String add;
    private int clientId;
    private int employeeId;
    private List<VisitRecords.DataBean.RecordsBean> recordsBeans;
    private Context context;
    private String name;

    @Override
    public Resources getResources() {
        Resources res = super.getResources();
        if (res.getConfiguration().fontScale != 1) {//非默认值
            Configuration newConfig = new Configuration();
            newConfig.setToDefaults();//设置默认
            res.updateConfiguration(newConfig, res.getDisplayMetrics());
        }
        return res;
    }
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_record_detail);

        context = this;
        //初始化Fragment数组
        pager = (ViewPager) this.findViewById(R.id.pager);
        list = new ArrayList<>();

        tv_online_record = this.findViewById(R.id.tv_online_record);
        tv_local_record = this.findViewById(R.id.tv_local_record);

        iv_back = this.findViewById(R.id.iv_back);
        iv_add = this.findViewById(R.id.iv_add);

        iv_add.setOnClickListener(this);
        iv_back.setOnClickListener(this);
        tv_online_record.setOnClickListener(this);
        tv_local_record.setOnClickListener(this);
        String s = getIntent().getStringExtra("date");
        add = getIntent().getStringExtra("add");
        clientId = getIntent().getIntExtra("clientId", 0);
        employeeId = getIntent().getIntExtra("employeeId", 0);
        visit_id = getIntent().getIntExtra("visitId", -1);
        name = getIntent().getStringExtra("name");
        recordsBean = new Gson().fromJson(s, VisitRecords.DataBean.RecordsBean.class);

        initViewPager(0);

//        getVisitRecords();
    }

    public void getVisitRecords() {

        ProgressDialog dialog = ProgressDialog.show(this, "提示", "正在更新云端数据……", false, false);

        Call<VisitRecords> visitRecordsCall =
                RetrofitUtils.getService()
                        .getVisit(employeeId, clientId);

        visitRecordsCall.enqueue(
                new Callback<VisitRecords>() {
                    @Override
                    public void onResponse(
                            Call<VisitRecords> call, Response<VisitRecords> response) {

                        if (dialog != null) {
                            dialog.dismiss();
                        }
                        if (response.isSuccessful()) {
                            recordsBeans = response.body().getData().getRecords();
                            LogUtil.i(TAG, "recordsBeans>>>>" + recordsBeans);
                            if (!recordsBeans.isEmpty()) {
                                for (VisitRecords.DataBean.RecordsBean recordsBean : recordsBeans) {
                                    if (visit_id == recordsBean.getVisit_id()) {
                                        LogUtil.i(TAG, "records>>>>" + recordsBean);


                                        /**
                                         * 更新数据
                                         */
                                        clouldRecordFragment.reflesh(recordsBean);


                                    }
                                }
                            } else {
                                Toast.makeText(context, "暂无拜访记录", Toast.LENGTH_LONG).show();
                            }
                        } else {
                            Toast.makeText(context, "拜访记录请求失败", Toast.LENGTH_LONG).show();
                        }


                        RecordHistoryDao testDao = AbstractAppDatabase.getDbDateHelper().getRecordHistoryDao();
                        List<RecordHistoryBean> listBean = testDao.getAllByVisitId(visit_id);
                        recordFragment.reflesh(listBean);
                    }

                    @Override
                    public void onFailure(Call<VisitRecords> call, Throwable t) {

                        if (dialog != null) {
                            dialog.dismiss();
                        }
                        Toast.makeText(context, "拜访记录请求失败", Toast.LENGTH_LONG).show();

                        RecordHistoryDao testDao = AbstractAppDatabase.getDbDateHelper().getRecordHistoryDao();

                        List<RecordHistoryBean> listBean = testDao.getAllByVisitId(visit_id);

                        recordFragment.reflesh(listBean);
                    }
                });
    }

    protected void doSwitch(int id) {

        Log.i(TAG, "doSwitch :" + id);
        switch (id) {
            case 0:

                this.tv_online_record.setTextColor(
                        this.selectColor);

                this.tv_local_record.setTextColor(
                        this.deselectTextColor);
                break;

            case 1:

                this.tv_online_record.setTextColor(
                        this.deselectTextColor);

                this.tv_local_record.setTextColor(
                        this.selectColor);
                break;

            default:
                break;
        }
    }

    /**
     * 初始化viewpager
     *
     * @param i 显示第i页内容
     */
    public void initViewPager(int i) {

        RecordHistoryDao testDao = AbstractAppDatabase.getDbDateHelper().getRecordHistoryDao();


        List<RecordHistoryBean> listBean = testDao.getAllByVisitId(visit_id);

        List<RecordDetailItem> itemList = new ArrayList<>();

        List<RecordDetailItem> itemList2 = new ArrayList<>();
        RecordDetailItem recordDetailItem;


        if (recordsBean != null) {
            List<VisitRecords.DataBean.RecordsBean.VoicesBean> voicesList = recordsBean.getVoices();
            for (VisitRecords.DataBean.RecordsBean.VoicesBean v : voicesList) {
                recordDetailItem = new RecordDetailItem();
                recordDetailItem.setType("0");

//http://ineutech.com:60001/xiaobaovisit/test/visit_voice/266_94e28fe4c44144f4ba7cc321e3e0f0e1_1600011900789.mp3
                recordDetailItem.setFilePath("http://ineutech.com:60001/xiaobaovisit/test/visit_voice/" + v.getVoice_file());
                recordDetailItem.setExtra(v.getVoice_id() + "");
                itemList.add(recordDetailItem);
            }
        }

        for (RecordHistoryBean r : listBean) {

            recordDetailItem = new RecordDetailItem();
            recordDetailItem.setFilePath(r.getFile_path());
            recordDetailItem.setFile_elpased(r.getFile_elpased());
            recordDetailItem.setType(r.getCloud() + "");

            if (recordDetailItem.getType().equals("1")) {
                itemList2.add(recordDetailItem);
            }


        }
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList("data", (ArrayList<? extends Parcelable>) itemList);
        bundle.putInt("visitId", visit_id);
        clouldRecordFragment = new CloudRecordFragment();
        clouldRecordFragment.setArguments(bundle);
        list.add(clouldRecordFragment);


        bundle = new Bundle();
        bundle.putParcelableArrayList("data", (ArrayList<? extends Parcelable>) itemList2);
        bundle.putInt("visitId", visit_id);

        recordFragment = new RecordFragment();
        recordFragment.setArguments(bundle);
        list.add(recordFragment);


        this.pager.setAdapter(
                new ContentFragmentPagerAdapter(this.getSupportFragmentManager(), list));


        pager.addOnPageChangeListener(new MyOnPageChangeListener());

        pager.setCurrentItem(i);
    }

    /**
     * 设置点击切换事件
     *
     * @param v
     */
    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.tv_online_record) {
            this.pager.setCurrentItem(0);
        } else if (id == R.id.tv_local_record) {
            this.pager.setCurrentItem(1);
        } else if (id == R.id.iv_back) {
            finish();
        } else if (id == R.id.iv_add) {
            try {
                Intent intent = new Intent(AudioRecordDetailActivity.this, AudioRecordActivity.class);
                intent.putExtra("visitId", visit_id);
                intent.putExtra("location", add);
                intent.putExtra("name", name);
                startActivity(intent);

                finish();
            } catch (Exception e) {
                Log.i(TAG, "add e:" + e);
            }
        }
    }


    /**
     * 页面切换监听事件
     */
    public class MyOnPageChangeListener implements ViewPager.OnPageChangeListener {

        @Override
        public void onPageSelected(int id) {
            doSwitch(id);
        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {
        }

        @Override
        public void onPageScrollStateChanged(int arg0) {
        }
    }
}
