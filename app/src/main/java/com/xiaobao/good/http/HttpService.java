package com.xiaobao.good.http;


import com.xiaobao.good.http.bean.Clients;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface HttpService {

    @GET("client")
    Call<Clients> getClients(@Query("employeeId") String employeeId);
}