package gachon.mobile.programming.android.finalproject.activities;

import android.app.Activity;
import android.content.Intent;
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
import android.widget.TextView;

import java.util.ArrayList;

import gachon.mobile.programming.android.finalproject.R;
import gachon.mobile.programming.android.finalproject.adapters.RecycleViewAdapter;
import gachon.mobile.programming.android.finalproject.models.RecycleViewData;
import gachon.mobile.programming.android.finalproject.utils.BaseActivity;

import static gachon.mobile.programming.android.finalproject.utils.ApplicationClass.handleUserApplicationExit;

/**
 * Created by JJSOFT-DESKTOP on 2017-05-09.
 */

public class MainActivity extends BaseActivity
        implements NavigationView.OnNavigationItemSelectedListener, BottomNavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout mDrawerLayout;
    private TextView mTextMessage;

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

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, mDrawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        mDrawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        final NavigationView leftNavigationView = (NavigationView) findViewById(R.id.left_navigation);
        leftNavigationView.setNavigationItemSelectedListener(this);

        final BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom_navigation);
        bottomNavigationView.inflateMenu(R.menu.main_bottom_navigation);
        Menu menu = bottomNavigationView.getMenu();

        // TODO - 카테고리 즐겨찾기한것 가져와야함 MAX COUNT = 5임 (즉 4개만 추가가능)
        // groupId, itemId, order, CharSequence
        menu.add(0, 0, 0, "TEXT0").setIcon(R.drawable.ic_search_white_24dp);
        menu.add(0, 1, 0, "TEXT1").setIcon(R.drawable.ic_dashboard_black_24dp);
        menu.add(0, 2, 0, "TEXT2").setIcon(R.drawable.ic_home_black_24dp);
        menu.add(0, 3, 0, "TEXT3").setIcon(R.drawable.ic_menu_camera);

        bottomNavigationView.setOnNavigationItemSelectedListener(this);

        //mTextMessage = (TextView) findViewById(R.id.message);

        /*final PullRefreshLayout pullRefreshLayout = (PullRefreshLayout) findViewById(R.id.pull_to_refresh);
        pullRefreshLayout.setOnRefreshListener(() -> {
            // Async 작업으로 true, false 주기
            pullRefreshLayout.setRefreshing(false);
        });*/

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_view_main);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

        ArrayList<RecycleViewData> recycleViewDataArrayList = new ArrayList<>();
        RecycleViewAdapter recycleViewAdapter = new RecycleViewAdapter(recycleViewDataArrayList);
        recyclerView.setAdapter(recycleViewAdapter);

        recycleViewDataArrayList.add(new RecycleViewData("#InsideOut", R.drawable.insideout));
        recycleViewDataArrayList.add(new RecycleViewData("#Mini", R.drawable.mini));
        recycleViewDataArrayList.add(new RecycleViewData("#ToyStroy", R.drawable.toystory));
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
            case R.id.navigation_home:
                //mTextMessage.setText(R.string.title_home);
                break;
            case 0:
                //mTextMessage.setText(item.getTitle());
                break;
            case 1:
                //mTextMessage.setText(item.getTitle());
                break;
            case 2:
                //mTextMessage.setText(item.getTitle());
                break;
            case 3:
                //mTextMessage.setText(item.getTitle());
                break;
        }

        mDrawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }
}
