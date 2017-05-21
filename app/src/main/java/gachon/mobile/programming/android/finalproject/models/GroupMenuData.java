package gachon.mobile.programming.android.finalproject.models;

import java.util.ArrayList;

/**
 * Created by JJSOFT-DESKTOP on 2017-05-21.
 */

public class GroupMenuData {
    private String groupTitle;
    private Integer groupResourceIcon;
    private ArrayList<ChildMenuData> childMenuDataArrayList;

    public String getGroupTitle() {
        return groupTitle;
    }

    public void setGroupTitle(String groupTitle) {
        this.groupTitle = groupTitle;
    }


    public ArrayList<ChildMenuData> getChildMenuDataArrayList() {
        return childMenuDataArrayList;
    }

    public void setChildMenuDataArrayList(ArrayList<ChildMenuData> childMenuDataArrayList) {
        this.childMenuDataArrayList = childMenuDataArrayList;
    }

    public Integer getGroupResourceIcon() {
        return groupResourceIcon;
    }

    public void setGroupResourceIcon(Integer groupResourceIcon) {
        this.groupResourceIcon = groupResourceIcon;
    }
}
