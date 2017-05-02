package com.reigndesign;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.gson.Gson;
import com.reigndesign.adapters.HitsAdapter;
import com.reigndesign.adapters.RealmModelAdapter;
import com.reigndesign.model.Hits;
import com.reigndesign.realm.HitsRealmController;
import com.reigndesign.retrofit.controllers.RoutesController;
import com.reigndesign.retrofit.model.HitsService;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;

public class MainActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {

    private static final String TAG = "HITS_OBJECT";
    private static String ANDROID = "android";

    private RecyclerView recyclerView;
    private HitsAdapter mAdapter;
    private SwipeRefreshLayout swipeRefreshLayout;
    private Paint p = new Paint();
    private View view;
    private Realm realm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //get realm instance
        this.realm = HitsRealmController.with(this).getRealm();

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_layout);
        swipeRefreshLayout.setOnRefreshListener(this);

        setupRecycler();

        if(Utils.isConnected(this)){
                swipeRefreshLayout.post(new Runnable() {
                                            @Override
                                            public void run() {
                                                swipeRefreshLayout.setRefreshing(true);
                                                fetchHits();
                                            }
                                        }
                );
        }else {
            HitsRealmController.with(this).refresh();
            setRealmAdapter(HitsRealmController.with(this).findAll());
        }

        initSwipe();

    }

    @Override
    public void onRefresh() {
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
        mAdapter = new HitsAdapter(this);
        recyclerView.setAdapter(mAdapter);

    }

    private void fetchHits()
    {

        // showing refresh animation before making http call
        swipeRefreshLayout.setRefreshing(true);

        RoutesController mListHits = new RoutesController();
        mListHits.listHits(ANDROID, new RoutesController.hitsCallback() {
            @Override
            public void onResponse(List<Object> response) {
                    String  title;
                    String author;
                    String createdAt;
                    String url;
                    int id;

                    for(Object mList: response)
                    {
                        String mJson = new Gson().toJson(mList);
                        HitsService model = new Gson().fromJson(mJson, HitsService.class);
                        //set variables
                        id = model.getObjectID();
                        author = model.getAuthor();
                        createdAt = model.getCreatedAt();
                        if(model.getTitle()==null) {
                            title = model.getStoryTitle();
                        }
                        else {
                            title = model.getTitle();
                        }

                        if(model.getUrl()==null) {
                            url = model.getStoryUrl();
                        }
                        else {
                            url = model.getUrl();
                        }
                        //Check that the database is empty or not.
                        if (!HitsRealmController.with(MainActivity.this).isEmpty()) {
                            realm.commitTransaction();
                            setData(id,author,createdAt,title,url);
                        }
                        else{
                            Hits mh;
                            mh = HitsRealmController.with(MainActivity.this).findById(id);
                            if (mh == null) {
                                realm.commitTransaction();
                                setData(id,author,createdAt,title,url);
                            }
                        }
                        //Log is to view Objects
                        Log.d(TAG,      "ID: " + String.valueOf(id) + " " +
                                        "AUTHOR: " + author + " " +
                                        "CREATED_AT: " + createdAt + " " +
                                        "TITLE: " + title + " " +
                                        "URL: " + url);
                    }

                HitsRealmController.with(MainActivity.this).refresh();
                setRealmAdapter(HitsRealmController.with(MainActivity.this).findAll());

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

    private void setData(int id, String author, String createdAt, String title, String url)
    {
        Hits hits = new Hits();
        hits.setObjectID(id);
        hits.setAuthor(author);
        hits.setCreatedAt(createdAt);
        hits.setTitle(title);
        hits.setUrl(url);
        HitsRealmController.with(MainActivity.this).add(hits);
    }

    private void setRealmAdapter(RealmResults<Hits> hits) {

        RealmModelAdapter realmAdapter = new RealmModelAdapter(this.getApplicationContext(), hits, true);
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

    private static Bitmap drawableToBitmap (Drawable drawable) {

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
