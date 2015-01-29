package com.sripadmanaban.googleplusphotos;

import android.app.Fragment;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.sripadmanaban.googleplusphotos.list.ListJson;
import com.sripadmanaban.googleplusphotos.search.SearchJson;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;



/**
 * Display Fragment
 * Created by Sripadmanaban on 1/28/2015.
 */
public class DisplayImagesFragment extends Fragment {

    private String authorization;

    private ListJson listJson;

    private View view;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_display_images, container, false);
        Bundle bundle = getArguments();

        authorization = "Bearer " + bundle.getString("ACCESS_TOKEN");

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        AsyncSearchActivities searchActivities = new AsyncSearchActivities();
        searchActivities.execute(authorization);
    }

    private class AsyncSearchActivities extends AsyncTask<String, Void, List<String>> {

        private String searchUrl = "https://www.googleapis.com/plus/v1/activities?query=photographs&key=AIzaSyBmE7DEY4PeKC_KaG7SqwPZdM9BexGiK_o";
        private List<String> imageUrl = new ArrayList<>();
        @Override
        protected List<String> doInBackground(String... params) {

            try {

                OkHttpClient client = new OkHttpClient();

                Request request = new Request.Builder()
                        .url(searchUrl)
                        .addHeader("Authorization", params[0])
                        .addHeader("X-JavaScript-User-Agent", "Google APIs Explorer")
                        .build();

                Response response = client.newCall(request).execute();

                String inputSearch = response.body().string();

                Gson gson = new GsonBuilder().create();

                SearchJson searchJson = gson.fromJson(inputSearch, SearchJson.class);

                Log.d("data", searchJson.getItems().get(0).getActor().getId());
                String id = searchJson.getItems().get(0).getActor().getId();

                String listUrl = "https://www.googleapis.com/plus/v1/people/" + id + "/activities/public?key=AIzaSyBmE7DEY4PeKC_KaG7SqwPZdM9BexGiK_o";

                OkHttpClient clientList = new OkHttpClient();

                Request requestList = new Request.Builder()
                        .url(listUrl)
                        .addHeader("Authorization", params[0])
                        .addHeader("X-JavaScript-User-Agent", "Google APIs Explorer")
                        .build();

                Response responseList = clientList.newCall(requestList).execute();

                String inputList = responseList.body().string();

                listJson = gson.fromJson(inputList, ListJson.class);
                Log.d("data", listJson.getItems().get(0).getObject().getAttachments().get(0).getFullImage().getUrl());

            } catch (IOException e) {
                e.printStackTrace();
            }
            return imageUrl;
        }

        @Override
        protected void onPostExecute(List<String> strings) {
            TextView textView = (TextView) view.findViewById(R.id.list_view);
            textView.setText(listJson.getItems().get(0).getObject().getAttachments().get(0).getFullImage().getUrl());

        }
    }

    @Override
    public void onPause() {
        super.onPause();

    }
}
