package com.xiaobao.good.schedule;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;

import com.xiaobao.good.R;
import com.xiaobao.good.log.LogUtil;
import com.xiaobao.good.retrofit.RetrofitUtils;
import com.xiaobao.good.sign.SignInActivity;
import com.xiaobao.good.wechat.WechatRecordActivity;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnItemClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ScheduleActivity extends Activity {
    @BindView(R.id.lv_time_line)
    ListView listTimeLine;
    @BindView(R.id.bt_add_schedule)
    Button btAddSchedule;
    @BindView(R.id.iv_wechat)
    ImageView ivWechat;
    @BindView(R.id.bt_back)
    Button btBack;
    private Context context;
    private String TAG = "X_ScheduleActivity";
    private List<VisitRecords.DataBean.RecordsBean> recordsBeans;

    @OnItemClick(R.id.lv_time_line)
    public void timeLineClick(int p) {
        VisitRecords.DataBean.RecordsBean s = recordsBeans.get(p);
        if (s.getPurpose().equals("展业")) {
        }
        if (s.getPurpose().equals("送礼品")) {
        }
        if (s.getPurpose().equals("递送保单")) {
        }
        if (s.getPurpose().equals("微信聊天")) {
            Intent i = new Intent(context, WechatRecordActivity.class);
            i.putExtra("visitId", s.getVisit_id());
            context.startActivity(i);
        }
    }

    @OnClick(R.id.iv_wechat)
    public void wechat() {
        Intent i = new Intent(context, WechatRecordActivity.class);
        context.startActivity(i);
    }

    @OnClick(R.id.bt_back)
    public void back() {
        finish();
    }

    @OnClick(R.id.bt_add_schedule)
    public void schedule() {
        Intent i = new Intent(context, SignInActivity.class);
        context.startActivity(i);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule);
        context = this;
        ButterKnife.bind(this);
        Call<VisitRecords> visitRecordsCall = RetrofitUtils.getService().getVisit(4);

        visitRecordsCall.enqueue(new Callback<VisitRecords>() {
            @Override
            public void onResponse(Call<VisitRecords> call, Response<VisitRecords> response) {
                if (response.isSuccessful()) {
                    recordsBeans = response.body().getData().getRecords();
                    LogUtil.i(TAG, "recordsBeans>>>>" + recordsBeans);
                    listTimeLine.setAdapter(new TimeLineAdapter(recordsBeans));
                }
            }

            @Override
            public void onFailure(Call<VisitRecords> call, Throwable t) {

            }
        });


    }
}
