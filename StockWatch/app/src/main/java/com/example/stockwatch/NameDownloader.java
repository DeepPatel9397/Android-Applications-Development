package com.example.stockwatch;

import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
//extends AsyncTask<String,Void, String>
public class NameDownloader implements Runnable {
    private static final String TAG = "NameDownloader";
    private MainActivity mainActivity;
    private HashMap<String, String> stockData = new HashMap<>();
    private final String STOCK_SYMBOLS_URL = "https://api.iextrading.com/1.0/ref-data/symbols";


    public NameDownloader(MainActivity mainActivity) {
        this.mainActivity = mainActivity;
    }


    private void handleResult(String s)
    {
        parseJSONData(s);
        mainActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mainActivity.getStockSymbolData(stockData);
            }
        });
    }
   

    private void parseJSONData(String s) {
        try {
            JSONArray jsonArray = new JSONArray(s);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                stockData.put(jsonObject.getString("symbol"), jsonObject.getString("name"));
            }
            Log.d(TAG, "parseJSON: stock data parsed to hash map " + stockData.size());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        Uri.Builder buildURL = Uri.parse(STOCK_SYMBOLS_URL).buildUpon();
        String urlToUse = buildURL.build().toString();
        Log.d(TAG, "doBackground: " + urlToUse);

        StringBuilder sb = new StringBuilder();
        try {

            URL url = new URL(urlToUse);

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            Log.d(TAG, "doInBackground: Response Code: " + conn.getResponseCode() + ", " + conn.getResponseMessage());

            conn.setRequestMethod("GET");

            InputStream is = conn.getInputStream();
            BufferedReader reader = new BufferedReader((new InputStreamReader(is)));

            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line).append('\n');
            }

            Log.d(TAG, "doInBackground: " + sb.toString());

        } catch (Exception e) {
            Log.e(TAG, "doInBackground: ", e);

        }
        handleResult(sb.toString());

    }
}

