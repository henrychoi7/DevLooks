package gachon.mobile.programming.android.finalproject.models;

import java.util.ArrayList;

public class FavoritesCategoryData {
    private boolean isSuccess;
    private ArrayList<FavoritesCategoryCodeData> data;

    public boolean isSuccess() {
        return isSuccess;
    }

    public ArrayList<FavoritesCategoryCodeData> getData() {
        return data;
    }
}
