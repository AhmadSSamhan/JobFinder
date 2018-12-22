package com.android.jobfinder.network;


import com.android.jobfinder.helper.ConstantApp;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.android.jobfinder.helper.ConstantApp.REQ_TIMEOUT;


public class RetrofitInstance {
    private static Retrofit retrofit = null;
    private static OkHttpClient okHttpClient2 = null;

    /**
     * Create an instance of Retrofit object
     * Handel Dynamic Url in interface to use dynamic provider
     */
    public static Retrofit getClient() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    //Get default url
                    .baseUrl(ConstantApp.BASE_URL_GITHUB)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(getOkHttpClient())
                    .build();
        }
        return retrofit;
    }

    /**
     * Create an instance of OkHttpClient object
     */
    private static OkHttpClient getOkHttpClient() {
        if (okHttpClient2 == null) {
            okHttpClient2 = new OkHttpClient.Builder()
                    .connectTimeout(REQ_TIMEOUT, TimeUnit.SECONDS)
                    .readTimeout(REQ_TIMEOUT, TimeUnit.SECONDS)
                    .build();
        }
        return okHttpClient2;
    }
}
