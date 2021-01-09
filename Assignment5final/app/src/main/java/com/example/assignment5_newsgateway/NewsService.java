package com.example.assignment5_newsgateway;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.util.Log;

import java.util.ArrayList;
import java.util.Objects;

public class NewsService extends Service {
    public NewsService() {
    }
    private MainActivity mainActivity;
    private static final String TAG = "NewsService";
    private boolean running = true;
    static ArrayList<Article> articlesList = new ArrayList<>();
    static final String ACTION_MSG_TO_SVC = "AMTS";
    static NewsServiceReceiver newsServiceReceiver;

    String[] Title;
    String[] Desc;
    String[] Urldata;
    String[] publishedAt;
    String[] author;
    String[] news_url;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        newsServiceReceiver = new NewsServiceReceiver();
        IntentFilter filter = new IntentFilter(ACTION_MSG_TO_SVC);
        registerReceiver(newsServiceReceiver, filter);

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    while (true) {
                        if (articlesList.size() == 0)
                            Thread.sleep(250);
                        else
                            sendMessage();
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
        return START_STICKY;
    }

    public void setArticles(ArrayList<Article> articles_data) {
        articlesList.clear();
        articlesList = articles_data;
    }

    private void sendMessage() {
        Intent intent = new Intent();
        intent.setAction(MainActivity.ACTION_NEWS_STORY);

        Title = new String[articlesList.size()];
        Desc = new String[articlesList.size()];
        Urldata = new String[articlesList.size()];
        publishedAt = new String[articlesList.size()];
        author = new String[articlesList.size()];
        news_url = new String[articlesList.size()];

        for (int i = 0; i < articlesList.size(); i++) {
            Title[i] = articlesList.get(i).getTitle();
            Desc[i] = articlesList.get(i).getDescription();
            Urldata[i] = articlesList.get(i).getUrlToImage();
            publishedAt[i] = articlesList.get(i).getPublishedAt();
            author[i] = articlesList.get(i).getAuthor();
            news_url[i] = articlesList.get(i).getUrlToArticle();
        }
        intent.putExtra("TITLE", Title);
        intent.putExtra("DESC", Desc);
        intent.putExtra("URL", Urldata);
        intent.putExtra("AUTHOR", author);
        intent.putExtra("PUBLISHEDAT", publishedAt);
        intent.putExtra("NEWSURL", news_url);
        sendBroadcast(intent);
        articlesList.clear();
    }

    @Override
    public void onDestroy() {
        Log.d(TAG, "onDestroy: Destroyed");
        unregisterReceiver(newsServiceReceiver);
        running = false;
        super.onDestroy();
    }

    class NewsServiceReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {

            switch (Objects.requireNonNull(intent.getAction())) {
                case ACTION_MSG_TO_SVC:
                    if (intent.hasExtra("SOURCE_DATA")) {
                       NewsArticleDownloader na =  new NewsArticleDownloader(NewsService.this, intent.getStringExtra("SOURCE_DATA"));
                        new Thread(na).start();
                    }
                    break;
            }
        }
    }
}
