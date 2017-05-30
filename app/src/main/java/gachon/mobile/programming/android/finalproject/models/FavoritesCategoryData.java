package gachon.mobile.programming.android.finalproject.models;

import java.util.ArrayList;

/**
 * Created by JJSOFT-DESKTOP on 2017-05-28.
 */

public class FavoritesCategoryData {
    private boolean isSuccess;
    private ArrayList<FavoritesCategoryCodeData> data;

    public FavoritesCategoryData(boolean isSuccess, ArrayList<FavoritesCategoryCodeData> data) {
        this.isSuccess = isSuccess;
        this.data = data;
    }

    public boolean isSuccess() {
        return isSuccess;
    }

    public ArrayList<FavoritesCategoryCodeData> getData() {
        return data;
    }
}
