package gachon.mobile.programming.android.finalproject.models;

/**
 * Created by JJSOFT-DESKTOP on 2017-05-28.
 */

public class FavoritesCategoryCodeData {
    private String categoryCode;

    private FavoritesCategoryCodeData(String categoryCode) {
        this.categoryCode = categoryCode;
    }

    public String getCategoryCode() {
        return categoryCode;
    }
}