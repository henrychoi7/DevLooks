package gachon.mobile.programming.android.finalproject.models;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;

public class RecyclerViewData implements Parcelable{
    private String imageUrl;
    private Bitmap imageResources;
    private String title;
    private String content;
    private String contentUrl;
    private String type;
    private String watchCount;
    private String favoritesCount;
    private String subInfo;
    private String tags;

    public RecyclerViewData() {}

    protected RecyclerViewData(Parcel in) {
        imageUrl = in.readString();
        imageResources = in.readParcelable(Bitmap.class.getClassLoader());
        title = in.readString();
        content = in.readString();
        contentUrl = in.readString();
        type = in.readString();
        watchCount = in.readString();
        favoritesCount = in.readString();
        subInfo = in.readString();
        tags = in.readString();
    }

    public static final Creator<RecyclerViewData> CREATOR = new Creator<RecyclerViewData>() {
        @Override
        public RecyclerViewData createFromParcel(Parcel in) {
            return new RecyclerViewData(in);
        }

        @Override
        public RecyclerViewData[] newArray(int size) {
            return new RecyclerViewData[size];
        }
    };

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

    public String getWatchCount() {
        return watchCount;
    }

    public void setWatchCount(String watchCount) {
        this.watchCount = watchCount;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(imageUrl);
        dest.writeParcelable(imageResources, flags);
        dest.writeString(title);
        dest.writeString(content);
        dest.writeString(contentUrl);
        dest.writeString(type);
        dest.writeString(watchCount);
        dest.writeString(favoritesCount);
        dest.writeString(subInfo);
        dest.writeString(tags);
    }
}
