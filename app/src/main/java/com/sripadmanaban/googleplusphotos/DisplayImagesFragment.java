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
import android.widget.AbsListView;
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
import com.sripadmanaban.googleplusphotos.list.ImagePlusOneURL;
import com.sripadmanaban.googleplusphotos.list.ItemsList;
import com.sripadmanaban.googleplusphotos.list.ListJson;

import java.io.IOException;
import java.util.ArrayList;
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

    private ImageAdapter imageAdapter;

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

        setRetainInstance(true);

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
        imageAdapter = new ImageAdapter(getActivity(), imageCenter.getImagePlusOneURLs());
        Log.d("count", imageAdapter.getCount() + "");
        gridView.setAdapter(imageAdapter);

        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getData(Constants.SWIPE_DOWN_REFRESH, null);
                swipeRefreshLayout.setRefreshing(true);
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        /**
         * Execute the background task, which uses {@link android.os.AsyncTask} to load the data.
         */
        getData(Constants.ON_RESUME, null);
    }

    private void getData(String type, String pageToken) {
        AsyncSearchActivities searchActivities = new AsyncSearchActivities();
        searchActivities.execute(authorization, type, pageToken);
    }


    private class AsyncSearchActivities extends AsyncTask<String, Void, List<ImagePlusOneURL>> {

        private String searchUrl = "https://www.googleapis.com/plus/v1/activities?query=photographs&maxResults=20&key=AIzaSyBmE7DEY4PeKC_KaG7SqwPZdM9BexGiK_o";

        private List<ImagePlusOneURL> mImagePlusOneURLs = new ArrayList<>();

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected List<ImagePlusOneURL> doInBackground(String... params) {

            try {

                if(params[2] != null) {
                    searchUrl = "https://www.googleapis.com/plus/v1/activities?query=photographs&maxResults=20&pageToken=" + params[2] + "&key=AIzaSyBmE7DEY4PeKC_KaG7SqwPZdM9BexGiK_o";
                }

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
                imageCenter.setToken(listJson.getNextPageToken());
                List<ItemsList> items = listJson.getItems();
                List<AttachmentsList> attachments = new ArrayList<>();
                for(ItemsList item : items) {
                    if(item.getObject().getAttachments() != null) {
                        attachments.addAll(item.getObject().getAttachments());
                    }
                }

                for(AttachmentsList attachment : attachments) {
                    FullImageList fullImage = attachment.getFullImage();
                    String plusOneUrl = attachment.getUrl();
                    if(fullImage != null) {
                        if (fullImage.getUrl() != null) {
                            if(!imageCenter.getCheckUrlMap().containsKey(fullImage.getUrl())) {
                                imageCenter.getCheckUrlMap().put(fullImage.getUrl(), 0);
                                ImagePlusOneURL object = new ImagePlusOneURL();
                                object.setFullImageUrl(fullImage.getUrl());
                                object.setPlusOneUrl(plusOneUrl);
                                switch (params[1]) {
                                    case Constants.ON_RESUME:
                                        imageCenter.getImagePlusOneURLs().add(object);
                                        break;
                                    case Constants.SWIPE_DOWN_REFRESH :
                                        mImagePlusOneURLs.add(object);
                                        break;
                                    case Constants.SCROLL_REFRESH :
                                        mImagePlusOneURLs.add(object);
                                        break;
                                }
                            }
                        }
                    }
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
            finally {
                imageCenter.updateImagePlusOneURLs(params[1], mImagePlusOneURLs);
            }
            return imageCenter.getImagePlusOneURLs();
        }

        @Override
        protected void onPostExecute(List<ImagePlusOneURL> list) {
            onRefreshComplete(list);
        }
    }


    private void onRefreshComplete(List<ImagePlusOneURL> list) {

        // Remove all items from the HashMap, and then replace them with the new items
        Log.d("count map", list.size() + "");
        imageAdapter.notifyDataSetChanged();

        gridView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                Log.d("ScrollState", scrollState + "");
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                Log.d("FirstItem", firstVisibleItem + "");
                Log.d("VisibleCount", visibleItemCount + "");
                Log.d("TotalItem", totalItemCount + "");

                if(totalItemCount - firstVisibleItem < 7) {
                    getData(Constants.SCROLL_REFRESH, imageCenter.getToken());
                }
            }
        });

        // Stop the refreshing indicator
        swipeRefreshLayout.setRefreshing(false);
    }



    private void sendToCallingActivity(int position) {

        if(mCallBack != null) {
            mCallBack.dataForFullImageFragment(position);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    public interface DisplayImagesFragmentCallBack {
        public void dataForFullImageFragment(int position);
    }
}