package com.sripadmanaban.googleplusphotos;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.google.android.gms.plus.PlusOneButton;
import com.squareup.picasso.Picasso;

/**
 * Created by Sripadmanaban on 2/2/2015.
 */
public class PlusOneImageFragment extends Fragment {

    private String plusOneUrl;
    private PlusOneButton mPlusOneButton;

    public static PlusOneImageFragment newInstance(String fullImageUrl, String plusOneUrl) {
        PlusOneImageFragment plusOneImageFragment = new PlusOneImageFragment();
        Bundle bundle = new Bundle();
        bundle.putString(Constants.FULL_IMAGE_URL, fullImageUrl);
        bundle.putString(Constants.PLUS_ONE_URL, plusOneUrl);
        plusOneImageFragment.setArguments(bundle);
        return plusOneImageFragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_plus_one_image, container, false);

        Bundle bundle = getArguments();
        plusOneUrl = bundle.getString(Constants.PLUS_ONE_URL);

        mPlusOneButton = (PlusOneButton) view.findViewById(R.id.plus_one_button);
        ImageView imageView = (ImageView) view.findViewById(R.id.imageView);

        Picasso.with(getActivity())
                .load(bundle.getString(Constants.FULL_IMAGE_URL))
                .fit()
                .into(imageView);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        mPlusOneButton.initialize(plusOneUrl, Constants.PLUS_ONE_REQUEST_CODE);
    }
}
