package com.xiaobao.good.sign;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;

import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.soundcloud.android.crop.Crop;
import com.xiaobao.good.R;
import com.xiaobao.good.common.eventbus.LocationInfo;
import com.xiaobao.good.sign.location.XBdLocationListener;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

public class SignInActivity extends Activity {
    private Context context;
    private String coorType = "bd09ll";
    private String TAG = "X_SignInActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign);
        context = this;
        Crop.pickImage(this);
        EventBus.getDefault().register(this);

        startLocation();
    }

    private void startLocation() {
        LocationClient mClient = new LocationClient(context);
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
    }

}
