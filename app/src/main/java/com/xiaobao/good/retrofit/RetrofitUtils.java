package com.xiaobao.good.retrofit;

import com.xiaobao.good.retrofit.interfaces.HttpInterface;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitUtils {

    private static Retrofit retrofit;
    private static HttpInterface service;
    private static OkHttpClient client;

    public static HttpInterface getService() {
        return service;
    }

    public static void initHttp() {
        client = new OkHttpClient.Builder().
                connectTimeout(40, TimeUnit.SECONDS).
                readTimeout(60, TimeUnit.SECONDS).
                writeTimeout(120, TimeUnit.SECONDS).build();

        retrofit = new Retrofit.Builder()
                .baseUrl("http://ineutech.com:60003/xiaobao/api/")
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        service = retrofit.create(HttpInterface.class);
    }


}
