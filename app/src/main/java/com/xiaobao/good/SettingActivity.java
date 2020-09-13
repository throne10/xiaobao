package com.xiaobao.good;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.xiaobao.good.common.CommonUtils;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class SettingActivity extends Activity {
    @OnClick(R.id.bt_back)
    public void back() {
        finish();
    }

    @OnClick({R.id.ll_exit, R.id.ll_version})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_exit:
                try {
                    Intent i = new Intent(this, LoginActivity.class);
                    this.startActivity(i);
                    finish();
                    BaseActivity.finishBase();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case R.id.ll_version:
                try {
                    Toast.makeText(this, "当前版本：" + CommonUtils.getVersionName(this), Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        ButterKnife.bind(this);
    }
}
