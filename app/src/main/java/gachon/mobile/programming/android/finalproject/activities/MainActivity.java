package gachon.mobile.programming.android.finalproject.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.NavigationView;
import android.support.v4.content.ContextCompat;
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
import android.widget.ExpandableListView;
import android.widget.Toast;

import com.baoyz.widget.PullRefreshLayout;

import java.util.ArrayList;

import gachon.mobile.programming.android.finalproject.R;
import gachon.mobile.programming.android.finalproject.adapters.ExpandableMenuAdapter;
import gachon.mobile.programming.android.finalproject.adapters.RecyclerViewAdapter;
import gachon.mobile.programming.android.finalproject.models.ChildMenuData;
import gachon.mobile.programming.android.finalproject.models.GroupMenuData;
import gachon.mobile.programming.android.finalproject.models.MenuData;
import gachon.mobile.programming.android.finalproject.models.RecyclerViewData;
import gachon.mobile.programming.android.finalproject.presenters.MainActivityPresenter;
import gachon.mobile.programming.android.finalproject.utils.BaseActivity;
import gachon.mobile.programming.android.finalproject.views.MainActivityView;
import jp.wasabeef.recyclerview.adapters.ScaleInAnimationAdapter;
import jp.wasabeef.recyclerview.animators.SlideInUpAnimator;

import static gachon.mobile.programming.android.finalproject.utils.ApplicationClass.DisplayCustomToast;
import static gachon.mobile.programming.android.finalproject.utils.ApplicationClass.HOME_VALUE;
import static gachon.mobile.programming.android.finalproject.utils.ApplicationClass.handleUserApplicationExit;

public class MainActivity extends BaseActivity
        implements NavigationView.OnNavigationItemSelectedListener, BottomNavigationView.OnNavigationItemSelectedListener, MainActivityView {
    private DrawerLayout mDrawerLayout;

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

        mMainActivityPresenter = new MainActivityPresenter(getApplicationContext(), this);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, mDrawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        mDrawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        final NavigationView leftNavigationView = (NavigationView) findViewById(R.id.left_navigation);
        if (leftNavigationView != null) {
            leftNavigationView.setNavigationItemSelectedListener(this);
//            setupDrawerContent(leftNavigationView);
        }

        final ExpandableListView expandableMenu = (ExpandableListView) findViewById(R.id.expandable_menu);
        ExpandableMenuAdapter expandableMenuAdapter = new ExpandableMenuAdapter(getExpandableMenuData());
        expandableMenu.setAdapter(expandableMenuAdapter);

        expandableMenu.setOnGroupClickListener((parent, v, groupPosition, id) -> {
            if (parent.isGroupExpanded(groupPosition)) {
                v.findViewById(R.id.group_image_view).setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_contracted_24dp));
            } else {
                v.findViewById(R.id.group_image_view).setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_expanded_24dp));
            }
            return false;
        });

        final PullRefreshLayout pullRefreshLayout = (PullRefreshLayout) findViewById(R.id.pull_to_refresh);
        pullRefreshLayout.setOnRefreshListener(() -> {
            final BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom_navigation);
            mMainActivityPresenter.changeCategory(bottomNavigationView.getSelectedItemId());
            pullRefreshLayout.setRefreshing(false);
        });

        if (super.checkPermissionAndSetDisplayData()) {
            mMainActivityPresenter.refreshDisplay();
            DisplayCustomToast(getApplicationContext(), "인터넷 권한얻음");
        } else {
            DisplayCustomToast(getApplicationContext(), "권한이 없어서 자료를 불러오지 못했습니다.");
        }
    }

    private ArrayList<GroupMenuData> getExpandableMenuData() {
        ArrayList<GroupMenuData> groupMenuDataArrayList = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            GroupMenuData groupMenuData = new GroupMenuData();
            groupMenuData.setGroupTitle("Group Test" + String.valueOf(i));
            //groupMenuData.setGroupResourceIcon(R.drawable.ic_contracted_24dp);
            groupMenuData.setGroupResourceIcon(null);

            ArrayList<ChildMenuData> childMenuDataArrayList = new ArrayList<>();
            for (int j = 0; j < 5; j++) {
                ChildMenuData childMenuData = new ChildMenuData();
                childMenuData.setChildTitle("Child Test" + String.valueOf(j));
                childMenuData.setChildResourceIcon(R.drawable.ic_menu_share);
                //childMenuData.setChildResourceIcon(null);
                childMenuDataArrayList.add(childMenuData);
            }
            groupMenuData.setChildMenuDataArrayList(childMenuDataArrayList);
            groupMenuDataArrayList.add(groupMenuData);
        }

        return groupMenuDataArrayList;
    }

    @Override
    public void setBottomMenuItems(ArrayList<MenuData> menuDataArrayList){
        final BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom_navigation);
        bottomNavigationView.inflateMenu(R.menu.main_bottom_navigation);
        final Menu menu = bottomNavigationView.getMenu();
        menu.add(0, HOME_VALUE, 0, getString(R.string.title_home)).setIcon(R.drawable.ic_home_black_24dp);

        for (MenuData menuData : menuDataArrayList) {
            // MAX COUNT = 5 (즉, 4개만 추가가능)
            // groupId, itemId, order, CharSequence
            menu.add(menuData.getGroupId(), menuData.getItemId(), menuData.getOrder(), menuData.getTitle()).setIcon(menuData.getResourceIcon());
        }

        bottomNavigationView.setOnNavigationItemSelectedListener(this);
    }

    @Override
    public void setDisplayRecyclerView(ArrayList<RecyclerViewData> recyclerViewDataArrayList) {
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_view_main);
        recyclerView.setItemAnimator(new SlideInUpAnimator());
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

        RecyclerViewAdapter recyclerViewAdapter = new RecyclerViewAdapter(recyclerViewDataArrayList);
        ScaleInAnimationAdapter scaleInAnimationAdapter = new ScaleInAnimationAdapter(recyclerViewAdapter);
        scaleInAnimationAdapter.setFirstOnly(true);
        scaleInAnimationAdapter.setDuration(500);
        scaleInAnimationAdapter.setInterpolator(new OvershootInterpolator(1f));
        recyclerView.setAdapter(scaleInAnimationAdapter);
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
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_search) {
            startActivity(new Intent(getApplicationContext(), SearchActivity.class));
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_camera:
                break;
            case R.id.nav_gallery:
                break;
            case R.id.nav_slideshow:
                break;
            case R.id.nav_manage:
                break;
            case R.id.nav_share:
                break;
            case R.id.nav_send:
                break;
            default:
                DisplayCustomToast(getApplicationContext(), String.valueOf(item.getItemId()));
                mMainActivityPresenter.changeCategory(item.getItemId());
                break;
            /*case R.id.nav_home:
                mMainActivityPresenter.changeCategory(item.getItemId());
                break;
            case 0:
                mMainActivityPresenter.changeCategory(item.getItemId());
                break;
            case 1:
                mMainActivityPresenter.changeCategory(item.getItemId());
                break;
            case 2:
                mMainActivityPresenter.changeCategory(R.id.nav_home);
                break;
            case 3:
                mMainActivityPresenter.changeCategory(R.id.nav_home);
                break;*/
        }

        mDrawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }
}
