package com.reigndesign;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.internal.LinkedTreeMap;
import com.reigndesign.retrofit.apiRoute.apiService;
import com.reigndesign.retrofit.model.hits;
import com.reigndesign.retrofit.responseGeneral.responseGeneral;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Query;

public class MainActivity extends AppCompatActivity {

    public static final String TAG = "JSON_BODY";
    private static String android = "android";

    private Retrofit mRetrofit;
    private apiService mService;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        buildRetrofit();

        Call<responseGeneral> generalResponse = mService.listNews(android);

        generalResponse.enqueue(new Callback<responseGeneral>() {
            @Override
            public void onResponse(Call<responseGeneral> call, Response<responseGeneral> response) {

                if (response.isSuccessful())
                {
                    responseGeneral mCallResponse = response.body();
                    Log.d(TAG, response.body().toString());
                    Toast.makeText(getBaseContext(),"aqui estoy",Toast.LENGTH_LONG).show();

                    List<Object> objectResponse = (List<Object>) mCallResponse.getObject();
                    List<hits> mHits = new ArrayList<hits>();

                    for(Object mList: objectResponse)
                    {
                        String mJson = new Gson().toJson(mList);
                        hits model = new Gson().fromJson(mJson, hits.class);
                        String author = model.getAuthor();
                        Log.d("AUTHOR",author);
                    }

                }
            }

            @Override
            public void onFailure(Call<responseGeneral> call, Throwable t) {

            }
        });
    }

    public void buildRetrofit()
    {
        // Intercept http
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        final OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(interceptor)
                .readTimeout(120, TimeUnit.SECONDS)
                .connectTimeout(120, TimeUnit.SECONDS)
                .build();

        mRetrofit = new Retrofit.Builder()
                .baseUrl(getString(R.string.URL_BASE))
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build();

        mService = mRetrofit.create(apiService.class);
    }


}
