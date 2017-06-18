package gachon.mobile.programming.android.finalproject.views;

import android.app.ProgressDialog;
import java.util.ArrayList;
import gachon.mobile.programming.android.finalproject.models.RecyclerViewData;

public interface SearchActivityView {

    void showProgressDialog(final ProgressDialog subscribeProgressDialog);

    void dismissProgressDialog(final ProgressDialog subscribeProgressDialog);

    void showCustomToast(final String message);

    void setDisplayRecyclerView(final ArrayList<RecyclerViewData> recyclerViewDataArrayList);

    interface UserInteractions {
        void refreshDisplay(final String searchValue);
    }
}