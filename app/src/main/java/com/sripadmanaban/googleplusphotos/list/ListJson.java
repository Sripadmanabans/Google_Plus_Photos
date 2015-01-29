package com.sripadmanaban.googleplusphotos.list;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
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
