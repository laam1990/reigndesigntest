package com.reigndesign.retrofit.responseGeneral;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Luis Adrian on 24/04/2017.
 */

public class responseGeneral {
    @SerializedName("nbHits")
    @Expose
    int nbHits;

    @SerializedName("query")
    @Expose
    String query;

    @SerializedName("hits")
    @Expose
    List<Object> hits;

    public int getNbHits() {
        return nbHits;
    }

    public void setNbHits(int nbHits) {
        this.nbHits = nbHits;
    }

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public List<Object> getObject() {
        return hits;
    }

    public void setObject(List<Object> hits) {
        this.hits = hits;
    }

    public responseGeneral() {
        this.hits = hits;
        this.nbHits = nbHits;
        this.query = query;

    }
}
