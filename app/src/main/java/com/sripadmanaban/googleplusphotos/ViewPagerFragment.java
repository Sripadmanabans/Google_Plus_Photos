package com.sripadmanaban.googleplusphotos;

import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v13.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sripadmanaban.googleplusphotos.list.ImageCenter;

/**
 * View Pager is located here
 * Created by Sripadmanaban on 2/6/2015.
 */
public class ViewPagerFragment extends Fragment {

    private ImageCenter imageCenter;

    private ScreenSlideAdapter adapter;

    public static Fragment getInstance(int position) {
        Fragment fragment = new ViewPagerFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(Constants.IMAGE_POSITION, position);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_view_pager, container, false);

        adapter = new ScreenSlideAdapter(getFragmentManager());
        Bundle bundle = getArguments();
        int currentPosition = bundle.getInt(Constants.IMAGE_POSITION, 0);

        imageCenter = ImageCenter.getImageCenter(getActivity().getApplicationContext());
        ViewPager viewPager = (ViewPager) view.findViewById(R.id.viewPager);
        viewPager.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        viewPager.setCurrentItem(currentPosition);
        viewPager.setPageTransformer(true , new ZoomOutPageTransformer());

        return view;
    }

    private class ScreenSlideAdapter extends FragmentStatePagerAdapter {
        public ScreenSlideAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public int getCount() {
            return imageCenter.getImagePlusOneURLs().size();
        }

        @Override
        public Fragment getItem(int position) {
            return PlusOneImageFragment.newInstance(position);
        }
    }
}
