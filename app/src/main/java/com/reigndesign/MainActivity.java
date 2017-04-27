package com.reigndesign;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.gson.Gson;
import com.reigndesign.adapters.hitsAdapter;
import com.reigndesign.adapters.realmModelAdapter;
import com.reigndesign.model.modelHits;
import com.reigndesign.realm.hitsRealm;
import com.reigndesign.retrofit.apiRoute.apiService;
import com.reigndesign.retrofit.controllers.routesController;
import com.reigndesign.retrofit.model.Hits;
import com.reigndesign.retrofit.responseGeneral.responseGeneral;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.realm.Realm;
import io.realm.RealmResults;
import io.realm.internal.Context;
import io.realm.processor.Utils;
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
    private Paint p = new Paint();
    private View view;
    private Realm realm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //get realm instance
        this.realm = hitsRealm.with(this).getRealm();

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_layout);
        swipeRefreshLayout.setOnRefreshListener(this);

        mListHist = new ArrayList<>();

        //hitsRealm.with(this).refresh();
        //setRealmAdapter(hitsRealm.with(this).findAll());
        //mAdapter = new hitsAdapter(this);
        setupRecycler();

        if(utils.isConnected(this)){
                swipeRefreshLayout.post(new Runnable() {
                                            @Override
                                            public void run() {
                                                swipeRefreshLayout.setRefreshing(true);
                                                fetchHits();
                                            }
                                        }
                );
        }else {
            hitsRealm.with(this).refresh();
            setRealmAdapter(hitsRealm.with(this).findAll());
        }



        initSwipe();


    }

    @Override
    public void onRefresh() {
        mListHist.clear();
        fetchHits();
    }

    private void setupRecycler() {

        recyclerView.setHasFixedSize(true);

        // use a linear layout manager since the cards are vertically scrollable
        final LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        // create an empty adapter and add it to the recycler view
        mAdapter = new hitsAdapter(this);
        recyclerView.setAdapter(mAdapter);

    }

    public void fetchHits()
    {

        // showing refresh animation before making http call
        swipeRefreshLayout.setRefreshing(true);

        routesController mListHits = new routesController();
        mListHits.listHits(android, new routesController.hitsCallback() {
            @Override
            public void onResponse(List<Object> response) {
                    String title,author, createdAt, url;
                    int id;
                    List<Object> objectResponse = (List<Object>) response;
                    List<Hits> mHits = new ArrayList<Hits>();

                    if(objectResponse.size()>0)
                    {
                        realm.commitTransaction();
                        realm.beginTransaction();
                    }

                    for(Object mList: objectResponse)
                    {
                        String mJson = new Gson().toJson(mList);
                        Hits model = new Gson().fromJson(mJson, Hits.class);
                        id = model.getObjectID();
                        author = model.getAuthor();
                        createdAt = model.getCreatedAt();
                        if(model.getTitle()==null)
                        {
                            title = model.getStoryTitle();
                        }
                        else
                        {
                            title = model.getTitle();
                        }

                        if(model.getUrl()==null)
                        {
                            url = model.getStoryUrl();
                        }
                        else {
                            url = model.getUrl();
                        }



                        if (!hitsRealm.with(MainActivity.this).isEmpty()) {
                            realm.commitTransaction();
                            setData(id,author,createdAt,title,url);
                        }
                        else{
                            modelHits mh;
                            mh = hitsRealm.with(MainActivity.this).findById(id);
                            if (mh == null) {
                                realm.commitTransaction();
                                setData(id,author,createdAt,title,url);
                            }
                        }

                        //setData(hits);


                        //mListHist.add(modelHits);

                        Log.d("HITS",   String.valueOf(id) + " " +
                                        author + " " +
                                        createdAt + " " +
                                        "TITLE: " + title + " " +
                                        "URL: " + url + " " +
                                        "STORY_TITLE: " + model.getStoryTitle() + " " +
                                        "STORY_URL: " + model.getStoryUrl());
                    }

                    if(objectResponse.size()>0)
                    {
                        realm.commitTransaction();
                        realm.beginTransaction();
                    }

                hitsRealm.with(MainActivity.this).refresh();
                setRealmAdapter(hitsRealm.with(MainActivity.this).findAll());

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

    public void setData(int id, String author, String createdAt, String title, String url)
    {
        modelHits hits = new modelHits();
        hits.setObjectID(id);
        hits.setAuthor(author);
        hits.setCreatedAt(createdAt);
        hits.setTitle(title);
        hits.setUrl(url);
        hitsRealm.with(MainActivity.this).add(hits);
    }

    public void setRealmAdapter(RealmResults<modelHits> hits) {

        realmModelAdapter realmAdapter = new realmModelAdapter(this.getApplicationContext(), hits, true);
        mAdapter.setRealmAdapter(realmAdapter);
        mAdapter.notifyDataSetChanged();
    }

    private void initSwipe(){
        ItemTouchHelper.SimpleCallback simpleItemTouchCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {

            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                int position = viewHolder.getAdapterPosition();

                if (direction == ItemTouchHelper.LEFT){
                    mAdapter.removeItem(position);
                }
            }

            @Override
            public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {

                Bitmap icon = null;
                if(actionState == ItemTouchHelper.ACTION_STATE_SWIPE){

                    View itemView = viewHolder.itemView;
                    float height = (float) itemView.getBottom() - (float) itemView.getTop();
                    float width = height / 3;

                        p.setColor(getResources().getColor(R.color.red));
                        RectF background = new RectF((float) itemView.getRight() + dX, (float) itemView.getTop(),(float) itemView.getRight(), (float) itemView.getBottom());
                        c.drawRect(background,p);

                        Drawable drawable = getResources().getDrawable(R.drawable.ic_delete_black_24dp);
                        icon = drawableToBitmap(drawable);

                        //icon = BitmapFactory.decodeResource(getBaseContext().getResources(), bitmap);
                        RectF icon_dest = new RectF((float) itemView.getRight() - 2*width ,(float) itemView.getTop() + width,(float) itemView.getRight() - width,(float)itemView.getBottom() - width);
                        c.drawBitmap(icon,null,icon_dest,p);

                }
                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
            }
        };
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleItemTouchCallback);
        itemTouchHelper.attachToRecyclerView(recyclerView);
    }
    private void removeView(){
        if(view.getParent()!=null) {
            ((ViewGroup) view.getParent()).removeView(view);
        }
    }

    public static Bitmap drawableToBitmap (Drawable drawable) {

        if (drawable instanceof BitmapDrawable) {
            return ((BitmapDrawable)drawable).getBitmap();
        }

        Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);

        return bitmap;
    }

}
