package gachon.mobile.programming.android.finalproject.views;

import java.util.ArrayList;

import gachon.mobile.programming.android.finalproject.models.MenuData;
import gachon.mobile.programming.android.finalproject.models.RecyclerViewData;

/**
 * Created by JJSOFT-DESKTOP on 2017-05-21.
 */

public interface MainActivityView {

    void setBottomMenuItems(ArrayList<MenuData> menuDataArrayList);

    void setDisplayRecyclerView(ArrayList<RecyclerViewData> recyclerViewDataArrayList);

    interface UserInteractions {

        void changeCategory(int categoryId);

        void refreshDisplay();
    }
}
