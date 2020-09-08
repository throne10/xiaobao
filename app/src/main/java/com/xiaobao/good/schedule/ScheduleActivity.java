package com.xiaobao.good.schedule;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;

import com.xiaobao.good.R;

import org.greenrobot.eventbus.EventBus;

import butterknife.ButterKnife;

public class ScheduleActivity extends Activity {
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule);
        context = this;
        EventBus.getDefault().register(this);
        ButterKnife.bind(this);

    }
}
