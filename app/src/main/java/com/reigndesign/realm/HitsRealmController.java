package com.reigndesign.realm;

import android.app.Activity;
import android.app.Application;

import com.reigndesign.model.Hits;

import io.realm.Realm;
import io.realm.RealmResults;

/**
 * Created by Luis Adrian on 26/04/2017.
 */

public class HitsRealmController {

    private static HitsRealmController instance;
    private final Realm realm;

    public HitsRealmController(Application application) {
        realm = Realm.getDefaultInstance();
    }

    public static HitsRealmController with(Activity activity) {

        if (instance == null) {
            instance = new HitsRealmController(activity.getApplication());
        }
        return instance;
    }

    public static HitsRealmController getInstance() {

        return instance;
    }

    public Realm getRealm() {

        return realm;
    }

    public void refresh() {

        realm.refresh();
    }

    public void add(Hits hits){

        realm.beginTransaction();
        realm.copyToRealm(hits);
        realm.commitTransaction();
    }

    public void delete(int id){

        realm.beginTransaction();
        Hits hits = findById(id);
        hits.removeFromRealm();
        realm.commitTransaction();

    }

    public RealmResults<Hits> findAll() {

        return realm.where(Hits.class).findAllSorted("createdAt",false);

    }

    public Hits findById(int id) {

        return realm.where(Hits.class).equalTo("objectID", id).findFirst();
    }

    public boolean isEmpty() {

        return !realm.allObjects(Hits.class).isEmpty();
    }

}
