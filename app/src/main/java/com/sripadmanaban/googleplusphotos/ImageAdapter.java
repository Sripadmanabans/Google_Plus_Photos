package com.sripadmanaban.googleplusphotos;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;
import com.sripadmanaban.googleplusphotos.list.ImagePlusOneURL;

import java.util.List;

/**
 * Adapter for the grid view
 * Created by Sripadmanaban on 2/2/2015.
 */
public class ImageAdapter extends BaseAdapter {

    private Context context;
    List<ImagePlusOneURL> imageUrl;

    public ImageAdapter(Context context, List<ImagePlusOneURL> list) {
        this.context = context;
        imageUrl = list;
    }

    @Override
    public int getCount() {
        return imageUrl.size();
    }

    @Override
    public Object getItem(int position) {
        return imageUrl.get(position).getFullImageUrl();
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
    Picasso.with(context).load(imageUrl.get(position).getFullImageUrl())
        .placeholder(R.drawable.placeholder)
        .resize(550, 600)
        .centerCrop()
        .into(imageView);

        return imageView;
    }
}
