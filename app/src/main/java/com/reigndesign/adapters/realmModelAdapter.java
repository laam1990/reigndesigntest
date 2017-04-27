package com.reigndesign.adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import io.realm.RealmBaseAdapter;
import io.realm.RealmObject;
import io.realm.RealmResults;

/**
 * Created by Luis Adrian on 26/04/2017.
 */

public class realmModelAdapter<T extends RealmObject> extends RealmBaseAdapter<T> {

    public realmModelAdapter(Context context, RealmResults<T> realmResults, boolean automaticUpdate) {

        super(context, realmResults, automaticUpdate);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        return null;
    }
}