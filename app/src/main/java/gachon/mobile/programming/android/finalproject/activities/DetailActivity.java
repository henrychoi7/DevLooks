package gachon.mobile.programming.android.finalproject.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.view.MenuItem;
import android.webkit.WebView;
import android.widget.TextView;

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
            //mHtmlTextView.setMovementMethod(LinkMovementMethod.getInstance());

            mHtmlWebView.getSettings().setJavaScriptEnabled(true);

            mHtmlWebView.setHorizontalScrollBarEnabled(false);
            mHtmlWebView.setVerticalScrollBarEnabled(false);
            mHtmlWebView.setBackgroundColor(0);

            mDetailActivityPresenter.refreshDisplay(selectedUrl);

            //String selectedType = selectedIntent.getStringExtra("selectedType");
            /*if (selectedType.equals(STACK_OVERFLOW)) {
                textView.setText(STACK_OVERFLOW + selectedUrl);
            } else if (selectedType.equals(OKKY)) {
                textView.setText(OKKY + selectedUrl);
            } else if (selectedType.equals(ON_OFF_MIX)) {
                textView.setText(ON_OFF_MIX + selectedUrl);
            } else {
                textView.setText("ELSE" + selectedUrl);
            }*/

            /*textView.setText(fromHtml("<b><font color=#ff0000> Html View using TextView" +
                    "</font></b><br><br><a href='http://m.naver.com'>naver.com</a>" +
                    "<br><br><a href='http://mainia.tistory.com'>mainia.tistory.com</a>"));*/

        }
    }

    private static Spanned fromHtml(String source) {
        if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.N) {
            // noinspection deprecation
            return Html.fromHtml(source);
        }
        return Html.fromHtml(source, Html.FROM_HTML_MODE_LEGACY);
    }

    @Override
    public void onBackPressed() {
        //startActivity(new Intent(getApplicationContext(), SubActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
        finish();
        super.onBackPressed();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            //startActivity(new Intent(getApplicationContext(), SubActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
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
    public void setTextViewFromHtml(String htmlSources) {
        //mHtmlTextView.setText(fromHtml(htmlSources));

        // 버전 4.0이하
        //mHtmlWebView.loadData(htmlSources, "text/html", "UTF-8");

        // 버전 4.1이상
        mHtmlWebView.loadData(htmlSources, "text/html; charset=UTF-8", null);
    }
}
