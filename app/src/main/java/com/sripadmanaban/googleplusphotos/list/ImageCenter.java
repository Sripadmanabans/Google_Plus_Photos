package com.sripadmanaban.googleplusphotos.list;

import android.content.Context;

import com.sripadmanaban.googleplusphotos.Constants;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;


/**
 * Center to store all image urls
 * Created by Sripadmanaban on 2/3/2015.
 */
public class ImageCenter {

    private List<ImagePlusOneURL> imagePlusOneURLs;
    private HashMap<String, Integer> checkUrlMap;
    private static ImageCenter imageCenter;
    private Context context;

    public static ImageCenter getImageCenter(Context context) {
        if(imageCenter == null) {
            imageCenter = new ImageCenter(context);
        }
        return imageCenter;
    }

    private ImageCenter(Context context) {
        imagePlusOneURLs = new ArrayList<>();
        checkUrlMap = new HashMap<>();
        this.context = context;
    }

    public HashMap<String, Integer> getCheckUrlMap() {
        return checkUrlMap;
    }

    public void setCheckUrlMap(HashMap<String, Integer> checkUrlMap) {
        this.checkUrlMap = checkUrlMap;
    }

    public List<ImagePlusOneURL> getImagePlusOneURLs() {
        return imagePlusOneURLs;
    }

    public void setImagePlusOneURLs(List<ImagePlusOneURL> imagePlusOneURLs) {
        this.imagePlusOneURLs = imagePlusOneURLs;
    }

    public void updateImagePlusOneURLs(String type, List<ImagePlusOneURL> list) {
        if(type.equals(Constants.SWIPE_DOWN_REFRESH)) {
            imagePlusOneURLs.addAll(0, list);
        }
    }
}