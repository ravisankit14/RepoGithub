package com.repogithub.restapicall;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RestClientApi {

    private static Retrofit mRetrofit;
    private static Retrofit mRetrofitRepo;

    private final static String BASE_URL = "https://api.github.com/";

    public static Retrofit getClient(){
        if(mRetrofit == null){
            mRetrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    //.client(client)
                    .build();
        }
        return mRetrofit;
    }

    public static Retrofit getClientRepo(){
        if(mRetrofitRepo == null){
            mRetrofitRepo = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return mRetrofitRepo;
    }
}
