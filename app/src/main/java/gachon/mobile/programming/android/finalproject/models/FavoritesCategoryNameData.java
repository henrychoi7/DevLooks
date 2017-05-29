package gachon.mobile.programming.android.finalproject.models;

/**
 * Created by JJSOFT-DESKTOP on 2017-05-28.
 */

public class FavoritesCategoryNameData {
    private String categoryName;

    private FavoritesCategoryNameData(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getCategoryName() {
        return categoryName;
    }
}