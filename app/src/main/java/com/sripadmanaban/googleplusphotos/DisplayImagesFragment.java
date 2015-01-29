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

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.json.JsonValue;

/**
 * Display Fragment
 * Created by Sripadmanaban on 1/28/2015.
 */
public class DisplayImagesFragment extends Fragment {

    private String authorization;

    private View view;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_display_images, container, false);
        Bundle bundle = getArguments();

        authorization = "Bearer " + bundle.getString("ACCESS_TOKEN");

        AsyncSearchActivities searchActivities = new AsyncSearchActivities();
        searchActivities.execute(authorization, null);

        return view;
    }

    private class AsyncSearchActivities extends AsyncTask<String, Void, String> {

        private static final String searchUrl = "https://www.googleapis.com/plus/v1/activities?query=photographs&key={AIzaSyBmE7DEY4PeKC_KaG7SqwPZdM9BexGiK_o}";
        private HttpURLConnection connection;
        private String result;

        @Override
        protected String doInBackground(String... params) {

            try {

                URL url = new URL(searchUrl);
                connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
                connection.setRequestProperty("Authorization", params[0]);
                connection.setRequestProperty("X-JavaScript-User-Agent", "Google APIs Explorer");
                connection.setDoInput(true);
                connection.connect();

                String id = "";

                InputStream inputStream = connection.getInputStream();
                JsonReader jsonReader = Json.createReader(inputStream);

                JsonObject jsonObject = jsonReader.readObject();

                jsonReader.close();
                inputStream.close();

                JsonArray items = jsonObject.getJsonArray("items");
                for(JsonValue value : items)
                {
                    JsonObject object = (JsonObject) value;
                    JsonObject actor = object.getJsonObject("actor");
                    id = actor.getString("id");
                    break;
                }

                result = id;

            } catch (IOException e) {
                e.printStackTrace();

            } finally {

                if(connection != null)
                {
                    connection.disconnect();
                }

            }
            return result;
        }

        @Override
        protected void onPostExecute(String s) {

            Log.d("IDValue", s);
            TextView textView = (TextView) view.findViewById(R.id.list_view);
            textView.setText(s);
        }
    }
}
