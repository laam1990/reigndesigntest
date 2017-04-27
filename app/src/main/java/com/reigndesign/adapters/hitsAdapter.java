package com.reigndesign.adapters;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.reigndesign.R;
import com.reigndesign.WebViewActivity;
import com.reigndesign.model.modelHits;
import com.reigndesign.realm.hitsRealm;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import io.realm.Realm;

/**
 * Created by Luis Adrian on 24/04/2017.
 */

public class hitsAdapter extends realmRecyclerViewAdapter<modelHits> {

    private Context mContext;
    private List<modelHits> mListHits;
    private Bundle mBundle;
    private String url;
    private Intent mIntent;
    private modelHits hits;

    private Realm realm;
    private Bundle bundle;

    public hitsAdapter(Context mContext) {
        this.mContext = mContext;

    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_hits, parent, false);

        return new MyViewHolder(itemView);

    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, final int position) {

        realm = hitsRealm.getInstance().getRealm();

        hits = getItem(position);
        final MyViewHolder holder = (MyViewHolder) viewHolder;
        //final modelHits hits = mListHits.get(position);

        holder.title.setText(hits.getTitle());
        holder.author.setText(hits.getAuthor());

        String date = DateAgo(hits.getCreatedAt());
        holder.time.setText(date);

        holder.row.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                url = hits.getUrl();
                mBundle = new Bundle();
                mBundle.putString("url",url);
                mIntent = new Intent(mContext, WebViewActivity.class);
                mIntent.putExtras(mBundle);
                mContext.startActivity(mIntent);

            }
        });

    }

    @Override
    public int getItemCount() {
        if (getRealmAdapter() != null) {
            return getRealmAdapter().getCount();
        }
        return 0;
    }

    public void removeItem(int position) {
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, getRealmAdapter().getCount());
    }

    public String DateAgo(String createdAt)
    {
        long time = 0;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        sdf.setTimeZone(TimeZone.getTimeZone("GMT"));
        try {
            time = sdf.parse(createdAt).getTime();
        }catch (ParseException e){
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        long now = System.currentTimeMillis();

        CharSequence ago =
                DateUtils.getRelativeTimeSpanString(time, now, DateUtils.MINUTE_IN_MILLIS);

        return ago.toString();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView title, author, time;
        LinearLayout row;

        public MyViewHolder(View view) {
            super(view);
            title = (TextView) view.findViewById(R.id.title);
            author = (TextView) view.findViewById(R.id.author);
            time = (TextView) view.findViewById(R.id.time);
            row = (LinearLayout) view.findViewById(R.id.linearRow);

        }
    }
}
