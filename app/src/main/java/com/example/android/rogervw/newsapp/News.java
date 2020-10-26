package com.example.android.rogervw.newsapp;

public class News {
    // Create String list
    private final String mWebTitle;
    private final String mDate;
    private final String mType;
    private final String mUrl;
    private final String mBody;
    private final String mImageThumbnail;

    public News(String webTitle, String webPubDate, String type, String url,
                String body, String imageThumbnail) {
        mWebTitle = webTitle;
        mDate = webPubDate;
        mType = type;
        mUrl = url;
        mBody = body;
        mImageThumbnail = imageThumbnail;
    }

    // Return title of article
    public String getWebTitle() {
        return mWebTitle;
    }

    // Return publication date of article
    public String getPubDate() {
        return mDate;
    }

    // Return article category
    public String getType() {
        return mType;
    }

    // Return web URL of full article
    public String getUrl() {
        return mUrl;
    }

    // Returns body text summary
    public String getBody() {
        return mBody;
    }

    // Return thumbnail image
    public String getImageThumbnail() {
        return mImageThumbnail;
    }
}
