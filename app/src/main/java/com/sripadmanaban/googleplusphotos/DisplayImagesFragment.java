package com.sripadmanaban.googleplusphotos;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.sripadmanaban.googleplusphotos.list.AttachmentsList;
import com.sripadmanaban.googleplusphotos.list.FullImageList;
import com.sripadmanaban.googleplusphotos.list.ImageCenter;
import com.sripadmanaban.googleplusphotos.list.ItemsList;
import com.sripadmanaban.googleplusphotos.list.ListJson;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * Display Fragment
 * Created by Sripadmanaban on 1/28/2015.
 */
public class DisplayImagesFragment extends Fragment {

    private String authorization;

    private GridView gridView;

    private SwipeRefreshLayout swipeRefreshLayout;

    DisplayImagesFragmentCallBack mCallBack;

    private ImageCenter imageCenter;

    public static DisplayImagesFragment newInstance(Context context) {
        DisplayImagesFragment displayImagesFragment = new DisplayImagesFragment();
        Bundle bundle = new Bundle();
        SharedPreferences preferences = context.getSharedPreferences(Constants.PREFERENCES, Context.MODE_PRIVATE);
        bundle.putString(Constants.ACCESS_TOKEN, preferences.getString(Constants.ACCESS_TOKEN, null));
        displayImagesFragment.setArguments(bundle);
        return displayImagesFragment;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mCallBack = (DisplayImagesFragmentCallBack) activity;
        }
        catch (ClassCastException e) {
            throw new ClassCastException("The activity should implement DisplayImagesFragmentCallBack");
        }

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_display_images, container, false);

        imageCenter = ImageCenter.getImageCenter(getActivity().getApplicationContext());

        Bundle bundle = getArguments();
        authorization = "Bearer " + bundle.getString(Constants.ACCESS_TOKEN);

        Log.d("auth", authorization);

        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.activity_main_swipe_refresh_layout);
        swipeRefreshLayout.setColorSchemeResources(R.color.orange, R.color.green, R.color.blue, R.color.red);

        gridView = (GridView) view.findViewById(R.id.gridView);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                sendToCallingActivity(position);
            }
        });

        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                AsyncSearchActivities searchActivities = new AsyncSearchActivities();
                searchActivities.execute(authorization);
                swipeRefreshLayout.setRefreshing(true);
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        initiateRefresh();
    }

    private void initiateRefresh() {
        /**
         * Execute the background task, which uses {@link android.os.AsyncTask} to load the data.
         */
        AsyncSearchActivities searchActivities = new AsyncSearchActivities();
        searchActivities.execute(authorization);
    }



    private class AsyncSearchActivities extends AsyncTask<String, Void, LinkedHashMap<String, String>> {

        private String searchUrl = "https://www.googleapis.com/plus/v1/activities?query=photographs&maxResults=20&key=AIzaSyBmE7DEY4PeKC_KaG7SqwPZdM9BexGiK_o";

        private LinkedHashMap<String, String> imageUrl = new LinkedHashMap<>();

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected LinkedHashMap<String, String> doInBackground(String... params) {

            try {

                OkHttpClient client = new OkHttpClient();

                Request request = new Request.Builder()
                        .url(searchUrl)
                        .addHeader("Authorization", params[0])
                        .addHeader("X-JavaScript-User-Agent", "Google APIs Explorer")
                        .build();

                Response response = client.newCall(request).execute();

                String input = response.body().string();

                Gson gson = new GsonBuilder().create();

                ListJson listJson = gson.fromJson(input, ListJson.class);
                List<ItemsList> items = listJson.getItems();
                List<AttachmentsList> attachments = new ArrayList<>();
                for(ItemsList item : items) {
                    if(item.getObject().getAttachments() != null) {
                        attachments.addAll(item.getObject().getAttachments());
                    }
                }

                for(AttachmentsList attachment : attachments) {
                    FullImageList fullImage = attachment.getFullImage();
                    String url = attachment.getUrl();
                    if(fullImage != null) {
                        if (fullImage.getUrl() != null) {
                            if(!imageUrl.containsKey(fullImage.getUrl())) {
                                imageUrl.put(fullImage.getUrl(), url);
                            }
                        }
                    }
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
            return imageUrl;
        }

        @Override
        protected void onPostExecute(LinkedHashMap<String, String> map) {
            onRefreshComplete(map);
        }
    }


    private void onRefreshComplete(LinkedHashMap<String,String> map) {

        // Remove all items from the HashMap, and then replace them with the new items
        Log.d("count map", map.size() + "");
        imageCenter.setImageUrl(map);

        ImageAdapter imageAdapter = new ImageAdapter(getActivity(), imageCenter.getImageUrl());
        Log.d("count", imageAdapter.getCount() + "");
        gridView.setAdapter(imageAdapter);

        // Stop the refreshing indicator
        swipeRefreshLayout.setRefreshing(false);
    }



    private void sendToCallingActivity(int position) {

        if(mCallBack != null) {
            imageCenter.setPosition(position);
            mCallBack.dataForFullImageFragment();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    public interface DisplayImagesFragmentCallBack {
        public void dataForFullImageFragment();
    }
}