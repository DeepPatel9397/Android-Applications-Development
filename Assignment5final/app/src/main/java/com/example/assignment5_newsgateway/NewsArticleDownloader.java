package com.example.assignment5_newsgateway;

import android.annotation.SuppressLint;
import android.net.Uri;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class NewsArticleDownloader implements Runnable {
    private final String URL_ARTICLE_HEAD = "https://newsapi.org/v2/top-headlines?pageSize=50&sources=";
    private final String URL_ARTICLE_TAIL = "&apiKey=";
    private final String KEY = "81fb0e8cfb714aa4ac35fb56c359b216";
    private static final String TAG = "NewsArticleDownloader";
    private String source;
    @SuppressLint("StaticFieldLeak")
    private NewsService newsService;
    ArrayList<Article> articles_data = new ArrayList<>();

    public NewsArticleDownloader(NewsService newsService, String s) {
        Log.d(TAG, "ArticleDownload: STARTED");
        this.newsService = newsService;
        this.source = s;

    }
    private void handleResult(final String s){
        if(s != null)
            Log.d(TAG, "datafound");

            Log.d(TAG, "JSONConversion: STARTED");
                parseJSON(s);


    }

    protected void parseJSON(String s) {
        ArrayList<Article> articalList = null;
        try {
            Log.d(TAG, "parseJSON");
            articalList = new ArrayList<>();
            String author, title, description , urlToImage , publishedAt , news_url ;
            JSONObject jObjMain = new JSONObject(s);

            JSONArray articles = jObjMain.getJSONArray("articles");
            Log.d(TAG, "Length " + articles.length());
            for (int i = 0; i < articles.length(); i++) {
                JSONObject source_object = (JSONObject) articles.get(i);
                author = source_object.getString("author");
                title = source_object.getString("title");
                description = source_object.getString("description");
                urlToImage = source_object.getString("urlToImage");
                publishedAt = source_object.getString("publishedAt");
                news_url = source_object.getString("url");
                articalList.add(new Article(author, title, description, urlToImage, publishedAt, news_url));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        newsService.setArticles(articalList);
    }

    @Override
    public void run() {
        Log.d(TAG, "doInBackground: STARTED");
        Uri dataUri;
        dataUri = Uri.parse(URL_ARTICLE_HEAD + source.toLowerCase() + URL_ARTICLE_TAIL + KEY);
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
                    Log.d(TAG, "doInBackground: string is "+(sb).toString() );
                }
            Log.d(TAG, "JSON Data"+sb.toString());

                handleResult(sb.toString());

        } catch (IOException ex) {
            ex.printStackTrace();
        }

    }
}
