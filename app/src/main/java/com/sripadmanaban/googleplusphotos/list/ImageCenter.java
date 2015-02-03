package com.sripadmanaban.googleplusphotos.list;

import android.content.Context;

import java.util.HashMap;

/**
 * Created by Sripadmanaban on 2/3/2015.
 */
public class ImageCenter {

    private HashMap<String, String> imageUrl;
    private static ImageCenter imageCenter;
    private Context context;

    public static ImageCenter getImageCenter(Context context) {
        if(imageCenter == null) {
            imageCenter = new ImageCenter(context);
        }
        return imageCenter;
    }

    private ImageCenter(Context context) {
        imageUrl = new HashMap<>();
        this.context = context;
    }

    public HashMap<String, String> getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(HashMap<String, String> imageUrl) {
        this.imageUrl = imageUrl;
    }
}
