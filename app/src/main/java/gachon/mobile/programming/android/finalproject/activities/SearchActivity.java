package gachon.mobile.programming.android.finalproject.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.OvershootInterpolator;
import android.widget.TextView;

import java.util.ArrayList;

import gachon.mobile.programming.android.finalproject.R;
import gachon.mobile.programming.android.finalproject.adapters.RecyclerViewAdapter;
import gachon.mobile.programming.android.finalproject.models.RecyclerViewData;
import gachon.mobile.programming.android.finalproject.presenters.SearchActivityPresenter;
import gachon.mobile.programming.android.finalproject.utils.BaseActivity;
import gachon.mobile.programming.android.finalproject.views.SearchActivityView;
import jp.wasabeef.recyclerview.adapters.ScaleInAnimationAdapter;
import jp.wasabeef.recyclerview.animators.SlideInUpAnimator;

import static gachon.mobile.programming.android.finalproject.utils.ApplicationClass.DisplayCustomToast;

public class SearchActivity extends BaseActivity implements SearchActivityView {
    private SearchActivityView.UserInteractions mSearchActivityPresenter;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        final Toolbar toolbarSearch = (Toolbar) findViewById(R.id.toolbar_search);
        setSupportActionBar(toolbarSearch);

        final ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayShowTitleEnabled(false);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        mSearchActivityPresenter = new SearchActivityPresenter(SearchActivity.this, this);
    }

    // 검색을 통한 자료 불러오기 요청 작업
    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
        getMenuInflater().inflate(R.menu.menu_search, menu);
        final MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        searchView.setQueryHint(getResources().getString(R.string.require_search));
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(final String searchValue) {
                mSearchActivityPresenter.refreshDisplay(searchValue);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String searchValue) {
                return false;
            }
        });
        return true;
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

    // 검색을 통한 자료 불러온것 화면에 처리 작업
    @Override
    public void setDisplayRecyclerView(final ArrayList<RecyclerViewData> recyclerViewDataArrayList) {
        final TextView textViewSearchContent = (TextView) findViewById(R.id.text_view_search_content);
        final RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_view_search);

        if (recyclerViewDataArrayList.size() == 0) {
            textViewSearchContent.setVisibility(View.VISIBLE);
            textViewSearchContent.setText(getString(R.string.empty_search_value));
            recyclerView.setVisibility(View.GONE);
            return;
        } else {
            textViewSearchContent.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
        }

        recyclerView.setItemAnimator(new SlideInUpAnimator());
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

        final RecyclerViewAdapter recyclerViewAdapter = new RecyclerViewAdapter(getApplicationContext(), recyclerViewDataArrayList);
        ScaleInAnimationAdapter scaleInAnimationAdapter = new ScaleInAnimationAdapter(recyclerViewAdapter);
        scaleInAnimationAdapter.setFirstOnly(true);
        scaleInAnimationAdapter.setDuration(500);
        scaleInAnimationAdapter.setInterpolator(new OvershootInterpolator(1f));
        recyclerView.setAdapter(scaleInAnimationAdapter);
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(getApplicationContext(), MainActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
        super.onBackPressed();
    }

    @Override
    public boolean onOptionsItemSelected(final MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            startActivity(new Intent(getApplicationContext(), MainActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
        }

        return super.onOptionsItemSelected(item);
    }
}