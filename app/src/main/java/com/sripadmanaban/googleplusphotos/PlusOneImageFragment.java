package com.sripadmanaban.googleplusphotos;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.GestureDetectorCompat;
import android.util.Log;
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
 * The plus one fragments class
 * Created by Sripadmanaban on 2/2/2015.
 */
public class PlusOneImageFragment extends Fragment {

    private String plusOneUrl;
    private PlusOneButton mPlusOneButton;
    private ImageCenter imageCenter;
    private ImageView imageView;
    private int position;

    public static PlusOneImageFragment newInstance() {
        return new PlusOneImageFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_plus_one_image, container, false);

        imageCenter = ImageCenter.getImageCenter(getActivity().getApplicationContext());
        position = imageCenter.getPosition();

        final GestureDetectorCompat mDetector = new GestureDetectorCompat(getActivity(), new GestureDetector.SimpleOnGestureListener() {
            private static final String TAG = "Gesture";

            @Override
            public boolean onDown(MotionEvent e) {
                Log.d(TAG, "Down");
                return true;
            }

            @Override
            public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
                Log.d(TAG, "on fling");
                boolean result = false;
                try {
                    float diffX = e2.getX() - e1.getX();
                    if(Math.abs(diffX) > 100 && Math.abs(velocityX) > 100) {
                        if(diffX > 0) {
                            //Going back
                            if(position > 0) {
                                position--;
                            }
                            else {
                                position = imageCenter.getImageUrl().size() - 1;
                            }
                        }
                        else {
                            //Going forward
                            if(position < imageCenter.getImageUrl().size() - 1) {
                                position++;
                            }
                            else {
                                position = 0;
                            }
                        }
                        loadImage(position);
                    }
                    result = true;
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
                return result;
            }
        });

        view.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return mDetector.onTouchEvent(event);
            }

        });

        mPlusOneButton = (PlusOneButton) view.findViewById(R.id.plus_one_button);
        imageView = (ImageView) view.findViewById(R.id.imageView);

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        loadImage(position);
    }

    private void loadImage(int position) {
        List<String> keys = new ArrayList<>( imageCenter.getImageUrl().keySet());
        String imageUrl = keys.get(position);
        plusOneUrl = imageCenter.getImageUrl().get(imageUrl);
        Picasso.with(getActivity())
                .load(imageUrl)
                .fit()
                .into(imageView);
        mPlusOneButton.initialize(plusOneUrl, Constants.PLUS_ONE_REQUEST_CODE);
    }

    @Override
    public void onResume() {
        super.onResume();
        mPlusOneButton.initialize(plusOneUrl, Constants.PLUS_ONE_REQUEST_CODE);
    }
}
