package gachon.mobile.programming.android.finalproject.models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by JJSOFT-DESKTOP on 2017-05-26.
 */

public class OnOffMixErrorData implements Parcelable {
    private int code;
    private String message;

    private OnOffMixErrorData(Parcel in) {
        code = in.readInt();
        message = in.readString();
    }

    public static final Creator<OnOffMixErrorData> CREATOR = new Creator<OnOffMixErrorData>() {
        @Override
        public OnOffMixErrorData createFromParcel(Parcel in) {
            return new OnOffMixErrorData(in);
        }

        @Override
        public OnOffMixErrorData[] newArray(int size) {
            return new OnOffMixErrorData[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(code);
        dest.writeString(message);
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
