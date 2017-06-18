package gachon.mobile.programming.android.finalproject.views;

import android.app.ProgressDialog;

import java.util.ArrayList;

import gachon.mobile.programming.android.finalproject.models.MenuData;
import gachon.mobile.programming.android.finalproject.models.NavigationMenuData;
import gachon.mobile.programming.android.finalproject.models.RecyclerViewData;

/**
 * Created by JJSOFT-DESKTOP on 2017-05-21.
 */

public interface SubActivityView {

    void showProgressDialog(final ProgressDialog subscribeProgressDialog);

    void dismissProgressDialog(final ProgressDialog subscribeProgressDialog);

    void showCustomToast(final String message);

    void setDisplayRecyclerView(final ArrayList<RecyclerViewData> recyclerViewDataArrayList);

    void addAdditionalData(final ArrayList<RecyclerViewData> additionalRecyclerViewData);

    interface UserInteractions {
        void refreshDisplay(int groupValue, String childTitle, int pageCount);
    }
}
