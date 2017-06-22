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
import static gachon.mobile.programming.android.finalproject.utils.ApplicationClass.clearPreferencesData;
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
    private SharedPreferences mSharedPreferences;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayShowTitleEnabled(false);
        }

        mMainActivityPresenter = new MainActivityPresenter(MainActivity.this, this);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        final ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, mDrawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        mDrawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        final NavigationView leftNavigationView = (NavigationView) findViewById(R.id.left_navigation);
        leftNavigationView.setNavigationItemSelectedListener(this);
        final View headerView = leftNavigationView.getHeaderView(0);

        // SharedPreference를 통해 사용자이름과 이메일주소 좌측 네비게이션바에 표시
        mSharedPreferences = getSharedPreferences(PREF_ID, Activity.MODE_PRIVATE);
        final TextView userNameTextView = (TextView) headerView.findViewById(R.id.username_text_view);
        userNameTextView.setText(mSharedPreferences.getString("name", getString(R.string.username)));
        final TextView emailTextView = (TextView) headerView.findViewById(R.id.email_text_view);
        emailTextView.setText(mSharedPreferences.getString("email", getString(R.string.email)));

        mBottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom_navigation);

        // 콘텐츠 리스트를 표시할 리사이클러뷰에 스크롤 끝에 도달시 추가 자료 불러오도록 작업
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

                final int lastVisibleItemPosition = ((LinearLayoutManager)recyclerView.getLayoutManager()).findLastCompletelyVisibleItemPosition();
                final int itemTotalCount = mFinalRecyclerViewData.size() - 1;

                if (lastVisibleItemPosition == itemTotalCount) {
                    pageCount++;
                    mMainActivityPresenter.changeCategory(mBottomNavigationView.getMenu().findItem(mBottomNavigationView.getSelectedItemId()), pageCount);
                }
            }
        });

        // 스크롤의 처음에서 아래로 끌어당길시 Refresh 되도록 작업
        final PullRefreshLayout pullRefreshLayout = (PullRefreshLayout) findViewById(R.id.pull_to_refresh_main);
        pullRefreshLayout.setOnRefreshListener(() -> {
            clearStackedData();
            mMainActivityPresenter.changeCategory(mBottomNavigationView.getMenu().findItem(mBottomNavigationView.getSelectedItemId()), pageCount);
            pullRefreshLayout.setRefreshing(false);
        });

        // 인터넷권한을 체크하여 데이터를 불러오도록 작업
        if (super.checkPermissionAndSetDisplayData()) {
            mMainActivityPresenter.refreshDisplay(mFinalRecyclerViewData);
            //DisplayCustomToast(getApplicationContext(), "인터넷 권한얻음");
        } else {
            DisplayCustomToast(getApplicationContext(), "권한이 없어서 자료를 불러오지 못했습니다.");
        }


        // 로그아웃 버튼을 통해 로그아웃시 자동로그인 체크와 사용자 정보 초기화
        final TextView logoutTextView = (TextView) findViewById(R.id.action_logout);
        logoutTextView.setOnClickListener(v -> {
            clearPreferencesData(mSharedPreferences);
            startActivity(new Intent(getApplicationContext(), LoginActivity.class).addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY));
            finish();
        });

        // 개인정보 설정창으로 이동하여 정보 수정 혹은 탈퇴 작업
        final TextView settingTextView = (TextView) findViewById(R.id.action_setting);
        settingTextView.setOnClickListener(v -> startActivity(new Intent(getApplicationContext(), SettingActivity.class)));
    }

    // 좌측 네비게이션바에 카테고리 구성과 즐겨찾기 한 내용 체크 작업
    @Override
    public void setExpandableMenuItems(final ArrayList<NavigationMenuData> navigationMenuDataArrayList) {
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
    public void showProgressDialog(final ProgressDialog subscribeProgressDialog, final String message) {
        subscribeProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        subscribeProgressDialog.setMessage(message);
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

    // 하단 네비게이션바에 사용자가 즐겨찾기 해놓은 카테고리 구성
    @Override
    public void setBottomMenuItems(final ArrayList<MenuData> menuDataArrayList) {
        final BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom_navigation);
        bottomNavigationView.inflateMenu(R.menu.menu_main_bottom_nav);
        final Menu menu = bottomNavigationView.getMenu();
        menu.add(0, HOME_VALUE, 0, getString(R.string.title_home)).setIcon(R.drawable.ic_home_black_24dp);

        for (final MenuData menuData : menuDataArrayList) {
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

    // 콘텐츠 리스트화면 구성
    @Override
    public void setDisplayRecyclerView(final ArrayList<RecyclerViewData> recyclerViewDataArrayList) {
        for (final RecyclerViewData recyclerViewData : recyclerViewDataArrayList) {
            mFinalRecyclerViewData.add(recyclerViewData);
        }
        mRecyclerViewAdapter = new RecyclerViewAdapter(getApplicationContext(), mFinalRecyclerViewData);
        final ScaleInAnimationAdapter scaleInAnimationAdapter = new ScaleInAnimationAdapter(mRecyclerViewAdapter);
        scaleInAnimationAdapter.setFirstOnly(true);
        scaleInAnimationAdapter.setDuration(500);
        scaleInAnimationAdapter.setInterpolator(new OvershootInterpolator(1f));
        mRecyclerView.setAdapter(scaleInAnimationAdapter);
    }

    // 추가적인 데이터를 불러 온 후 기존리스트에 데이터 추가 작업
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

    // 비어있는 메뉴를 구성한 후,setBottomMenuItems() 를 통해 하단 네비게이션 바 구축 작업
    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    // 검색화면으로 이동하는 작업
    @Override
    public boolean onOptionsItemSelected(final MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_search:
                startActivity(new Intent(getApplicationContext(), SearchActivity.class));
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    // 하단 네비게이션 클릭시 해당하는 카테고리의 데이터 불러오기 작업
    @Override
    public boolean onNavigationItemSelected(@NonNull final MenuItem item) {
        clearStackedData();
        mMainActivityPresenter.changeCategory(item, pageCount);

        mDrawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    // 스크롤로 인한 추가데이터 보내온것을 초기화
    private void clearStackedData() {
        pageCount = 1;
        mFinalRecyclerViewData = new ArrayList<>();
    }
}
