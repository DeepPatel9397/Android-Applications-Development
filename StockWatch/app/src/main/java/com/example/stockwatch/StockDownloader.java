package com.example.stockwatch;

import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class StockDownloader implements Runnable {
    private static final String TAG = "StockDownloader";
    private final String symbol;
    private MainActivity mainActivity;
    private final String STOCK_DOWNLOAD_URL_PRE = "https://cloud.iexapis.com/stable/stock/";
    private final String STOCK_DOWNLOAD_URL_TRAIL = "/quote?token=pk_7cc99dd3562644b9a73c4656f2cd986a";
    Stock stock = new Stock();

    public StockDownloader(MainActivity mainActivity,String s) {
        this.mainActivity = mainActivity;
        this.symbol = s;
    }


    private void handleResult(String s)
    {
        parseJSONData(s);
        mainActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mainActivity.updateStockList(stock);
            }
        });
    }


    void parseJSONData(String s) {
        try {
            JSONObject obj = new JSONObject(s);
            stock.setStockSymbol(obj.getString("symbol"));
            stock.setCompanyName(obj.getString("companyName"));
            stock.setPrice(obj.getDouble("latestPrice"));
            stock.setPriceChange(obj.getDouble("change"));
            stock.setPercentageChange(obj.getDouble("changePercent"));

        } catch (JSONException e) {
            e.printStackTrace();
        }


    }

    @Override
    public void run() {

        Uri.Builder buildURL = Uri.parse(STOCK_DOWNLOAD_URL_PRE + symbol + STOCK_DOWNLOAD_URL_TRAIL).buildUpon();
        String urlToUse = buildURL.build().toString();
        Log.d(TAG, "doInBackground: " + urlToUse);

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
