package com.reigndesign.retrofit;

import com.reigndesign.R;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Luis Adrian on 24/04/2017.
 */

public class BaseController {
    public Retrofit retrofit;
    public static final String URL_BASE = "https://hn.algolia.com/api/v1/";

    public BaseController(){
    // Intercept http
    HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
    interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

    final OkHttpClient client = new OkHttpClient.Builder()
            .addInterceptor(interceptor)
            .readTimeout(120, TimeUnit.SECONDS)
            .connectTimeout(120, TimeUnit.SECONDS)
            .build();

    retrofit = new Retrofit.Builder()
            .baseUrl(URL_BASE)
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build();
    }
}
