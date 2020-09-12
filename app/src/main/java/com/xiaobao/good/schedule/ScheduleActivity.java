package com.xiaobao.good.schedule;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.xiaobao.good.AudioRecordDetailActivity;
import com.xiaobao.good.ClientActivity;
import com.xiaobao.good.R;
import com.xiaobao.good.common.StringUtils;
import com.xiaobao.good.common.eventbus.ClientUpdate;
import com.xiaobao.good.log.LogUtil;
import com.xiaobao.good.retrofit.RetrofitUtils;
import com.xiaobao.good.retrofit.result.Clients;
import com.xiaobao.good.sign.SignInActivity;
import com.xiaobao.good.wechat.WechatRecordActivity;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnItemClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ScheduleActivity extends Activity {

    @OnClick(R.id.iv_head_img)
    public void clickHead() {
        if (StringUtils.isNotEmpty(strClientBean)) {
            Intent intent = new Intent(getApplicationContext(), ClientActivity.class);
            LogUtil.d(TAG, "clickHead ClientBean > " + strClientBean);
            intent.putExtra("ClientBean", strClientBean);
            startActivity(intent);
        } else {
            Toast.makeText(getApplicationContext(), "获取客户信息失败", Toast.LENGTH_SHORT).show();
        }
    }

    @BindView(R.id.lv_time_line)
    ListView listTimeLine;

    @BindView(R.id.bt_add_schedule)
    Button btAddSchedule;

    @BindView(R.id.iv_wechat)
    ImageView ivWechat;

    @BindView(R.id.bt_back)
    Button btBack;

    @BindView(R.id.tv_name)
    TextView tvName;

    @BindView(R.id.tv_phone_no)
    TextView tvPhoneNo;

    @BindView(R.id.tv_visit_time)
    TextView tvVisitCount;

    @BindView(R.id.tv_last_visit_time)
    TextView tvVisitTime;

    @BindView(R.id.tv_last_visit_key)
    TextView tvVisitKey;

    @BindView(R.id.tv_client_label)
    TextView tvClientLabel;

    private Context context;
    private String TAG = "X_ScheduleActivity";
    private List<VisitRecords.DataBean.RecordsBean> recordsBeans;

    @OnItemClick(R.id.lv_time_line)
    public void timeLineClick(int p) {
        VisitRecords.DataBean.RecordsBean s = recordsBeans.get(p);
        if (s.getPurpose().equals("展业")) {
            Intent i = new Intent(context, AudioRecordDetailActivity.class);
            i.putExtra("visitId", s.getVisit_id());
            i.putExtra("name", intentClient.getClient_name());


            i.putExtra("date", new Gson().toJson(recordsBeans.get(p)));
            context.startActivity(i);
        }
        if (s.getPurpose().equals("送礼品")) {

            Intent i = new Intent(context, AudioRecordDetailActivity.class);
            i.putExtra("name", intentClient.getClient_name());

            i.putExtra("visitId", s.getVisit_id());

            i.putExtra("date", new Gson().toJson(recordsBeans.get(p)));
            context.startActivity(i);
        }
        if (s.getPurpose().equals("递送保单")) {
            Intent i = new Intent(context, AudioRecordDetailActivity.class);
            i.putExtra("name", intentClient.getClient_name());

            i.putExtra("visitId", s.getVisit_id());

            i.putExtra("date", new Gson().toJson(recordsBeans.get(p)));
            context.startActivity(i);
        }
        if (s.getPurpose().equals("微信聊天")) {
            Intent i = new Intent(context, WechatRecordActivity.class);
            i.putExtra("visitId", s.getVisit_id());
            i.putExtra("employeeId", intentClient.getEmployee_id());
            i.putExtra("clientId", intentClient.getClient_id());
            Log.i("yxd123", "intentClient)>>>" + intentClient);
            context.startActivity(i);
        }
    }

    @OnClick(R.id.iv_wechat)
    public void wechat() {
        Intent i = new Intent(context, WechatRecordActivity.class);
        i.putExtra("employeeId", intentClient.getEmployee_id());
        i.putExtra("clientId", intentClient.getClient_id());
        context.startActivity(i);
    }

    @OnClick(R.id.bt_back)
    public void back() {
        finish();
    }

    @OnClick(R.id.bt_add_schedule)
    public void schedule() {
        Intent i = new Intent(context, SignInActivity.class);
        i.putExtra("name", intentClient.getClient_name());
        i.putExtra("clientId", intentClient.getClient_id());
        i.putExtra("employeeId", intentClient.getEmployee_id());
        context.startActivity(i);
    }

    private Clients.DataBean.ClientsBean intentClient;
    private String strClientBean;

    @Override
    protected void onResume() {
        super.onResume();
        initData();
    }

    private void initData() {
        Gson gson = new Gson();
        if (StringUtils.isNotEmpty(strClientBean)) {
            intentClient = gson.fromJson(strClientBean, Clients.DataBean.ClientsBean.class);
        }
        tvName.setText(intentClient.getClient_name());
        tvPhoneNo.setText(intentClient.getClient_phone());
        tvVisitKey.setText("-");
        tvClientLabel.setText(intentClient.getClient_label());

        getVisitRecords();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule);
        context = this;
        ButterKnife.bind(this);
        EventBus.getDefault().register(this);
        strClientBean = getIntent().getStringExtra("ClientBean");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void onEvent(ClientUpdate clientUpdate) {
        if (clientUpdate != null && clientUpdate.isUpdate()) {
            strClientBean = clientUpdate.getClientInfo();
            LogUtil.d(TAG, "strClientBean > " + strClientBean);
        }
    }

    private void getVisitRecords() {
        Call<VisitRecords> visitRecordsCall =
                RetrofitUtils.getService()
                        .getVisit(intentClient.getEmployee_id(), intentClient.getClient_id());

        visitRecordsCall.enqueue(
                new Callback<VisitRecords>() {
                    @Override
                    public void onResponse(
                            Call<VisitRecords> call, Response<VisitRecords> response) {
                        if (response.isSuccessful()) {
                            recordsBeans = response.body().getData().getRecords();
                            LogUtil.i(TAG, "recordsBeans>>>>" + recordsBeans);
                            if (!recordsBeans.isEmpty()) {
                                listTimeLine.setAdapter(new TimeLineAdapter(recordsBeans));
                                tvVisitCount.setText(recordsBeans.size() + "");
                                tvVisitTime.setText(recordsBeans.get(0).getVisit_time());
                            } else {
                                Toast.makeText(context, "暂无拜访记录", Toast.LENGTH_LONG).show();
                            }
                        } else {
                            Toast.makeText(context, "拜访记录请求失败", Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<VisitRecords> call, Throwable t) {
                        Toast.makeText(context, "拜访记录请求失败", Toast.LENGTH_LONG).show();
                    }
                });
    }
}
