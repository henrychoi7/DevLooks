package gachon.mobile.programming.android.finalproject.models;

import java.util.ArrayList;

/**
 * Created by JJSOFT-DESKTOP on 2017-05-28.
 */

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
