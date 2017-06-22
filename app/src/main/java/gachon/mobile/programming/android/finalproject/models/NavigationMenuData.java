package gachon.mobile.programming.android.finalproject.models;

import java.util.ArrayList;

public class NavigationMenuData {
    private int type;
    private String title;
    private Integer imageResource;
    private boolean isFavorite = false;
    private ArrayList<NavigationMenuData> invisibleChildren;

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getImageResource() {
        return imageResource;
    }

    public void setImageResource(Integer imageResource) {
        this.imageResource = imageResource;
    }

    public ArrayList<NavigationMenuData> getInvisibleChildren() {
        return invisibleChildren;
    }

    public void setInvisibleChildren(ArrayList<NavigationMenuData> invisibleChildren) {
        this.invisibleChildren = invisibleChildren;
    }

    public boolean isFavorite() {
        return isFavorite;
    }

    public void setFavorite(boolean favorite) {
        isFavorite = favorite;
    }
}
