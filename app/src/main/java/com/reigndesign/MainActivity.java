package com.reigndesign;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;
import com.reigndesign.adapters.hitsAdapter;
import com.reigndesign.model.modelHits;
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

public class MainActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {

    public static final String TAG = "JSON_BODY";
    private static String android = "android";

    private RecyclerView recyclerView;
    private List<modelHits> mListHist;
    private hitsAdapter mAdapter;
    private SwipeRefreshLayout swipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_layout);

        mListHist = new ArrayList<>();
        mAdapter = new hitsAdapter(this, mListHist);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);

        swipeRefreshLayout.setOnRefreshListener(this);

        swipeRefreshLayout.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        swipeRefreshLayout.setRefreshing(true);
                                        fetchHits();
                                    }
                                }
        );





    }

    @Override
    public void onRefresh() {
        mListHist.clear();
        fetchHits();
    }

    public void fetchHits()
    {
        // showing refresh animation before making http call
        swipeRefreshLayout.setRefreshing(true);

        routesController mListHits = new routesController();
        mListHits.listHits(android, new routesController.hitsCallback() {
            @Override
            public void onResponse(List<Object> response) {

                List<Object> objectResponse = (List<Object>) response;
                List<Hits> mHits = new ArrayList<Hits>();

                for(Object mList: objectResponse)
                {
                    String mJson = new Gson().toJson(mList);
                    Hits model = new Gson().fromJson(mJson, Hits.class);
                    int id = model.getObjectID();
                    String author = model.getAuthor();
                    String createdAt = model.getCreatedAt();
                    String title = model.getTitle();
                    String storyTitle = model.getStoryTitle();
                    String url = model.getUrl();
                    String storyUrl = model.getStoryUrl();
                    modelHits modelHits = new modelHits(title,storyTitle,id,createdAt,author,url,storyUrl);
                    mListHist.add(modelHits);

                    Log.d("HITS",   String.valueOf(id) + " " +
                                    author + " " +
                                    createdAt + " " +
                                    "TITLE: " + title + " " +
                                    "URL: " + url + " " +
                                    "STORY_TITLE: " + storyTitle + " " +
                                    "STORY_URL: " + storyUrl);
                }

                mAdapter.notifyDataSetChanged();

                // stopping swipe refresh
                swipeRefreshLayout.setRefreshing(false);

            }

            @Override
            public void onFailure(String mensajeError) {
                Toast.makeText(getApplicationContext(), mensajeError, Toast.LENGTH_LONG).show();
                // stopping swipe refresh
                swipeRefreshLayout.setRefreshing(false);
            }
        });
    }

}
