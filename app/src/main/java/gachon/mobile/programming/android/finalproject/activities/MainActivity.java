package gachon.mobile.programming.android.finalproject.activities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.OvershootInterpolator;
import android.widget.TextView;

import com.baoyz.widget.PullRefreshLayout;

import org.w3c.dom.Text;

import java.util.ArrayList;

import gachon.mobile.programming.android.finalproject.R;
import gachon.mobile.programming.android.finalproject.adapters.ExpandableMenuAdapter;
import gachon.mobile.programming.android.finalproject.adapters.RecyclerViewAdapter;
import gachon.mobile.programming.android.finalproject.models.MenuData;
import gachon.mobile.programming.android.finalproject.models.NavigationMenuData;
import gachon.mobile.programming.android.finalproject.models.RecyclerViewData;
import gachon.mobile.programming.android.finalproject.presenters.MainActivityPresenter;
import gachon.mobile.programming.android.finalproject.utils.BaseActivity;
import gachon.mobile.programming.android.finalproject.views.MainActivityView;
import jp.wasabeef.recyclerview.adapters.ScaleInAnimationAdapter;
import jp.wasabeef.recyclerview.animators.ScaleInRightAnimator;
import jp.wasabeef.recyclerview.animators.SlideInUpAnimator;

import static gachon.mobile.programming.android.finalproject.utils.ApplicationClass.DisplayCustomToast;
import static gachon.mobile.programming.android.finalproject.utils.ApplicationClass.HOME_VALUE;
import static gachon.mobile.programming.android.finalproject.utils.ApplicationClass.PREF_ID;
import static gachon.mobile.programming.android.finalproject.utils.ApplicationClass.handleUserApplicationExit;

public class MainActivity extends BaseActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        BottomNavigationView.OnNavigationItemSelectedListener,
        MainActivityView {
    private DrawerLayout mDrawerLayout;
    private ArrayList<RecyclerViewData> mFinalRecyclerViewData = new ArrayList<>();
    private RecyclerViewAdapter mRecyclerViewAdapter;
    private RecyclerView mRecyclerView;
    private int pageCount = 1;
    private BottomNavigationView mBottomNavigationView;
    private MainActivityView.UserInteractions mMainActivityPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayShowTitleEnabled(false);
        }

        //mMainActivityPresenter = new MainActivityPresenter(getApplicationContext(), this);
        mMainActivityPresenter = new MainActivityPresenter(MainActivity.this, this);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, mDrawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        mDrawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        final NavigationView leftNavigationView = (NavigationView) findViewById(R.id.left_navigation);
        leftNavigationView.setNavigationItemSelectedListener(this);

        mBottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom_navigation);

        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view_main);
        mRecyclerView.setItemAnimator(new SlideInUpAnimator());
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                if (mBottomNavigationView.getSelectedItemId() == 0 || mFinalRecyclerViewData.size() == 0) {
                    return;
                }

                int lastVisibleItemPosition = ((LinearLayoutManager)recyclerView.getLayoutManager()).findLastCompletelyVisibleItemPosition();
                int itemTotalCount = mFinalRecyclerViewData.size() - 1;

                if (lastVisibleItemPosition == itemTotalCount) {
                    pageCount++;
                    mMainActivityPresenter.changeCategory(mBottomNavigationView.getMenu().findItem(mBottomNavigationView.getSelectedItemId()), pageCount);
                }
            }
        });

        final PullRefreshLayout pullRefreshLayout = (PullRefreshLayout) findViewById(R.id.pull_to_refresh_main);
        pullRefreshLayout.setOnRefreshListener(() -> {
            clearStackedData();
            mMainActivityPresenter.changeCategory(mBottomNavigationView.getMenu().findItem(mBottomNavigationView.getSelectedItemId()), pageCount);
            pullRefreshLayout.setRefreshing(false);
        });

        if (super.checkPermissionAndSetDisplayData()) {
            mMainActivityPresenter.refreshDisplay(mFinalRecyclerViewData);
            //DisplayCustomToast(getApplicationContext(), "인터넷 권한얻음");
        } else {
            DisplayCustomToast(getApplicationContext(), "권한이 없어서 자료를 불러오지 못했습니다.");
        }

        final TextView logoutTextView = (TextView) findViewById(R.id.action_logout);
        logoutTextView.setOnClickListener(v -> {
            SharedPreferences.Editor sharedPreferencesEditor = getSharedPreferences(PREF_ID, Activity.MODE_PRIVATE).edit();
            sharedPreferencesEditor.putBoolean("is_checked_auto_login", false);
            sharedPreferencesEditor.putString("email", null);
            sharedPreferencesEditor.putString("password", null);
            sharedPreferencesEditor.apply();

            startActivity(new Intent(getApplicationContext(), LoginActivity.class).addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY));
            finish();
        });
    }

    @Override
    public void setExpandableMenuItems(ArrayList<NavigationMenuData> navigationMenuDataArrayList) {
        final RecyclerView expandableMenu = (RecyclerView) findViewById(R.id.expandable_menu);
        expandableMenu.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        expandableMenu.setItemAnimator(new ScaleInRightAnimator());
        expandableMenu.setHasFixedSize(true);

        final ExpandableMenuAdapter expandableMenuAdapter = new ExpandableMenuAdapter(getApplicationContext(), this, navigationMenuDataArrayList);
        ScaleInAnimationAdapter scaleInAnimationAdapter = new ScaleInAnimationAdapter(expandableMenuAdapter);
        scaleInAnimationAdapter.setFirstOnly(true);

        expandableMenu.setAdapter(expandableMenuAdapter);
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
    public void setBottomMenuItems(ArrayList<MenuData> menuDataArrayList) {
        final BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom_navigation);
        bottomNavigationView.inflateMenu(R.menu.menu_main_bottom_navigation);
        final Menu menu = bottomNavigationView.getMenu();
        menu.add(0, HOME_VALUE, 0, getString(R.string.title_home)).setIcon(R.drawable.ic_home_black_24dp);

        for (MenuData menuData : menuDataArrayList) {
            // MAX COUNT = 5 (즉, 4개만 추가가능)
            // groupId, itemId, order, CharSequence
            if (menuData.getResourceIcon() == null) {
                menu.add(menuData.getGroupId(), menuData.getItemId(), menuData.getOrder(), menuData.getTitle());
            } else {
                menu.add(menuData.getGroupId(), menuData.getItemId(), menuData.getOrder(), menuData.getTitle()).setIcon(menuData.getResourceIcon());
            }
        }

        bottomNavigationView.setOnNavigationItemSelectedListener(this);
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
    public void addAdditionalData(final ArrayList<RecyclerViewData> additionalRecyclerViewData) {
        mFinalRecyclerViewData = mRecyclerViewAdapter.add(additionalRecyclerViewData, mFinalRecyclerViewData.size());
    }

    @Override
    public void onBackPressed() {
        if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            mDrawerLayout.closeDrawer(GravityCompat.START);
            return;
        }

        handleUserApplicationExit(getApplicationContext(), this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_search:
                startActivity(new Intent(getApplicationContext(), SearchActivity.class));
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        clearStackedData();
        mMainActivityPresenter.changeCategory(item, pageCount);

        mDrawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    private void clearStackedData() {
        pageCount = 1;
        mFinalRecyclerViewData = new ArrayList<>();
    }
}
