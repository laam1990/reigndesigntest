package com.reigndesign.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.reigndesign.R;
import com.reigndesign.model.modelHits;

import java.util.List;

/**
 * Created by Luis Adrian on 24/04/2017.
 */

public class hitsAdapter extends RecyclerView.Adapter<hitsAdapter.MyViewHolder> {

    private Context mContext;
    private List<modelHits> mListHits;

    public hitsAdapter(Context mContext, List<modelHits> mListHits) {
        this.mContext = mContext;
        this.mListHits = mListHits;
    }

    @Override
    public hitsAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_hits, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(hitsAdapter.MyViewHolder holder, int position) {

        modelHits hits = mListHits.get(position);

        if(position % 2 != 0 )
        {
            holder.row.setBackgroundColor(mContext.getResources().getColor(R.color.gray) );
        }

        if(hits.getTitle()!= null)
        {
            holder.title.setText(hits.getTitle());
        } else{
            holder.title.setText(hits.getStoryTitle());
        }

        holder.author.setText(hits.getAuthor());
        holder.time.setText(hits.getCreatedAt());

    }

    @Override
    public int getItemCount() {
        return mListHits.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        public TextView title, author, time;
        public LinearLayout row;

        public MyViewHolder(View view) {
            super(view);
            title = (TextView) view.findViewById(R.id.title);
            author = (TextView) view.findViewById(R.id.author);
            time = (TextView) view.findViewById(R.id.time);
            row = (LinearLayout) view.findViewById(R.id.linearRow);

        }
    }
}