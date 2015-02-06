package com.sripadmanaban.googleplusphotos;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.google.android.gms.plus.PlusOneButton;
import com.squareup.picasso.Picasso;
import com.sripadmanaban.googleplusphotos.list.ImageCenter;

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

    public static Fragment newInstance(int position) {
        Fragment fragment = new PlusOneImageFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(Constants.IMAGE_POSITION, position);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_plus_one_image, container, false);

        Bundle bundle = getArguments();

        imageCenter = ImageCenter.getImageCenter(getActivity().getApplicationContext());
        position = bundle.getInt(Constants.IMAGE_POSITION);

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
        String imageUrl = imageCenter.getImagePlusOneURLs().get(position).getFullImageUrl();
        plusOneUrl = imageCenter.getImagePlusOneURLs().get(position).getPlusOneUrl();
        Picasso.with(getActivity())
                .load(imageUrl)
                .resize(500, 500)
                .centerCrop()
                .into(imageView);
        mPlusOneButton.initialize(plusOneUrl, Constants.PLUS_ONE_REQUEST_CODE);
    }

    @Override
    public void onResume() {
        super.onResume();
        mPlusOneButton.initialize(plusOneUrl, Constants.PLUS_ONE_REQUEST_CODE);
    }
}
