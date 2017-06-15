package gachon.mobile.programming.android.finalproject.models;

import android.graphics.Bitmap;

public class RecyclerViewData {
    private String imageUrl;
    private Bitmap imageResources;
    private String title;
    private String content;
    private String contentUrl;
    private String type;
    private String favoritesCount;
    private String subInfo;

    public RecyclerViewData() {}

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Bitmap getImageResources() {
        return imageResources;
    }

    public void setImageResources(Bitmap imageResources) {
        this.imageResources = imageResources;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getContentUrl() {
        return contentUrl;
    }

    public void setContentUrl(String contentUrl) {
        this.contentUrl = contentUrl;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getFavoritesCount() {
        return favoritesCount;
    }

    public void setFavoritesCount(String favoritesCount) {
        this.favoritesCount = favoritesCount;
    }

    public String getSubInfo() {
        return subInfo;
    }

    public void setSubInfo(String subInfo) {
        this.subInfo = subInfo;
    }
}
