package com.example.assignment5_newsgateway;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Date;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link NewsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class NewsFragment extends Fragment {

    private static final String TAG = "NewsFragment";
    static MainActivity ma;

    public TextView articleTitle;
    public TextView articleDate;
    public TextView articleAuthor;
    public ImageView articleImage;
    public TextView articleDescription;
    public TextView articleCount;


    public static final NewsFragment newInstance(String title, String description, String Url, String newsnumber, String date, String author, MainActivity mainActivity, String News_Url) {
        NewsFragment fragment = new NewsFragment();
        Bundle bundle = new Bundle(7);
        bundle.putString("title", title);
        bundle.putString("description", description);
        bundle.putString("image", Url);
        bundle.putString("number", newsnumber);
        bundle.putString("date", date);
        bundle.putString("author", author);
        bundle.putString("url", News_Url);
        ma = mainActivity;
        fragment.setArguments(bundle);
        return fragment;
    }




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_news, container, false);
        try {
            articleTitle = view.findViewById(R.id.titleID);
            articleDate = view.findViewById(R.id.dateID);
            articleAuthor = view.findViewById(R.id.authorID);
            articleImage = view.findViewById(R.id.imageID);
            articleDescription = view.findViewById(R.id.descriptionID);
            articleCount = view.findViewById(R.id.countID);
            final String articleUrl = getArguments().getString("url");

            String title = "";
            if (getArguments().getString("title") != null)
                title = getArguments().getString("title");

            articleTitle.setText(title);

            articleTitle.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d(TAG, "onClick: Clicked Title");
                    Uri uri = Uri.parse(articleUrl);
                    Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                    startActivity(intent);
                }
            });

            String publishedDate = getArguments().getString("date");
            if (publishedDate == null || publishedDate.equals("null")) {
                articleDate.setVisibility(View.GONE);
            }
            articleDate.setText(changeDateFormat(publishedDate));


            String author = getArguments().getString("author");
            if (author == null || author.equals("null")) {
                articleAuthor.setVisibility(View.GONE);
            }
            articleAuthor.setText(author);

            String imageUrl = getArguments().getString("image");
            setImage(articleImage, imageUrl);

            articleImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d(TAG, "onClick: Clicked Image");
                    Uri uri = Uri.parse(articleUrl);
                    Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                    startActivity(intent);
                }
            });

            String description = getArguments().getString("description");
            if (description == null || description.equals("null") || description.isEmpty()) {
                description = getString(R.string.no_description);
            }
            articleDescription.setText(description);

            articleDescription.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d(TAG, "onClick: Clicked Description");
                    Uri uri = Uri.parse(articleUrl);
                    Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                    startActivity(intent);
                }
            });
            articleDescription.setMovementMethod(new ScrollingMovementMethod());

            String articleNumber = getArguments().getString("number");
            articleCount.setText(articleNumber);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return view;

    }
    public void setImage(final ImageView articleImage, final String Url) {
        if (Url == null) {
            articleImage.setImageResource(R.drawable.no_img);
        } else {

            Picasso picasso = new Picasso.Builder(ma)
                    .listener(new Picasso.Listener() {
                        @Override
                        public void onImageLoadFailed(Picasso picasso, Uri uri, Exception exception) {
                            final String changedUrl = Url.replace("http:", "https:");
                            picasso.load(changedUrl)
                                    .error(R.drawable.brokenimage)
                                    .placeholder(R.drawable.placeholder)
                                    .into(articleImage);
                        }
                    })
                    .build();

            picasso.load(Url)
                    .error(R.drawable.brokenimage)
                    .placeholder(R.drawable.placeholder)
                    .into(articleImage);
        }
    }

    private String changeDateFormat(String articledate) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        Date date = null;
        try {
            if (articledate != null) {
                date = format.parse(articledate);
                SimpleDateFormat format_new = new SimpleDateFormat("MMM dd, yyyy HH:mm");
                String date1 = format_new.format(date);
                return date1;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";

    }
}

