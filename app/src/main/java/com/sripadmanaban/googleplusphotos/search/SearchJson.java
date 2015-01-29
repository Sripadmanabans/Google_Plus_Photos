package com.sripadmanaban.googleplusphotos.search;

import com.google.gson.annotations.SerializedName;
import com.sripadmanaban.googleplusphotos.search.ItemSearch;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Sripadmanaban on 1/29/2015.
 */
public class SearchJson {

    @SerializedName("items")
    private List<ItemSearch> items = new ArrayList<>();

    public List<ItemSearch> getItems() {
        return items;
    }

    public void setItems(List<ItemSearch> items) {
        this.items = items;
    }
}
