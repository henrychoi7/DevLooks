package gachon.mobile.programming.android.finalproject.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
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
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sub);

        final Toolbar toolbarLogin = (Toolbar) findViewById(R.id.toolbar_sub);
        setSupportActionBar(toolbarLogin);

        final ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayShowTitleEnabled(false);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        final Intent selectedIntent = getIntent();
        mSelectedTitle = getString(R.string.app_name);
        if (selectedIntent != null) {
            mSelectedGroupValue = selectedIntent.getIntExtra("group_value", 0);
            mSelectedTitle = selectedIntent.getStringExtra("child_title");
        }

        ((TextView) findViewById(R.id.text_view_sub_title)).setText(mSelectedTitle);

        // 선택된 카테고리와 웹사이트를 파라미터로 넘겨주어 해당 자료 요청 작업
        mSubActivityPresenter = new SubActivityPresenter(SubActivity.this, this);
        mSubActivityPresenter.refreshDisplay(mSelectedGroupValue, mSelectedTitle, pageCount);

        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view_sub);
        mRecyclerView.setItemAnimator(new SlideInUpAnimator());
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        // 스크롤의 끝에 도달시 추가적인 자료를 불러오는 작업
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                final int lastVisibleItemPosition = ((LinearLayoutManager) recyclerView.getLayoutManager()).findLastCompletelyVisibleItemPosition();
                final int itemTotalCount = mFinalRecyclerViewData.size() - 1;

                if (lastVisibleItemPosition == itemTotalCount) {
                    pageCount++;
                    mSubActivityPresenter.refreshDisplay(mSelectedGroupValue, mSelectedTitle, pageCount);
                }
            }
        });

        // 스크롤의 처음에서 아래로 당길시 Refresh 되는 작업
        final PullRefreshLayout pullRefreshLayout = (PullRefreshLayout) findViewById(R.id.pull_to_refresh_sub);
        pullRefreshLayout.setOnRefreshListener(() -> {
            clearStackedData();
            mSubActivityPresenter.refreshDisplay(mSelectedGroupValue, mSelectedTitle, pageCount);
            pullRefreshLayout.setRefreshing(false);
        });
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

    // 선택된 자료 요청의 결과값 처리 작업
    @Override
    public void setDisplayRecyclerView(final ArrayList<RecyclerViewData> recyclerViewDataArrayList) {
        for (final RecyclerViewData recyclerViewData : recyclerViewDataArrayList) {
            mFinalRecyclerViewData.add(recyclerViewData);
        }
        mRecyclerViewAdapter = new RecyclerViewAdapter(getApplicationContext(), mFinalRecyclerViewData);
        ScaleInAnimationAdapter scaleInAnimationAdapter = new ScaleInAnimationAdapter(mRecyclerViewAdapter);
        scaleInAnimationAdapter.setFirstOnly(true);
        scaleInAnimationAdapter.setDuration(500);
        scaleInAnimationAdapter.setInterpolator(new OvershootInterpolator(1f));
        mRecyclerView.setAdapter(scaleInAnimationAdapter);
    }

    // 추가로 불러온 자료를 붙여주는 작업
    @Override
    public void addAdditionalData(final ArrayList<RecyclerViewData> additionalRecyclerViewData) {
        mFinalRecyclerViewData = mRecyclerViewAdapter.add(additionalRecyclerViewData, mFinalRecyclerViewData.size());
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

    // 추가로 불러왔던 작업을 초기화하는 작업
    private void clearStackedData() {
        pageCount = 1;
        mFinalRecyclerViewData = new ArrayList<>();
    }
}
