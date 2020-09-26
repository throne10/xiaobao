package com.xiaobao.good.sign;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.google.gson.Gson;
import com.xiaobao.good.AudioRecordActivity;
import com.xiaobao.good.BaseActivity2;
import com.xiaobao.good.R;
import com.xiaobao.good.common.StringUtils;
import com.xiaobao.good.common.eventbus.LocationInfo;
import com.xiaobao.good.log.LogUtil;
import com.xiaobao.good.retrofit.RetrofitUtils;
import com.xiaobao.good.sign.location.XBdLocationListener;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnItemSelected;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignInActivity extends BaseActivity2 {
    @BindView(R.id.sp_purpose)
    Spinner spPurpose;
    @BindView(R.id.bt_record)
    Button btRecord;
    @BindView(R.id.bt_sign)
    Button btSign;
    private Context context;
    private String coorType = "bd09ll";
    private String TAG = "X_SignInActivity";
    private String addr = "";
    private boolean isSign = false;
    private String purpose = "展业";
    private int visitId;
    private String name = "unknown";
    private int clientId;
    private int employeeId;
    private LocationClient mClient;

    @OnClick(R.id.bt_back)
    public void back() {
        finish();
    }

    @BindView(R.id.tv_visit_name)
    TextView tvVisitName;

    @OnItemSelected(R.id.sp_purpose)
    public void purpose(int position) {
        String[] array = getResources().getStringArray(R.array.purpose);
        purpose = array[position];
    }

    @OnClick(R.id.bt_sign)
    public void sign() {
        if (isSign) {
            Toast.makeText(context, "您已签到成功。", Toast.LENGTH_LONG).show();
        }
        if (StringUtils.isEmpty(addr)) {
            Toast.makeText(context, "定位失败，请查看gps是否打开", Toast.LENGTH_LONG).show();
            startLocation();
            return;
        }

        Visit visit = new Visit();
        visit.setSign_address(addr);
        visit.setClient_id(clientId);
        visit.setEmployee_id(employeeId);
        visit.setPurpose(purpose);
        String s = new Gson().toJson(visit);
        LogUtil.i(TAG, "toJson>>>" + s);
        Call<VisitResult> visitResultCall = RetrofitUtils.getService().visit(visit);
        visitResultCall.enqueue(new Callback<VisitResult>() {
            @Override
            public void onResponse(Call<VisitResult> call, Response<VisitResult> response) {
                if (response.isSuccessful()) {
                    visitId = response.body().getData();
                    LogUtil.i(TAG, response.body().getData() + "");
                    Toast.makeText(context, "签到成功。", Toast.LENGTH_LONG).show();
                    btSign.setText("已签到");
                    btSign.setBackgroundResource(R.drawable.sign_btn_signed);
                    isSign = true;
                } else {
                    Toast.makeText(context, "签到请求失败", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<VisitResult> call, Throwable t) {
                Toast.makeText(context, "签到请求失败", Toast.LENGTH_LONG).show();
            }
        });
    }

    @OnClick(R.id.bt_record)
    public void record() {
        if (isSign) {
            Intent i = new Intent(context, AudioRecordActivity.class);
            i.putExtra("location", addr);
            i.putExtra("visitId", visitId);
            i.putExtra("name", name);
            context.startActivity(i);
            if (StringUtils.isEmpty(addr)) {
                Toast.makeText(context, "定位失败，请查看gps是否打开", Toast.LENGTH_LONG).show();
            }

            try {
                mClient.stop();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            Toast.makeText(context, "请先签到。", Toast.LENGTH_LONG).show();
        }
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        try {
            mClient.stop();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign);
        context = this;
        name = getIntent().getStringExtra("name");
        clientId = getIntent().getIntExtra("clientId", 0);
        employeeId = getIntent().getIntExtra("employeeId", 0);
        EventBus.getDefault().register(this);

        ButterKnife.bind(this);

        tvVisitName.setText(name);
        startLocation();

    }

    private void startLocation() {
         mClient = new LocationClient(context);
        LocationClientOption mOption = new LocationClientOption();
        mOption.setScanSpan(0);
        mOption.setCoorType(coorType);
        mOption.setIsNeedAddress(true);
        mOption.setOpenGps(true);
        mOption.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);
        mClient.setLocOption(mOption);
        XBdLocationListener xBdLocationListener = new XBdLocationListener();
        mClient.registerLocationListener(xBdLocationListener);
        mClient.start();
    }

    @Subscribe
    public void onEvent(LocationInfo result) {
        //接收以及处理数据
        Log.i(TAG, "result>>>" + result);
        if (StringUtils.isEmpty(addr)) {
            addr = result.getAddr();
        }
    }

}
