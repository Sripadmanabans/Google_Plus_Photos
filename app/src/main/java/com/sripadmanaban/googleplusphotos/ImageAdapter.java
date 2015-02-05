package com.sripadmanaban.googleplusphotos;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Adapter for the grid view
 * Created by Sripadmanaban on 2/2/2015.
 */
public class ImageAdapter extends BaseAdapter {

    private Context context;
    List<String> imageUrl;

    public ImageAdapter(Context context, HashMap<String, String> map) {
        Log.d("map", map.toString());
        this.context = context;
        imageUrl = new ArrayList<>(map.keySet());
    }

    @Override
    public int getCount() {
        return imageUrl.size();
    }

    @Override
    public Object getItem(int position) {
        return imageUrl.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView imageView;
        if(convertView == null) {
            imageView = new ImageView(context);
        }
        else {
            imageView = (ImageView) convertView;
        }

     Picasso.with(context).setIndicatorsEnabled(true);
  Picasso.with(context).load(imageUrl.get(position))
        .resize(350, 350)
        .centerCrop()
        .into(imageView);

        return imageView;
        }
        }
