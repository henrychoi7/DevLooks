package gachon.mobile.programming.android.finalproject.DTO;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by JJSoft on 2017-05-08.
 */

public class LoginData implements Parcelable {
    private String data;
    private String error;

    private LoginData(Parcel in) {
        data = in.readString();
        error = in.readString();
    }

    public static final Creator<LoginData> CREATOR = new Creator<LoginData>() {
        @Override
        public LoginData createFromParcel(Parcel in) {
            return new LoginData(in);
        }

        @Override
        public LoginData[] newArray(int size) {
            return new LoginData[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(data);
        dest.writeString(error);
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }
}
