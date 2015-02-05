package com.sripadmanaban.googleplusphotos.list;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Object inside the items json object
 * Created by Sripadmanaban on 1/29/2015.
 */
public class ObjectList {

    @SerializedName("attachments")
    private List<AttachmentsList> attachments = new ArrayList<>();

    public List<AttachmentsList> getAttachments() {
        return attachments;
    }

    public void setAttachments(List<AttachmentsList> attachments) {
        this.attachments = attachments;
    }
}
