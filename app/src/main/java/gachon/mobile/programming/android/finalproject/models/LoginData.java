package gachon.mobile.programming.android.finalproject.models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by JJSOFT-DESKTOP on 2017-05-09.
 */

public class LoginData implements Parcelable {
    private boolean isSuccess;
    private String data;

    private LoginData(Parcel in) {
        isSuccess = in.readByte() != 0;
        data = in.readString();
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

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public boolean isSuccess() {
        return isSuccess;
    }

    public void setSuccess(boolean success) {
        isSuccess = success;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeByte((byte) (isSuccess ? 1 : 0));
        dest.writeString(data);
    }
}
