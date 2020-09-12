package com.xiaobao.good.retrofit.interfaces;

import com.xiaobao.good.retrofit.result.Clients;
import com.xiaobao.good.retrofit.result.UserInfoData;
import com.xiaobao.good.retrofit.result.VoiceContent;
import com.xiaobao.good.retrofit.result.WechatRecord;
import com.xiaobao.good.schedule.VisitRecords;
import com.xiaobao.good.sign.Visit;
import com.xiaobao.good.sign.VisitResult;
import com.xiaobao.good.wechat.WeChatResult;

import okhttp3.MultipartBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Query;

public interface HttpInterface {

    @GET("client")
    Call<Clients> getClients(@Query("employeeId") String employeeId); // 获取客户列表

    @GET("login")
    Call<UserInfoData> login(@Query("loginName") String name, @Query("loginPwd") String pwd); // 登陆

    @POST("accurate")
    Call<WechatRecord> getWechatRecord(@Body MultipartBody imgs); // 获取微信聊天记录

    @POST("client")
    Call<ResponseBody> postClient(@Body Clients.DataBean.ClientsBean clientInfo); // 新增客户信息

    @PUT("client")
    Call<ResponseBody> putClient(@Body Clients.DataBean.ClientsBean clientInfo); // 修改客户信息

    @DELETE("client")
    Call<Clients> deleteClient(@Query("client_id") int id); // 删除客户信息

    @POST("wechat")
    Call<ResponseBody> appendWechats(@Query("visit_id") int visitId, @Query("wechatContent") String weChatContent);//追加微信上报

    @GET("visit")
    Call<VisitRecords> getVisit(@Query("employeeId") int employeeId, @Query("clientId") int clientId); // 拜访记录


    @POST("visit")
    Call<VisitResult> visit(@Body Visit visit); // 新增拜訪

    @GET("wechat")
    Call<WeChatResult> getWechats(@Query("visit_id") int visit_id); // 获取微信聊天


    @Multipart
    @POST("uploadVoice")
    Call<ResponseBody> upload(@Query("visit_id") int visit_id,
                              @Part MultipartBody.Part file);

    @GET("voiceContent")
    Call<VoiceContent> getVoiceContent(@Query("voice_id") int voiceId);

}