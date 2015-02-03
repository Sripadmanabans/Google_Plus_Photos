package com.sripadmanaban.googleplusphotos;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.GestureDetectorCompat;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.google.android.gms.plus.PlusOneButton;
import com.squareup.picasso.Picasso;
import com.sripadmanaban.googleplusphotos.list.ImageCenter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Sripadmanaban on 2/2/2015.
 */
public class PlusOneImageFragment extends Fragment implements
        GestureDetector.OnGestureListener, GestureDetector.OnDoubleTapListener {

    private String plusOneUrl;
    private PlusOneButton mPlusOneButton;
    private ImageCenter imageCenter;

    private GestureDetectorCompat mDetector;

    public static PlusOneImageFragment newInstance(int position) {
        PlusOneImageFragment plusOneImageFragment = new PlusOneImageFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(Constants.IMAGE_POSITION,position);
        plusOneImageFragment.setArguments(bundle);
        return plusOneImageFragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_plus_one_image, container, false);

        imageCenter = ImageCenter.getImageCenter(getActivity().getApplicationContext());

        Bundle bundle = getArguments();

        int position = bundle.getInt(Constants.IMAGE_POSITION);
        List<String> keys =new ArrayList<String>( imageCenter.getImageUrl().keySet());

        String key = keys.get(position);



        plusOneUrl = imageCenter.getImageUrl().get(key);

        mPlusOneButton = (PlusOneButton) view.findViewById(R.id.plus_one_button);
        ImageView imageView = (ImageView) view.findViewById(R.id.imageView);

        Picasso.with(getActivity())
                .load(key)
                .fit()
                .into(imageView);

        return view;
    }


    @Override
    public void onResume() {
        super.onResume();
        mPlusOneButton.initialize(plusOneUrl, Constants.PLUS_ONE_REQUEST_CODE);
    }



    @Override
    public boolean onSingleTapConfirmed(MotionEvent e) {
        return false;
    }

    @Override
    public boolean onDoubleTap(MotionEvent e) {
        return false;
    }

    @Override
    public boolean onDoubleTapEvent(MotionEvent e) {
        return false;
    }

    @Override
    public boolean onDown(MotionEvent e) {
        return false;
    }

    @Override
    public void onShowPress(MotionEvent e) {

    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        return false;
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        return false;
    }

    @Override
    public void onLongPress(MotionEvent e) {

    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        return false;
    }
}
