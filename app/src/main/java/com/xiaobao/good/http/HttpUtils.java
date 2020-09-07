package com.xiaobao.good.http;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class HttpUtils {
    private static Retrofit retrofit;
    private static HttpService service;

    public static HttpService getService() {
        return service;
    }

    public static void initHttp() {
        retrofit = new Retrofit.Builder()
                .baseUrl("http://ineutech.com:60003/xiaobao/api/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        service = retrofit.create(HttpService.class);
    }


}
