package com.example.assignment5_newsgateway;

import android.net.Uri;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;

public class NewsSourceDownloader implements Runnable {
    private MainActivity mainActivity ;
	private String source;

    private final String URL_ALL = "https://newsapi.org/v2/sources?language=en&country=us&apiKey=81fb0e8cfb714aa4ac35fb56c359b216";
    private final String URL_CATEGORY_HEAD = "https://newsapi.org/v2/sources?language=en&country=us&category=";
    private final String URL_CATEGORY_TAIL = "&apiKey=";
    private final String KEY = "81fb0e8cfb714aa4ac35fb56c359b216";
    private static final String TAG = "NewsSourceDownloader";

    public NewsSourceDownloader(MainActivity mainActivity,String s) {
        this.mainActivity = mainActivity;
		this.source = s;
		
    }
    private void handleResult(final String s)
    {

        if (s == null)
            mainActivity.noDataFound();
        mainActivity.runOnUiThread(new Runnable() {

            @Override
            public void run() {

                    ArrayList<Sources> sources_data;
                sources_data = parseJSON(s);
                assert sources_data != null;
                    mainActivity.sources_data_to_add(sources_data);

            }
        });
    }

    private ArrayList<Sources> parseJSON(String s) {
        try {
            Log.d(TAG, "parseJSON: parsing JSON");

            ArrayList<Sources> sourcesList = new ArrayList<>();

            String Name = null;
            String Id = null;
            String Category = null;
            String Url = null;
            Log.d(TAG, "Souce List " + s);
            JSONObject jsonObject = new JSONObject(s);
            JSONArray sources = jsonObject.getJSONArray("sources");
            Log.d(TAG, "Length " + sources.length());

            for (int i = 0; i < sources.length(); i++) {
                JSONObject source_object = (JSONObject) sources.get(i);
                Id = source_object.getString("id");
                Name = source_object.getString("name");
                Url = source_object.getString("url");
                Category = source_object.getString("category");
                sourcesList.add(new Sources(Id, Name, Url, Category));
            }

            return sourcesList;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void run() {
        Uri dataUri;
         if (source.equals("all")) {
            dataUri = Uri.parse(URL_ALL);
        } else
            dataUri = Uri.parse(URL_CATEGORY_HEAD + source + URL_CATEGORY_TAIL + KEY);
        String urlToUse = dataUri.toString();

        StringBuilder sb = new StringBuilder();
        try {
            URL url = new URL(urlToUse);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.addRequestProperty("User-Agent","");

                InputStream is = conn.getInputStream();
                BufferedReader reader = new BufferedReader((new InputStreamReader(is)));

                String line;
                while ((line = reader.readLine()) != null) {
                    sb.append(line).append('\n');
                }

               handleResult(sb.toString());


            } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

}
