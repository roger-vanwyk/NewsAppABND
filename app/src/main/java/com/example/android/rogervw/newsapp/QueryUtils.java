package com.example.android.rogervw.newsapp;

import android.text.TextUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

public class QueryUtils {

    // LOG_TAG error messages log
    private static final String LOG_TAG = QueryUtils.class.getSimpleName();
    private QueryUtils() throws IOException {
        }

    private String requestURL;
    // Query the guardian APIs data set and return {@link News}
    URL url = createUrl(null);

    // Make HTTP request and parse jsonResponse

    String jsonResponse;

 {

        jsonResponse = makeHttpRequest(url);

// Log a tag message of the error

     Throwable e = null;
     Log.e(LOG_TAG, "fetchNewsData: Problem making the HTTP request", null);
    }



// Extract data preferences from jsonResponse and create a {@link News} list

    List<News> news = extractFeatureFromJson(jsonResponse);

    private static URL createUrl(String stringUrl) {

        URL url = null;

        try {

            url = new URL(stringUrl);

        }
        catch (MalformedURLException e) {

            Log.e(LOG_TAG, "createUrl: Problem building the URL", e);

        }

        return url;

    }



    /**
     * Make an HTTP request to provided URL,
     * return a String as response
     */

    private static String makeHttpRequest(URL url) throws IOException {

        String jsonResponse = "";



// If the URL is null,return

        if (url == null) {

            return jsonResponse;

        }


        HttpURLConnection urlConnection = null;

        InputStream inputStream = null;

        try {

            urlConnection = (HttpURLConnection) url.openConnection();

            urlConnection.setReadTimeout(10000 /* milliseconds */);

            urlConnection.setConnectTimeout(15000 /* milliseconds */);

            urlConnection.setRequestMethod("GET");

            urlConnection.connect();



// If successful, read input stream and parse jsonResponse

            if (urlConnection.getResponseCode() == 200) {

                inputStream = urlConnection.getInputStream();

                jsonResponse = readFromStream(inputStream);

            } else {

                Log.e(LOG_TAG, "makeHttpRequest: Error response code: " +

                        urlConnection.getResponseCode());

            }

        } catch (IOException e) {

            Log.e(LOG_TAG, "makeHttpRequest: Problem retrieving the news JSON results", e);

        } finally {

            if (urlConnection != null) {

                urlConnection.disconnect();

            }

            if (inputStream != null) {

                inputStream.close();

            }

        }

        return jsonResponse;

    }



    /**
     * Convert the {@link InputStream} into a String which contains the
     * whole JSON response from the server.
     */

    private static String readFromStream(InputStream inputStream) throws IOException {

        StringBuilder output = new StringBuilder();

        if (inputStream != null) {

            InputStreamReader inputStreamReader = new InputStreamReader(inputStream,

                    Charset.forName("UTF-8"));

            BufferedReader reader = new BufferedReader(inputStreamReader);

            String line = reader.readLine();

            while (line != null) {

                output.append(line);

                line = reader.readLine();

            }

        }

        return output.toString();

    }


    private static List<News> extractFeatureFromJson(String newsJSON) {


// If the JSON string is empty, return

        if (TextUtils.isEmpty(newsJSON)) {

            return null;

        }

// Create an empty ArrayList to parse our {@link News} list

        List<News> news = new ArrayList<>();



/**
 * Parse jsonResponse string.
 * If there's a problem with the format of the jsonResponse,
 * our app will throw a JSONException.
 * Catch the exception to prevent the app from crashing,
 * and send a LOG_TAG message
 * */

        try {


// Change jsonResponse to jsonObject

            JSONObject baseJsonResponse = new JSONObject(newsJSON);

            JSONObject response = baseJsonResponse.getJSONObject("response");



// Use key "results" to extract the jsonArray

            JSONArray newsArray = response.getJSONArray("results");



// Create a {@link News} object for all news items

            for (int i = 0; i < newsArray.length(); i++) {

                JSONObject currentNewsItem = newsArray.getJSONObject(i);

                String webTitle = currentNewsItem.getString("webTitle");

                if (webTitle.contains("|")) {

                    String[] deleteRedundant = webTitle.split("\\|");

                    webTitle = deleteRedundant[0];
                }


                String webPubDate = currentNewsItem.getString("webPublicationDate");

                String type = currentNewsItem.getString("type");



                JSONObject blocks = currentNewsItem.getJSONObject("blocks");

                JSONArray blocksArray = blocks.getJSONArray("body");

                String body;

                if (blocksArray.length() != 0) {

                    JSONObject blocksObject = blocksArray.getJSONObject(0);

                    body = blocksObject.optString("bodyTextSummary", "");

                } else {

                    body = "";

                }


                String url = currentNewsItem.getString("webUrl");


                if (!currentNewsItem.has("fields")) {

                    continue;

                }


                String imageThumbnail;

                JSONObject fields = new JSONObject();
                if (fields.has("thumbnail")) {

                    imageThumbnail = fields.optString("thumbnail", "");

                } else {

                    imageThumbnail = null;
                }


                News newsItem = new News(webTitle, webPubDate, type, url, body,

                        imageThumbnail);



// Add the new {@link News} to the news items list

                news.add(newsItem);
            }

        } catch (JSONException e) {


// Prevent the app from crashing by catching the possible JSONException that might be thrown,

// and log a tag message

            Log.e("QueryUtils", "extractFeatureFromJson: +");

        }



// Return the list of news items

        return news;

    }

    public static List<News> fetchNewsData(Object mUrl) {
        return null;
    }
}