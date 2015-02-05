package com.sripadmanaban.googleplusphotos.list;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * This is the whole json object received from Google
 * Created by Sripadmanaban on 1/29/2015.
 */
public class ListJson {

    @SerializedName("items")
    private List<ItemsList> items = new ArrayList<>();

    public List<ItemsList> getItems() {
        return items;
    }

    public void setItems(List<ItemsList> items) {
        this.items = items;
    }
}
