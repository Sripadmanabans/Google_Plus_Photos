package com.sripadmanaban.googleplusphotos.list;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Sripadmanaban on 1/29/2015.
 */
public class FullImageList {

    @SerializedName("url")
    private String url;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
