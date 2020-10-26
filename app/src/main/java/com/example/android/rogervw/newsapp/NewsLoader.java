package com.example.android.rogervw.newsapp;

import android.content.AsyncTaskLoader;
import android.content.Context;

import java.util.List;

// Our {@link NewsLoader} extends an {@link AsyncTaskLoader}
// which we use to share data amongst activities as required

public class NewsLoader extends AsyncTaskLoader<List<News>> {

    // We create a private string for our query URL
    private String mUrl;

    // Creates a new {@link NewsLoader}
    // to load data from
    public NewsLoader(Context context, String url) {
        super(context);
        mUrl = url;
    }
    // Ensure that the data loads
    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    // We load in background
    @Override
    public List<News> loadInBackground() {
        if (mUrl == null) {
            return null;
        }

        // First check network by making HTTP network request,
        // We make an HTTP request, and parse the jsonResponse,
        // Then, we extract data to populate in to our listview
        List<News> news = QueryUtils.fetchNewsData(mUrl);
        return news;
    }
}
