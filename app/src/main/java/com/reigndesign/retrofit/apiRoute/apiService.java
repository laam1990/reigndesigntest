package com.reigndesign.retrofit.apiRoute;

import com.reigndesign.retrofit.responseGeneral.responseGeneral;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Query;

/**
 * Created by Luis Adrian on 24/04/2017.
 */

public interface apiService {

    @Headers("Content-Type: application/json")
    @GET("search_by_date")
    Call<responseGeneral> listNews(
            @Query(value = "query") String android
    );
}
