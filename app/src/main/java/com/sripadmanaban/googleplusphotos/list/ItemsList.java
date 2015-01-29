package com.sripadmanaban.googleplusphotos.list;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Sripadmanaban on 1/29/2015.
 */
public class ItemsList {

    @SerializedName("object")
    private ObjectList object;

    public ObjectList getObject() {
        return object;
    }

    public void setObject(ObjectList object) {
        this.object = object;
    }
}
