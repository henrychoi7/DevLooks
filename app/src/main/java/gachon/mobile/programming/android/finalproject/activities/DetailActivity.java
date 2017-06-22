package gachon.mobile.programming.android.finalproject.activities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;

import gachon.mobile.programming.android.finalproject.R;
import gachon.mobile.programming.android.finalproject.models.RecyclerViewData;
import gachon.mobile.programming.android.finalproject.models.SingleData;
import gachon.mobile.programming.android.finalproject.utils.BaseActivity;
import gachon.mobile.programming.android.finalproject.utils.ExceptionHelper;
import gachon.mobile.programming.android.finalproject.views.DetailActivityView;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import okhttp3.RequestBody;

import static gachon.mobile.programming.android.finalproject.utils.ApplicationClass.DisplayCustomToast;
import static gachon.mobile.programming.android.finalproject.utils.ApplicationClass.MEDIA_TYPE_JSON;
import static gachon.mobile.programming.android.finalproject.utils.ApplicationClass.PREF_ID;
import static gachon.mobile.programming.android.finalproject.utils.ApplicationClass.RETROFIT_INTERFACE;

/**
 * Created by JJSOFT-DESKTOP on 2017-06-05.
 */

public class DetailActivity extends BaseActivity implements DetailActivityView {
    private RecyclerViewData mRecyclerViewData = new RecyclerViewData();
    private SharedPreferences mSharedPreferences;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        final Toolbar toolbarLogin = (Toolbar) findViewById(R.id.toolbar_detail);
        setSupportActionBar(toolbarLogin);

        final ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayShowTitleEnabled(false);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        final TextView detailTitle = (TextView) findViewById(R.id.text_view_detail_title);

        mSharedPreferences = getApplicationContext().getSharedPreferences(PREF_ID, Activity.MODE_PRIVATE);

        final Intent selectedIntent = getIntent();
        if (selectedIntent != null) {
            mRecyclerViewData = selectedIntent.getParcelableExtra("selectedData");
            detailTitle.setText(mRecyclerViewData.getTitle());
            setWebViewFromHtml(mRecyclerViewData.getContentUrl());
        }
    }

    @Override
    public void onBackPressed() {
        finish();
        super.onBackPressed();
    }

    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
        getMenuInflater().inflate(R.menu.menu_sub, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(final MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
            case R.id.action_content_favorites:
                final String email = mSharedPreferences.getString("email", null);
                final String password = mSharedPreferences.getString("password", null);
                final JSONObject jsonObject = new JSONObject();
                try {
                    jsonObject.put("email", email);
                    jsonObject.put("password", password);
                    jsonObject.put("content_title", mRecyclerViewData.getTitle());
                    jsonObject.put("content_url", mRecyclerViewData.getContentUrl());
                    jsonObject.put("content_tag", mRecyclerViewData.getTags());
                    jsonObject.put("content_summary", mRecyclerViewData.getContent());
                    jsonObject.put("content_watch_count", mRecyclerViewData.getWatchCount());
                    jsonObject.put("content_favorites_count", mRecyclerViewData.getFavoritesCount());
                } catch (final JSONException e) {
                    DisplayCustomToast(getApplicationContext(), ExceptionHelper.getApplicationExceptionMessage(e));
                    break;
                }

                final Observable<SingleData> storeCategoryRx = RETROFIT_INTERFACE.StoreContentRx(RequestBody.create(MEDIA_TYPE_JSON, jsonObject.toString()));
                storeCategoryRx.subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(t -> DisplayCustomToast(getApplicationContext(), t.getData()),
                                e -> DisplayCustomToast(getApplicationContext(), ExceptionHelper.getApplicationExceptionMessage((Exception) e)));
                break;
            case R.id.action_copy:
                final ClipboardManager clipboardManager = (ClipboardManager) getApplicationContext().getSystemService(Context.CLIPBOARD_SERVICE);
                clipboardManager.setPrimaryClip(ClipData.newPlainText("text", mRecyclerViewData.getContentUrl()));
                DisplayCustomToast(getApplicationContext(), getApplicationContext().getString(R.string.complete_to_copy));
                break;
            case R.id.action_share:
                final String userName = mSharedPreferences.getString("name", null);

                final Intent intentForShare = new Intent(Intent.ACTION_SEND);
                intentForShare.putExtra(Intent.EXTRA_SUBJECT, getApplicationContext().getString(R.string.from_devLooks) + userName + getApplicationContext().getString(R.string.share_data) + "\n");
                intentForShare.putExtra(Intent.EXTRA_TEXT, getString(R.string.title) + " : " + mRecyclerViewData.getTitle() + "\n\n" + mRecyclerViewData.getContentUrl());
                intentForShare.setType("text/plain");

                getApplicationContext().startActivity(Intent.createChooser(intentForShare, getString(R.string.share)).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void showProgressDialog(final ProgressDialog subscribeProgressDialog) {
        subscribeProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        subscribeProgressDialog.setMessage(getResources().getString(R.string.loading));
        subscribeProgressDialog.setCancelable(false);
        subscribeProgressDialog.show();
    }

    @Override
    public void dismissProgressDialog(final ProgressDialog subscribeProgressDialog) {
        subscribeProgressDialog.dismiss();
    }

    @Override
    public void showCustomToast(final String message) {
        DisplayCustomToast(getApplicationContext(), message);
    }

    @Override
    public void setWebViewFromHtml(final String baseUrl) {
        final WebView htmlWebView = (WebView) findViewById(R.id.html_web_view);

        final WebSettings webSettings = htmlWebView.getSettings();
        // 자바스크립트 허용
        webSettings.setJavaScriptEnabled(true);
        webSettings.setDomStorageEnabled(true);
        webSettings.setDatabaseEnabled(true);

        // 캐시추가
        final File cacheDir = getCacheDir();
        if (!cacheDir.exists()) {
            cacheDir.mkdirs();
        }
        webSettings.setAppCachePath(cacheDir.getPath());
        webSettings.setAppCacheEnabled(true);

        //배경색
        htmlWebView.setBackgroundColor(0);
        //웹뷰 컨텐츠 사이즈 맞추기
        htmlWebView.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        //Zoom 허용
        htmlWebView.getSettings().setBuiltInZoomControls(true);
        //Zoom 허용
        htmlWebView.getSettings().setSupportZoom(true);
        //웹뷰 확대된것 비율맞추기 %
        htmlWebView.setInitialScale(1);
        //meata 태그의 viewport 사용 가능
        htmlWebView.getSettings().setLoadWithOverviewMode(true);
        //html 스크린크기를 웹뷰에 맞춰줌
        htmlWebView.getSettings().setUseWideViewPort(true);
        //웹뷰의 뒤로가기
        htmlWebView.goBack();

        //콘텐츠 URL로 이동
        htmlWebView.loadUrl(baseUrl);

        // 현재 타겟 API가 17이므로 안써도됨.
        /*if (Build.VERSION.SDK_INT < Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
            // 버전 4.0이하
            htmlWebView.loadData(htmlSources, "text/html", "UTF-8");
        } else {
            // 버전 4.1이상
            htmlWebView.loadData(htmlSources, "text/html; charset=UTF-8", null);
        }*/
    }
}
