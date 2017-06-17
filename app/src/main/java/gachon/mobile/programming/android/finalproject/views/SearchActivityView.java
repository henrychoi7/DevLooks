package gachon.mobile.programming.android.finalproject.views;

import android.app.ProgressDialog;
import java.util.ArrayList;
import gachon.mobile.programming.android.finalproject.models.RecyclerViewData;

public interface SearchActivityView {

    void showProgressDialog(ProgressDialog subscribeProgressDialog);

    void dismissProgressDialog(ProgressDialog subscribeProgressDialog);

    void showCustomToast(String message);

    void setDisplayRecyclerView(ArrayList<RecyclerViewData> recyclerViewDataArrayList);

    interface UserInteractions {
        void refreshDisplay(String searchValue);
    }
}