package com.sripadmanaban.googleplusphotos.list;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Sripadmanaban on 1/29/2015.
 */
public class AttachmentsList {

    @SerializedName("fullImage")
    private FullImageList fullImage;

    public FullImageList getFullImage() {
        return fullImage;
    }

    public void setFullImage(FullImageList fullImage) {
        this.fullImage = fullImage;
    }
}
