package gachon.mobile.programming.android.finalproject.models;

import java.util.ArrayList;

/**
 * Created by JJSOFT-DESKTOP on 2017-06-17.
 */

public class UserData {
    private boolean isSuccess;
    private ArrayList<UserInfoData> data;

    public boolean isSuccess() {
        return isSuccess;
    }

    public void setSuccess(boolean success) {
        isSuccess = success;
    }

    public ArrayList<UserInfoData> getData() {
        return data;
    }

    public void setData(ArrayList<UserInfoData> data) {
        this.data = data;
    }
}
