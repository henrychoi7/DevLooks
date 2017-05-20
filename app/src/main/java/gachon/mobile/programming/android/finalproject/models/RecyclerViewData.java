package gachon.mobile.programming.android.finalproject.models;

import android.os.Parcel;
import android.os.Parcelable;

public class RecyclerViewData implements Parcelable {
    private int image_resources;
    private String title;
    private String content;

    public RecyclerViewData() {}

    private RecyclerViewData(Parcel in) {
        image_resources = in.readInt();
        title = in.readString();
        content = in.readString();
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(image_resources);
        dest.writeString(title);
        dest.writeString(content);
    }

    public int getImage_resources() {
        return image_resources;
    }

    public void setImage_resources(int image_resources) {
        this.image_resources = image_resources;
    }

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
}
