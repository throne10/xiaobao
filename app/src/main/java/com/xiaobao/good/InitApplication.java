package com.xiaobao.good;

import android.app.Application;
import android.content.Context;

import com.xiaobao.good.db.AbstractAppDatabase;
import com.xiaobao.good.retrofit.RetrofitUtils;

public class InitApplication extends Application {
    private static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        context = this.getApplicationContext();
        AbstractAppDatabase.initDb(context);
        RetrofitUtils.initHttp();
    }

    public static Context getContext() {
        return context;
    }

}
