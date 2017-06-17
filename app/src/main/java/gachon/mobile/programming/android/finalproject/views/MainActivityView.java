package gachon.mobile.programming.android.finalproject.views;

import android.app.ProgressDialog;
import android.view.MenuItem;

import java.util.ArrayList;

import gachon.mobile.programming.android.finalproject.models.MenuData;
import gachon.mobile.programming.android.finalproject.models.NavigationMenuData;
import gachon.mobile.programming.android.finalproject.models.RecyclerViewData;

/**
 * Created by JJSOFT-DESKTOP on 2017-05-21.
 */

public interface MainActivityView {

    void showProgressDialog(ProgressDialog subscribeProgressDialog);

    void dismissProgressDialog(ProgressDialog subscribeProgressDialog);

    void showCustomToast(String message);

    void setBottomMenuItems(ArrayList<MenuData> menuDataArrayList);

    void setDisplayRecyclerView(ArrayList<RecyclerViewData> recyclerViewDataArrayList);

    void setExpandableMenuItems(ArrayList<NavigationMenuData> groupMenuDataArrayList);

    void addAdditionalData(final ArrayList<RecyclerViewData> additionalRecyclerViewData);

    interface UserInteractions {

        void changeCategory(final MenuItem item, final int pageCount);

        void refreshDisplay(final ArrayList<RecyclerViewData> finalRecyclerViewData);
    }
}
