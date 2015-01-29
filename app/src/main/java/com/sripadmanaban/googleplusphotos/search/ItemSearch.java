package com.sripadmanaban.googleplusphotos.search;

import com.google.gson.annotations.SerializedName;
import com.sripadmanaban.googleplusphotos.search.ActorSearch;

/**
 * Created by Sripadmanaban on 1/29/2015.
 */
public class ItemSearch {

    @SerializedName("actor")
    private ActorSearch actor;

    public ActorSearch getActor() {
        return actor;
    }

    public void setActor(ActorSearch actor) {
        this.actor = actor;
    }
}
