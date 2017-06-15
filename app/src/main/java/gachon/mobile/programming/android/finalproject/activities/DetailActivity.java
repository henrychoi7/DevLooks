package gachon.mobile.programming.android.finalproject.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.webkit.WebSettings;
import android.webkit.WebView;

import gachon.mobile.programming.android.finalproject.R;
import gachon.mobile.programming.android.finalproject.presenters.DetailActivityPresenter;
import gachon.mobile.programming.android.finalproject.utils.BaseActivity;
import gachon.mobile.programming.android.finalproject.views.DetailActivityView;

import static gachon.mobile.programming.android.finalproject.utils.ApplicationClass.DisplayCustomToast;

/**
 * Created by JJSOFT-DESKTOP on 2017-06-05.
 */

public class DetailActivity extends BaseActivity implements DetailActivityView {
    //private TextView mHtmlTextView;
    private WebView mHtmlWebView;
    private DetailActivityPresenter mDetailActivityPresenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        final Toolbar toolbarLogin = (Toolbar) findViewById(R.id.toolbar_sub);
        setSupportActionBar(toolbarLogin);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayShowTitleEnabled(false);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        mDetailActivityPresenter = new DetailActivityPresenter(DetailActivity.this, this);

        //mHtmlTextView = (TextView) findViewById(R.id.html_text_view);
        mHtmlWebView = (WebView) findViewById(R.id.html_web_view);

        Intent selectedIntent = getIntent();
        if (selectedIntent != null) {
            String selectedUrl = selectedIntent.getStringExtra("selectedUrl");
            String selectedType = selectedIntent.getStringExtra("selectedType");
            //mHtmlWebView.setHorizontalScrollBarEnabled(false);
            //mHtmlWebView.setVerticalScrollBarEnabled(false);
            mHtmlWebView.getSettings().setJavaScriptEnabled(true);
            //배경색
            mHtmlWebView.setBackgroundColor(0);
            //웹뷰 컨텐츠 사이즈 맞추기
            mHtmlWebView.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
            //Zoom 허용
            mHtmlWebView.getSettings().setBuiltInZoomControls(true);
            //Zoom 허용
            mHtmlWebView.getSettings().setSupportZoom(true);
            //웹뷰 확대된것 비율맞추기 %
            mHtmlWebView.setInitialScale(1);
            //meata 태그의 viewport 사용 가능
            mHtmlWebView.getSettings().setLoadWithOverviewMode(true);
            //html 스크린크기를 웹뷰에 맞춰줌
            mHtmlWebView.getSettings().setUseWideViewPort(true);
            //웹뷰의 뒤로가기
            mHtmlWebView.goBack();

            mHtmlWebView.loadUrl(selectedUrl);

            //mDetailActivityPresenter.refreshDisplay(selectedUrl, selectedType);
        }
    }

    @Override
    public void onBackPressed() {
        finish();
        super.onBackPressed();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void showProgressDialog(ProgressDialog subscribeProgressDialog) {
        subscribeProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        subscribeProgressDialog.setMessage(getResources().getString(R.string.loading));
        subscribeProgressDialog.setCancelable(false);
        subscribeProgressDialog.show();
    }

    @Override
    public void dismissProgressDialog(ProgressDialog subscribeProgressDialog) {
        subscribeProgressDialog.dismiss();
    }

    @Override
    public void showCustomToast(String message) {
        DisplayCustomToast(getApplicationContext(), message);
    }

    @Override
    public void setWebViewFromHtml(String baseUrl, String htmlSources) {
        //mHtmlWebView.loadData(htmlSources, "text/html; charset=UTF-8", null);
        //mHtmlWebView.loadDataWithBaseURL(baseUrl, htmlSources, "text/html", "charset=UTF-8", null);
        mHtmlWebView.loadUrl(baseUrl);
        // 현재 타겟 API가 17이므로 안써도됨.
        /*if (Build.VERSION.SDK_INT < Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
            // 버전 4.0이하
            mHtmlWebView.loadData(htmlSources, "text/html", "UTF-8");
        } else {
            // 버전 4.1이상
            mHtmlWebView.loadData(htmlSources, "text/html; charset=UTF-8", null);
        }*/
    }
}
