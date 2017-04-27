package com.reigndesign.realm;

import android.app.Activity;
import android.app.Application;
import android.support.v7.util.SortedList;
import android.support.v7.widget.util.SortedListAdapterCallback;

import com.reigndesign.model.modelHits;
import com.reigndesign.retrofit.model.Hits;

import io.realm.Realm;
import io.realm.RealmResults;

import static io.realm.RealmResults.SORT_ORDER_ASCENDING;
import static io.realm.RealmResults.SORT_ORDER_DESCENDING;

/**
 * Created by Luis Adrian on 26/04/2017.
 */

public class hitsRealm {

    private static hitsRealm instance;
    private final Realm realm;

    public hitsRealm(Application application) {
        realm = Realm.getDefaultInstance();
    }

    public static hitsRealm with(Activity activity) {

        if (instance == null) {
            instance = new hitsRealm(activity.getApplication());
        }
        return instance;
    }

    public static hitsRealm getInstance() {

        return instance;
    }

    public Realm getRealm() {

        return realm;
    }

    public void refresh() {

        realm.refresh();
    }

    public void add(modelHits hits){

        realm.beginTransaction();
        realm.copyToRealm(hits);
        realm.commitTransaction();
    }

    public void delete(int id){

        realm.beginTransaction();
        modelHits hits = findById(id);
        hits.removeFromRealm();
        realm.commitTransaction();

    }

    public RealmResults<modelHits> findAll() {

        return realm.where(modelHits.class).findAllSorted("createdAt",false);

    }

    public modelHits findById(int id) {

        return realm.where(modelHits.class).equalTo("objectID", id).findFirst();
    }

    public boolean isEmpty() {

        return !realm.allObjects(modelHits.class).isEmpty();
    }

}
