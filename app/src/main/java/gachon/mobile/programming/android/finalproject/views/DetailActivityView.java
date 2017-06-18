package gachon.mobile.programming.android.finalproject.views;

import android.app.ProgressDialog;

import java.util.ArrayList;

import gachon.mobile.programming.android.finalproject.models.RecyclerViewData;

/**
 * Created by JJSOFT-DESKTOP on 2017-05-21.
 */

public interface DetailActivityView {

    void showProgressDialog(final ProgressDialog subscribeProgressDialog);

    void dismissProgressDialog(final ProgressDialog subscribeProgressDialog);

    void showCustomToast(final String message);

    void setWebViewFromHtml(final String baseUrl);

}
