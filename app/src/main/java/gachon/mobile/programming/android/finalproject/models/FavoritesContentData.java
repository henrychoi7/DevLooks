package gachon.mobile.programming.android.finalproject.models;

import java.util.ArrayList;

public class FavoritesContentData {
    private boolean isSuccess;
    private ArrayList<FavoritesContentDetailData> data;

    public boolean isSuccess() {
        return isSuccess;
    }

    public ArrayList<FavoritesContentDetailData> getData() {
        return data;
    }
}
