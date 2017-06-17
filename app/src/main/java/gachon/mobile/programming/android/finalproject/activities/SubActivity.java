package gachon.mobile.programming.android.finalproject.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.animation.OvershootInterpolator;
import android.widget.TextView;

import com.baoyz.widget.PullRefreshLayout;

import java.util.ArrayList;

import gachon.mobile.programming.android.finalproject.R;
import gachon.mobile.programming.android.finalproject.adapters.RecyclerViewAdapter;
import gachon.mobile.programming.android.finalproject.models.RecyclerViewData;
import gachon.mobile.programming.android.finalproject.presenters.SubActivityPresenter;
import gachon.mobile.programming.android.finalproject.utils.BaseActivity;
import gachon.mobile.programming.android.finalproject.views.SubActivityView;
import jp.wasabeef.recyclerview.adapters.ScaleInAnimationAdapter;
import jp.wasabeef.recyclerview.animators.SlideInUpAnimator;

import static gachon.mobile.programming.android.finalproject.utils.ApplicationClass.DisplayCustomToast;

/**
 * Created by JJSOFT-DESKTOP on 2017-06-04.
 */

public class SubActivity extends BaseActivity implements SubActivityView {
    private SubActivityView.UserInteractions mSubActivityPresenter;
    private int mSelectedGroupValue = 0;
    private String mSelectedTitle;
    private RecyclerView mRecyclerView;
    private RecyclerViewAdapter mRecyclerViewAdapter;
    private ArrayList<RecyclerViewData> mFinalRecyclerViewData = new ArrayList<>();
    private int pageCount = 1;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sub);

        final Toolbar toolbarLogin = (Toolbar) findViewById(R.id.toolbar_sub);
        setSupportActionBar(toolbarLogin);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayShowTitleEnabled(false);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        Intent selectedIntent = getIntent();
        mSelectedTitle = getString(R.string.app_name);
        if (selectedIntent != null) {
            mSelectedGroupValue = selectedIntent.getIntExtra("group_value", 0);
            mSelectedTitle = selectedIntent.getStringExtra("child_title");
        }

        TextView subTitle = (TextView) findViewById(R.id.text_view_sub_title);
        subTitle.setText(mSelectedTitle);

        mSubActivityPresenter = new SubActivityPresenter(SubActivity.this, this);
        mSubActivityPresenter.refreshDisplay(mSelectedGroupValue, mSelectedTitle, pageCount);

        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view_sub);
        mRecyclerView.setItemAnimator(new SlideInUpAnimator());
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                /*if (mFinalRecyclerViewData.size() == 0) {
                    return;
                }*/

                int lastVisibleItemPosition = ((LinearLayoutManager)recyclerView.getLayoutManager()).findLastCompletelyVisibleItemPosition();
                int itemTotalCount = mFinalRecyclerViewData.size() - 1;

                if (lastVisibleItemPosition == itemTotalCount) {
                    pageCount++;
                    mSubActivityPresenter.refreshDisplay(mSelectedGroupValue, mSelectedTitle, pageCount);
                }
            }
        });

        final PullRefreshLayout pullRefreshLayout = (PullRefreshLayout) findViewById(R.id.pull_to_refresh_sub);
        pullRefreshLayout.setOnRefreshListener(() -> {
            clearStackedData();
            mSubActivityPresenter.refreshDisplay(mSelectedGroupValue, mSelectedTitle, pageCount);
            pullRefreshLayout.setRefreshing(false);
        });
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
    public void setDisplayRecyclerView(ArrayList<RecyclerViewData> recyclerViewDataArrayList) {
        for (RecyclerViewData recyclerViewData : recyclerViewDataArrayList) {
            mFinalRecyclerViewData.add(recyclerViewData);
        }
        mRecyclerViewAdapter = new RecyclerViewAdapter(getApplicationContext(), mFinalRecyclerViewData);
        ScaleInAnimationAdapter scaleInAnimationAdapter = new ScaleInAnimationAdapter(mRecyclerViewAdapter);
        scaleInAnimationAdapter.setFirstOnly(true);
        scaleInAnimationAdapter.setDuration(500);
        scaleInAnimationAdapter.setInterpolator(new OvershootInterpolator(1f));
        mRecyclerView.setAdapter(scaleInAnimationAdapter);
    }

    @Override
    public void addAdditionalData(ArrayList<RecyclerViewData> additionalRecyclerViewData) {
        mFinalRecyclerViewData = mRecyclerViewAdapter.add(additionalRecyclerViewData, mFinalRecyclerViewData.size());
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(getApplicationContext(), MainActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
        super.onBackPressed();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            startActivity(new Intent(getApplicationContext(), MainActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
        }

        return super.onOptionsItemSelected(item);
    }

    private void clearStackedData() {
        pageCount = 1;
        mFinalRecyclerViewData = new ArrayList<>();
    }
}
