package com.xiaobao.good.retrofit.interfaces;

import android.app.Application;
import android.content.Context;

import com.xiaobao.good.db.AbstractAppDatabase;
import com.xiaobao.good.http.bean.Clients;
import com.xiaobao.good.retrofit.result.UserInfoData;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface LoginInterface {


    @GET("login")
    Call<UserInfoData> login(@Query("loginName") String name, @Query("loginPwd") String pwd);//登陆

    class InitApplication extends Application {
        private static Context context;

        @Override
        public void onCreate() {
            super.onCreate();
            context = this.getApplicationContext();
            AbstractAppDatabase.initDb(context);
        }

        public static Context getContext() {
            return context;
        }

    }
}
