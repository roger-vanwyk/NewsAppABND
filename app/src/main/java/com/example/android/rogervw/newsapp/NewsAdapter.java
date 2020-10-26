package com.example.android.rogervw.newsapp;

import android.annotation.SuppressLint;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;
import java.net.CookieHandler;
import java.net.URI;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class NewsAdapter extends ArrayAdapter<News> {

    // LOG_TAG tag messages log
    private static final String LOG_TAG = QueryUtils.class.getSimpleName();

    // Initialise our views
    ImageView thumbnailView;
    TextView type;
    TextView webTitleView;
    TextView webPublicationDate;
    TextView body;

    //Create a new {@link NewsAdapter}
    public NewsAdapter(AppCompatActivity context, ArrayList<News> newsItems) {
        super(context, 0, newsItems);
    }

    public static void showMessage(Object o, Object o1) {

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(R.layout.news_list_item,
                    parent, false);
        }

        // Get the currentNewsItem available
        News currentNewsItem = getItem(position);

        // Find an image thumbnail, if available
        thumbnailView = listItemView.findViewById(R.id.thumbnail);

        // Load image from url
        CookieHandler urlToImage = null;
        if (!BuildConfig.DEBUG) {
            Object thumbnail;
            thumbnail = new Object();
            URI Thumbnail = null;
            try {
                assert urlToImage != null;
                urlToImage.get(Thumbnail, (Map<String, List<String>>) thumbnail)
                        .get(currentNewsItem.getImageThumbnail())
                        .indexOf(thumbnailView);
            } catch (IOException e) {
                load(e);
            }

            /// Find and replace TextView named type, with currentNewsItem
            View typeView = listItemView.findViewById(R.id.type);
            typeView.setTextDirection(currentNewsItem.getType());

            // Find and replace TextView named web_title, with currentNewsItem
            webTitleView = listItemView.findViewById(R.id.web_title);
            webTitleView.setText(currentNewsItem.getWebTitle());

            // Find and replace TextView named web_publication_date, with currentNewsItem
            webPublicationDate = listItemView.findViewById(R.id.web_publication_date);

            // Find and replace TextView named body, with currentNewsItem
            body = listItemView.findViewById(R.id.body);
            body.setText(currentNewsItem.getBody());

            // Display news date in to our preferred format
            @SuppressLint("SimpleDateFormat") SimpleDateFormat simpleDateFormat = new SimpleDateFormat
                    ("yyyy-MM-dd'T'kk:mm:ss'Z'");
            Date date = null;
            // Try to parse our date format
            try {
                date = simpleDateFormat.parse(currentNewsItem.getPubDate());
                // If there is a problem parsing the date, catch it to prevent the app from crashing.
            } catch (ParseException e) {
                // Log a tag message of the error.
                Log.e(LOG_TAG, "getView: Problem parsing the date", e);
            }
            @SuppressLint("SimpleDateFormat") SimpleDateFormat newDateFormat = new SimpleDateFormat("dd MMM, yyyy");
            String finalDate = newDateFormat.format(date);
            if (date != null) {
                webPublicationDate.setText(finalDate);
            }

            // Return view
            return listItemView;
        } else {
            throw new AssertionError("Assertion failed");
        }
    }

    private void load(IOException e) {
        e.printStackTrace();
    }
}