package com.xiaobao.good.retrofit;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitUtils {


    private static Retrofit.Builder mBuilder = new Retrofit.Builder()
            .baseUrl("http://ineutech.com:60003/xiaobao/api/")
            .addConverterFactory(GsonConverterFactory.create());

    public static Retrofit.Builder getBuilderInstance() {
        return mBuilder;
    }

}
