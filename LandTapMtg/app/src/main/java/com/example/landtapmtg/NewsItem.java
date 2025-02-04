package com.example.landtapmtg;

public class NewsItem {
    private final String title;
    private final String url;
    private final String imageUrl;

    public NewsItem(String title, String url, String imageUrl) {
        this.title = title;
        this.url = url;
        this.imageUrl = imageUrl;
    }

    public String getTitle() {
        return title;
    }

    public String getUrl() {
        return url;
    }

    public String getImageUrl() {
        return imageUrl;
    }
}
