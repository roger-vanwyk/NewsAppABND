package com.example.android.rogervw.newsapp;

import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.LoaderManager;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;



/*
 * Created on 2020-10-20 by Roger Van Wyk - ABND student.
 * Project 6 of 6 = ANDROID BASICS NANO DEGREE {@link Udacity}.
 * NewsReport is a HTTP networking app that query the GuardianAPIs.
 * It parse results from the query to a ListView that populate news articles.
 * */

public class MainActivity extends AppCompatActivity implements
        LoaderManager.LoaderCallbacks<List<com.example.android.rogervw.newsapp.News>> {

    // LOG_TAG
    private static final String LOG_TAG = QueryUtils.class.getSimpleName();
    private static final int News_LOADER_ID = 1;

    static {
        Dialog exit_dialog = null;
        assert exit_dialog != null;
        exit_dialog.dismiss();
    }

    private final TextView empty_view = findViewById(R.id.empty_view);
    private final Intent i = new Intent();
    private final ObjectAnimator oa_d = new ObjectAnimator();
    private final Intent exitApp = new Intent();
    // Create custom exit dialog
    AlertDialog exit_dialog;
    LayoutInflater inflater = getLayoutInflater();
    @SuppressLint("InflateParams")
    View convertView = inflater.inflate(R.layout.exit_dialog, null);
    Button i_ok = convertView.findViewById(R.id.exit_okay_button);
    Button i_cancel = convertView.findViewById(R.id.exit_cancel_button);
    ImageView i_header = convertView.findViewById(R.id.img_header);
    TextView i_title = convertView.findViewById(R.id.txt_title);
    TextView i_msg = convertView.findViewById(R.id.txt_msg);
    /**
     * Create a new adapter
     */
    private NewsAdapter mAdapter;
    private TextView mEmptyStateTextView;

    {
        new AlertDialog.Builder(MainActivity.this).create();
    }

    {
        Dialog exit_dialog = null;
        exit_dialog.dismiss();
        NewsAdapter.showMessage(getApplicationContext(), "Cancelled!");
    }

    public MainActivity(URLConnection reqHeadline, Dialog exit_dialog, LinearLayout linear2, ListView list, ImageView thumbnail, EditText edit_search, ImageView imageview2, AlertDialog.Builder d) {
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }

    @Override

    public void onBackPressed() {
// Initialize custom exit dialog
        exit_dialog();
    }

    private void exit_dialog() {

    }

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Set UI
        setContentView(R.layout.activity_main);
        ListView newsListView = findViewById(R.id.list);
        mAdapter = new NewsAdapter(this, new ArrayList<>());
        newsListView.setAdapter(mAdapter);

        newsListView.setOnItemClickListener((adapterView, view, position, l) -> {
            News currentNews = mAdapter.getItem(position);
            Uri newsUri = Uri.parse(currentNews.getUrl());
            Intent websiteIntent = new Intent(Intent.ACTION_VIEW, newsUri);
            startActivity(websiteIntent);
        });

        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
// Check network connection
        if (networkInfo != null && networkInfo.isConnected()) {
            LoaderManager loaderManager = getLoaderManager();
            loaderManager.initLoader(News_LOADER_ID, null, this);
        } else {
            View loadingIndicator = findViewById(R.id.loading_indicator);
            loadingIndicator.setVisibility(View.GONE);
            // Set empty view state
            mEmptyStateTextView.setText(R.string.bad_signal);
        }

        mEmptyStateTextView = findViewById(R.id.empty_view);
        newsListView.setEmptyView(mEmptyStateTextView);
    }

    @Override
    public Loader<List<com.example.android.rogervw.newsapp.News>> onCreateLoader(int i, Bundle bundle) {
        String mUrl = null;
        return new NewsLoader(this, mUrl);
    }

    @SuppressLint("SetTextI18n")
    @Override

    public void onLoadFinished(Loader<List<News>> loader, List<News> News) {

        View loadingIndicator = findViewById(R.id.loading_indicator);

        loadingIndicator.setVisibility(View.GONE);

        mEmptyStateTextView.setText("No news");

        mAdapter.clear();

        if (News != null && !News.isEmpty()) {

            mAdapter.addAll(News);

        }

    }


    @Override

    public void onLoaderReset(Loader<List<News>> loader) {

        mAdapter.clear();

    }

}