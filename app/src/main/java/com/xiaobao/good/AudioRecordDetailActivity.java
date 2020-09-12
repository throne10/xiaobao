package com.xiaobao.good;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager.widget.ViewPager;

import com.google.gson.Gson;
import com.xiaobao.good.db.AbstractAppDatabase;
import com.xiaobao.good.db.RecordHistoryBean;
import com.xiaobao.good.db.dao.RecordHistoryDao;
import com.xiaobao.good.record.RecordDetailItem;
import com.xiaobao.good.record.fragment.ContentFragmentPagerAdapter;
import com.xiaobao.good.record.fragment.RecordFragment;
import com.xiaobao.good.schedule.VisitRecords;

import java.util.ArrayList;
import java.util.List;

public class AudioRecordDetailActivity extends FragmentActivity implements View.OnClickListener {


    private static final String TAG = "record_detail";
    private ViewPager pager;
    private List<Fragment> list;

    private TextView tv_online_record;
    private TextView tv_local_record;

    /**
     * 选中选项卡颜色
     */
    private int selectColor = Color.parseColor("#FF9100");
    /**
     * 未选中选项颜色
     */
    private int deselectTextColor = Color.parseColor("#ffffff");
    private VisitRecords.DataBean.RecordsBean recordsBean;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_record_detail);

        //初始化Fragment数组
        pager = (ViewPager) this.findViewById(R.id.pager);
        list = new ArrayList<>();

        tv_online_record = this.findViewById(R.id.tv_online_record);
        tv_local_record = this.findViewById(R.id.tv_local_record);

        tv_online_record.setOnClickListener(this);
        tv_local_record.setOnClickListener(this);
        String s = getIntent().getStringExtra("date");

        recordsBean = new Gson().fromJson(s, VisitRecords.DataBean.RecordsBean.class);

        Log.i(TAG, "recordsBean>>>" + recordsBean.getVoices().get(0).getVoice_file());
        initViewPager(0);


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


        List<RecordHistoryBean> listBean = testDao.getAll();

        List<RecordDetailItem> itemList = new ArrayList<>();

        List<RecordDetailItem> itemList2 = new ArrayList<>();
        RecordDetailItem recordDetailItem;


        for (RecordHistoryBean r : listBean) {

            recordDetailItem = new RecordDetailItem();
            recordDetailItem.setFilePath(r.getFile_path());
            recordDetailItem.setFile_elpased(r.getFile_elpased());
            recordDetailItem.setType(r.getCloud() + "");

            if (recordDetailItem.getType().equals("0")) {
                itemList.add(recordDetailItem);
            } else {
                itemList2.add(recordDetailItem);
            }


        }
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList("data", (ArrayList<? extends Parcelable>) itemList);
        RecordFragment recordFragment = new RecordFragment();
        recordFragment.setArguments(bundle);
        list.add(recordFragment);


        bundle = new Bundle();
        bundle.putParcelableArrayList("data", (ArrayList<? extends Parcelable>) itemList2);
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
        }
    }


    /**
     * 页面切换监听事件
     *
     * @author Justsy
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
