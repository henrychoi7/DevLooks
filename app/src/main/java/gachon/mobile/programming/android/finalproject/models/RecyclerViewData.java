package gachon.mobile.programming.android.finalproject.models;

import android.graphics.Bitmap;

public class RecyclerViewData {
    private String imageUrl;
    private Bitmap imageResources;
    private String title;
    private String content;

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
}
