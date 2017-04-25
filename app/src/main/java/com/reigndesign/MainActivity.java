package com.reigndesign;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;
import com.reigndesign.retrofit.apiRoute.apiService;
import com.reigndesign.retrofit.controllers.routesController;
import com.reigndesign.retrofit.model.Hits;
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

public class MainActivity extends AppCompatActivity {

    public static final String TAG = "JSON_BODY";
    private static String android = "android";

    private Retrofit mRetrofit;
    private apiService mService;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        routesController mListHits = new routesController();
        mListHits.listHits(android, new routesController.hitsCallback() {
            @Override
            public void onResponse(List<Object> response) {

                    Toast.makeText(getBaseContext(),"aqui estoy",Toast.LENGTH_LONG).show();

                    List<Object> objectResponse = (List<Object>) response;
                    List<Hits> mHits = new ArrayList<Hits>();

                    for(Object mList: objectResponse)
                    {
                        String mJson = new Gson().toJson(mList);
                        Hits model = new Gson().fromJson(mJson, Hits.class);
                        String author = model.getAuthor();
                        Log.d("AUTHOR",author);
                    }

                }

            @Override
            public void onFailure(String mensajeError) {

            }
        });

    }

}
