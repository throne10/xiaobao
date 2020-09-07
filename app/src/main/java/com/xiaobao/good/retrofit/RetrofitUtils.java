package com.xiaobao.good.retrofit;

import com.xiaobao.good.retrofit.interfaces.HttpInterface;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitUtils {

    private static Retrofit retrofit;
    private static HttpInterface service;

    public static HttpInterface getService() {
        return service;
    }

    public static void initHttp() {
        retrofit = new Retrofit.Builder()
                .baseUrl("http://ineutech.com:60003/xiaobao/api/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        service = retrofit.create(HttpInterface.class);
    }


}
