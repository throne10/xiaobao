package com.xiaobao.good.retrofit.interfaces;


import com.xiaobao.good.retrofit.result.Clients;
import com.xiaobao.good.retrofit.result.UserInfoData;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface HttpInterface {

    @GET("client")
    Call<Clients> getClients(@Query("employeeId") String employeeId);

    @GET("login")
    Call<UserInfoData> login(@Query("loginName") String name, @Query("loginPwd") String pwd);//登陆
}