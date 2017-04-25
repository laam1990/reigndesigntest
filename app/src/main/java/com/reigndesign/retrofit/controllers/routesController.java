package com.reigndesign.retrofit.controllers;

import android.util.Log;

import com.google.gson.JsonObject;
import com.reigndesign.retrofit.apiRoute.apiService;
import com.reigndesign.retrofit.baseController;
import com.reigndesign.retrofit.responseGeneral.responseGeneral;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Luis Adrian on 24/04/2017.
 */

public class routesController extends baseController {

    private apiService mService;
    private Call<responseGeneral> generalResponse;


    public routesController(){
        mService = mRetrofit.create(apiService.class);
    }

    public void listHits(String query, final hitsCallback callback)
    {
        generalResponse = mService.listNews(query);
        call(callback);
    }

    public void call(final hitsCallback callback )
    {
        generalResponse.enqueue(new Callback<responseGeneral>() {
            @Override
            public void onResponse(Call<responseGeneral> call, Response<responseGeneral> response) {
                // http response status code + headers
                Log.d("Response url: ", call.request().url().toString());

                // http response status code + headers
                Log.d("Response status code: ", String.valueOf(response.code()));

                // isSuccess is true if response code => 200 and <= 300
                if (!response.isSuccessful()) {
                    // print response body if unsuccessful
                    try {
                        callback.onFailure(response.errorBody().string());
                        Log.e("Error: ", response.errorBody().string());
                    } catch (Exception e) {
                        // do nothing
                        Log.e("Error: ", e.getMessage());

                    }
                    return;
                }

                callback.onResponse(response.body().getObject());
            }

            @Override
            public void onFailure(Call<responseGeneral> call, Throwable t) {
                Log.e("Url Error: ", call.request().url().toString());
                Log.e("Url Mensaje: ", t.getMessage());

                callback.onFailure(t.getMessage());
            }
        });
    }

    public interface hitsCallback {
        void onResponse(List<Object> response);
        void onFailure(String mensajeError);
    }
}
