package com.sripadmanaban.googleplusphotos;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;

import java.util.HashMap;

/**
 * Display Activity
 * Created by Sripadmanaban on 1/28/2015.
 */
public class DisplayImagesActivity extends Activity implements DisplayImagesFragment.DisplayImagesFragmentCallBack {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_images);

        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, DisplayImagesFragment.newInstance(this), DisplayImagesFragment.class.getName());
        transaction.commit();
    }

    @Override
    public void dataForFullImageFragment(int position) {
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, PlusOneImageFragment.newInstance(position), PlusOneImageFragment.class.getName());
        transaction.addToBackStack(PlusOneImageFragment.class.getName());
        transaction.commit();
    }
}
