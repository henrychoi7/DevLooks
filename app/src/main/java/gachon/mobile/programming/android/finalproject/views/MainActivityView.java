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

    void showProgressDialog(final ProgressDialog subscribeProgressDialog, final String message);

    void dismissProgressDialog(final ProgressDialog subscribeProgressDialog);

    void showCustomToast(final String message);

    void setBottomMenuItems(final ArrayList<MenuData> menuDataArrayList);

    void setDisplayRecyclerView(final ArrayList<RecyclerViewData> recyclerViewDataArrayList);

    void setExpandableMenuItems(final ArrayList<NavigationMenuData> groupMenuDataArrayList);

    void addAdditionalData(final ArrayList<RecyclerViewData> additionalRecyclerViewData);

    interface UserInteractions {

        void changeCategory(final MenuItem item, final int pageCount);

        void refreshDisplay(final ArrayList<RecyclerViewData> finalRecyclerViewData);
    }
}
