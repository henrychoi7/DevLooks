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

    void showProgressDialog(ProgressDialog subscribeProgressDialog);

    void dismissProgressDialog(ProgressDialog subscribeProgressDialog);

    void showCustomToast(String message);

    void setDisplayRecyclerView(ArrayList<RecyclerViewData> recyclerViewDataArrayList);

    interface UserInteractions {
        void refreshDisplay();
    }
}
