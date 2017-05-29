package gachon.mobile.programming.android.finalproject.models;

import java.util.ArrayList;

/**
 * Created by JJSOFT-DESKTOP on 2017-05-28.
 */

public class FavoritesContentData {
    private boolean isSuccess;
    private ArrayList<String> data;

    public FavoritesContentData(boolean isSuccess, ArrayList<String> data) {
        this.isSuccess = isSuccess;
        this.data = data;
    }

    public boolean isSuccess() {
        return isSuccess;
    }

    public ArrayList<String> getData() {
        return data;
    }
}