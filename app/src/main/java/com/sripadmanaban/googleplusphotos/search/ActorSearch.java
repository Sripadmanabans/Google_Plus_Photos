package com.sripadmanaban.googleplusphotos.search;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Sripadmanaban on 1/29/2015.
 */
public class ActorSearch {

    @SerializedName("id")
    private String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
