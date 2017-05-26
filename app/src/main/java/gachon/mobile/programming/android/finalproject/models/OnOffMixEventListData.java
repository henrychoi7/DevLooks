package gachon.mobile.programming.android.finalproject.models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by JJSOFT-DESKTOP on 2017-05-26.
 */

public class OnOffMixEventListData implements Parcelable {
    private String title;
    private int totalCanAttend;
    private String bannerUrl;

    private OnOffMixEventListData(Parcel in) {
        title = in.readString();
        totalCanAttend = in.readInt();
        bannerUrl = in.readString();
    }

    public static final Creator<OnOffMixEventListData> CREATOR = new Creator<OnOffMixEventListData>() {
        @Override
        public OnOffMixEventListData createFromParcel(Parcel in) {
            return new OnOffMixEventListData(in);
        }

        @Override
        public OnOffMixEventListData[] newArray(int size) {
            return new OnOffMixEventListData[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(title);
        dest.writeInt(totalCanAttend);
        dest.writeString(bannerUrl);
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBannerUrl() {
        return bannerUrl;
    }

    public void setBannerUrl(String bannerUrl) {
        this.bannerUrl = bannerUrl;
    }

    public int getTotalCanAttend() {
        return totalCanAttend;
    }

    public void setTotalCanAttend(int totalCanAttend) {
        this.totalCanAttend = totalCanAttend;
    }
}
