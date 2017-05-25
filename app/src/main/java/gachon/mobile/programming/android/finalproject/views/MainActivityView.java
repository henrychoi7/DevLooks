package gachon.mobile.programming.android.finalproject.views;

import java.util.ArrayList;

import gachon.mobile.programming.android.finalproject.models.MenuData;
import gachon.mobile.programming.android.finalproject.models.NavigationMenuData;
import gachon.mobile.programming.android.finalproject.models.RecyclerViewData;

/**
 * Created by JJSOFT-DESKTOP on 2017-05-21.
 */

public interface MainActivityView {

    void setBottomMenuItems(ArrayList<MenuData> menuDataArrayList);

    void setDisplayRecyclerView(ArrayList<RecyclerViewData> recyclerViewDataArrayList);

    void setExpandableMenuItems(ArrayList<NavigationMenuData> groupMenuDataArrayList);

    interface UserInteractions {

        void changeCategory(int categoryId);

        void refreshDisplay();

        ArrayList<NavigationMenuData> getExpandableMenuData();
    }
}
