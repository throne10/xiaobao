package com.xiaobao.good.retrofit;

import com.xiaobao.good.retrofit.interfaces.HttpInterface;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import java.util.concurrent.TimeUnit;

public class RetrofitUtils {

    private static Retrofit retrofit;
    private static HttpInterface service;

    public static HttpInterface getService() {
        return service;
    }

    public static final OkHttpClient client = new OkHttpClient.Builder().
            connectTimeout(120, TimeUnit.SECONDS).
            readTimeout(120, TimeUnit.SECONDS).
            writeTimeout(120, TimeUnit.SECONDS).build();

    public static void initHttp() {
        retrofit = new Retrofit.Builder()
                .client(client)
                .baseUrl("http://ineutech.com:60003/xiaobao/api/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        service = retrofit.create(HttpInterface.class);
    }


}
