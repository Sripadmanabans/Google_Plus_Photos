package com.sripadmanaban.googleplusphotos;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;

/**
 * Display Activity
 * Created by Sripadmanaban on 1/28/2015.
 */
public class DisplayImagesActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_images);

        Intent intent = getIntent();
        Bundle bundle = new Bundle();
        bundle.putString("ACCESS_TOKEN", intent.getStringExtra("ACCESS_TOKEN"));

        DisplayImagesFragment fragment = new DisplayImagesFragment();
        fragment.setArguments(bundle);
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.add(R.id.fragment_container, fragment, DisplayImagesFragment.class.getName());
        transaction.commit();
    }
}
