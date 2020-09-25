package com.xiaobao.good;

import android.app.Activity;
import android.content.res.Configuration;
import android.content.res.Resources;

public class BaseActivity2 extends Activity {

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
}
