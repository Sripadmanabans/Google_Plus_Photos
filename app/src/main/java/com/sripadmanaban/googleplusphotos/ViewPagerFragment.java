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
 * Created by Sripadmanaban on 2/6/2015.
 */
public class ViewPagerFragment extends Fragment {

    public static Fragment getInstance() {
        return new ViewPagerFragment();
    }

    private ImageCenter imageCenter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_view_pager, container, false);

        imageCenter = ImageCenter.getImageCenter(getActivity().getApplicationContext());
        ViewPager viewPager = (ViewPager) view.findViewById(R.id.viewPager);
        viewPager.setAdapter(new ScreenSlideAdapter(getFragmentManager()));

        return view;
    }

    private class ScreenSlideAdapter extends FragmentStatePagerAdapter {
        public ScreenSlideAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public int getCount() {
            return imageCenter.getCount();
        }

        @Override
        public Fragment getItem(int position) {
            return PlusOneImageFragment.newInstance(position);
        }
    }
}
