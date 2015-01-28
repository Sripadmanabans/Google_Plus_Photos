package com.sripadmanaban.googleplusphotos;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Display Fragment
 * Created by Sripadmanaban on 1/28/2015.
 */
public class DisplayImagesFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_display_images, container, false);
        Bundle bundle = getArguments();
        TextView textView = (TextView) view.findViewById(R.id.list_view);
        textView.setText(bundle.getString("ACCESS_TOKEN"));
        return view;
    }
}
