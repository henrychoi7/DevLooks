package gachon.mobile.programming.android.finalproject.views;

import android.app.ProgressDialog;

import java.util.ArrayList;

import gachon.mobile.programming.android.finalproject.models.RecyclerViewData;

/**
 * Created by JJSOFT-DESKTOP on 2017-05-21.
 */

public interface DetailActivityView {

    void showProgressDialog(ProgressDialog subscribeProgressDialog);

    void dismissProgressDialog(ProgressDialog subscribeProgressDialog);

    void showCustomToast(String message);

    void setWebViewFromHtml(String baseUrl, String htmlSources);

    interface UserInteractions {
        void refreshDisplay(String selectedUrl, String selectedType);
    }
}
