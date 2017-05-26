package gachon.mobile.programming.android.finalproject.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Created by JJSOFT-DESKTOP on 2017-05-26.
 */

public class OnOffMixData implements Parcelable {
    private OnOffMixErrorData error;
    private ArrayList<OnOffMixEventListData> eventList;

    private OnOffMixData(Parcel in) {
        error = in.readParcelable(OnOffMixErrorData.class.getClassLoader());
        eventList = in.createTypedArrayList(OnOffMixEventListData.CREATOR);
    }

    public static final Creator<OnOffMixData> CREATOR = new Creator<OnOffMixData>() {
        @Override
        public OnOffMixData createFromParcel(Parcel in) {
            return new OnOffMixData(in);
        }

        @Override
        public OnOffMixData[] newArray(int size) {
            return new OnOffMixData[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(error, flags);
        dest.writeTypedList(eventList);
    }

    public OnOffMixErrorData getError() {
        return error;
    }

    public void setError(OnOffMixErrorData error) {
        this.error = error;
    }

    public ArrayList<OnOffMixEventListData> getEventList() {
        return eventList;
    }

    public void setEventList(ArrayList<OnOffMixEventListData> eventList) {
        this.eventList = eventList;
    }
}
