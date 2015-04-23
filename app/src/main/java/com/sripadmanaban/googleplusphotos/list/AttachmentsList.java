package com.sripadmanaban.googleplusphotos.list;

import com.google.gson.annotations.SerializedName;

/**
 * Contains the images url and the plusone's url
 * Created by Sripadmanaban on 1/29/2015.
 */
public class AttachmentsList {

    @SerializedName("url")
    private String url;

    @SerializedName("fullImage")
    private FullImageList fullImage;

    public FullImageList getFullImage() {
        return fullImage;
    }

    public void setFullImage(FullImageList fullImage) {
        this.fullImage = fullImage;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
