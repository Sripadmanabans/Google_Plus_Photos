package com.sripadmanaban.googleplusphotos.list;

import android.content.Context;

import java.util.LinkedHashMap;


/**
 * Center to store all image urls
 * Created by Sripadmanaban on 2/3/2015.
 */
public class ImageCenter {

    private LinkedHashMap<String, String> imageUrl;
    private static ImageCenter imageCenter;
    private int position;
    private Context context;

    public static ImageCenter getImageCenter(Context context) {
        if(imageCenter == null) {
            imageCenter = new ImageCenter(context);
        }
        return imageCenter;
    }

    private ImageCenter(Context context) {
        imageUrl = new LinkedHashMap<>();
        this.context = context;
    }

    public LinkedHashMap<String, String> getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(LinkedHashMap<String, String> imageUrl) {

        for(String urlKeys: imageUrl.keySet()){
           if(!this.imageUrl.containsKey(urlKeys)){
               this.imageUrl.put(urlKeys, imageUrl.get(urlKeys));
           }
       }
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public int getCount() {
        return imageUrl.size();
    }
}