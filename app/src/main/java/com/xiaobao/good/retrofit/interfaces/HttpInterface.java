package com.xiaobao.good.retrofit.interfaces;

import com.xiaobao.good.retrofit.result.Clients;
import com.xiaobao.good.retrofit.result.UserInfoData;
import com.xiaobao.good.retrofit.result.WechatRecord;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Query;

public interface HttpInterface {

    @GET("client")
    Call<Clients> getClients(@Query("employeeId") String employeeId); // 获取客户列表

    @GET("login")
    Call<UserInfoData> login(@Query("loginName") String name, @Query("loginPwd") String pwd); // 登陆

    @POST("accurate")
    Call<WechatRecord> getWechatRecord(@Body MultipartBody imgs); // 获取微信聊天记录

    @POST("client")
    Call<Clients> postClient(@Query("clientInfo") String clientInfo); // 新增客户信息

    @PUT("client")
    Call<Clients> putClient(@Query("clientInfo") String clientInfo); // 修改客户信息

    @DELETE("client")
    Call<Clients> deleteClient(@Query("client_id") int id); // 删除客户信息
}
