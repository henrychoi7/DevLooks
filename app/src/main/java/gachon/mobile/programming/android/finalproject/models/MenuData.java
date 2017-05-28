package gachon.mobile.programming.android.finalproject.models;

/**
 * Created by JJSOFT-DESKTOP on 2017-05-21.
 */

public class MenuData {
    private int groupId;
    private int itemId;
    private int order;
    private String title;
    private Integer resourceIcon;

    public MenuData(int groupId, int order) {
        this.groupId = groupId;
        this.order = order;
    }

    public int getItemId() {
        return itemId;
    }

    public void setItemId(int itemId) {
        this.itemId = itemId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getResourceIcon() {
        return resourceIcon;
    }

    public void setResourceIcon(Integer resourceIcon) {
        this.resourceIcon = resourceIcon;
    }

    public int getGroupId() {
        return groupId;
    }

    public int getOrder() {
        return order;
    }
}
