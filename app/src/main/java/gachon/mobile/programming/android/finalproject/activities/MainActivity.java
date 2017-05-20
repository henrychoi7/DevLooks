package gachon.mobile.programming.android.finalproject.activities;

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
import android.view.animation.OvershootInterpolator;

import com.baoyz.widget.PullRefreshLayout;

import java.util.ArrayList;

import gachon.mobile.programming.android.finalproject.R;
import gachon.mobile.programming.android.finalproject.adapters.RecyclerViewAdapter;
import gachon.mobile.programming.android.finalproject.models.RecyclerViewData;
import gachon.mobile.programming.android.finalproject.utils.BaseActivity;
import jp.wasabeef.recyclerview.adapters.ScaleInAnimationAdapter;
import jp.wasabeef.recyclerview.animators.SlideInUpAnimator;

import static gachon.mobile.programming.android.finalproject.utils.ApplicationClass.handleUserApplicationExit;

public class MainActivity extends BaseActivity
        implements NavigationView.OnNavigationItemSelectedListener, BottomNavigationView.OnNavigationItemSelectedListener {
    private DrawerLayout mDrawerLayout;
    private ArrayList<RecyclerViewData> mRecyclerViewDataArrayList;

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

        setDisplayRecyclerView();

        final PullRefreshLayout pullRefreshLayout = (PullRefreshLayout) findViewById(R.id.pull_to_refresh);
        pullRefreshLayout.setOnRefreshListener(() -> {
            setDisplayRecyclerView();
            pullRefreshLayout.setRefreshing(false);
        });
    }

    private void setDisplayRecyclerView() {
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_view_main);
        recyclerView.setItemAnimator(new SlideInUpAnimator());
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

        mRecyclerViewDataArrayList = new ArrayList<>();
        RecyclerViewAdapter mRecyclerViewAdapter = new RecyclerViewAdapter(mRecyclerViewDataArrayList);
        ScaleInAnimationAdapter scaleInAnimationAdapter = new ScaleInAnimationAdapter(mRecyclerViewAdapter);
        scaleInAnimationAdapter.setFirstOnly(true);
        scaleInAnimationAdapter.setDuration(500);
        scaleInAnimationAdapter.setInterpolator(new OvershootInterpolator(1f));
        setRecyclerViewData();
        recyclerView.setAdapter(scaleInAnimationAdapter);
    }

    private void setRecyclerViewData() {
        RecyclerViewData recyclerViewData = new RecyclerViewData();
        recyclerViewData.setImage_resources(R.drawable.insideout);
        recyclerViewData.setTitle("This is insideout Title...................................................................................");
        recyclerViewData.setContent("This is insideout Content...................................................................................");
        mRecyclerViewDataArrayList.add(recyclerViewData);

        recyclerViewData = new RecyclerViewData();
        recyclerViewData.setImage_resources(R.drawable.toystory);
        recyclerViewData.setTitle("This is toystory Title...................................................................................");
        recyclerViewData.setContent("This is toystory Content...................................................................................");
        mRecyclerViewDataArrayList.add(recyclerViewData);

        recyclerViewData = new RecyclerViewData();
        recyclerViewData.setImage_resources(R.drawable.insideout);
        recyclerViewData.setTitle("This is insideout Title...................................................................................");
        recyclerViewData.setContent("This is insideout Content...................................................................................");
        mRecyclerViewDataArrayList.add(recyclerViewData);

        recyclerViewData = new RecyclerViewData();
        recyclerViewData.setImage_resources(R.drawable.toystory);
        recyclerViewData.setTitle("This is toystory Title...................................................................................");
        recyclerViewData.setContent("This is toystory Content...................................................................................");
        mRecyclerViewDataArrayList.add(recyclerViewData);

        recyclerViewData = new RecyclerViewData();
        recyclerViewData.setImage_resources(R.drawable.insideout);
        recyclerViewData.setTitle("This is insideout Title...................................................................................");
        recyclerViewData.setContent("This is insideout Content...................................................................................");
        mRecyclerViewDataArrayList.add(recyclerViewData);

        recyclerViewData = new RecyclerViewData();
        recyclerViewData.setImage_resources(R.drawable.toystory);
        recyclerViewData.setTitle("This is toystory Title...................................................................................");
        recyclerViewData.setContent("This is toystory Content...................................................................................");
        mRecyclerViewDataArrayList.add(recyclerViewData);

        recyclerViewData = new RecyclerViewData();
        recyclerViewData.setImage_resources(R.drawable.insideout);
        recyclerViewData.setTitle("This is insideout Title...................................................................................");
        recyclerViewData.setContent("This is insideout Content...................................................................................");
        mRecyclerViewDataArrayList.add(recyclerViewData);

        recyclerViewData = new RecyclerViewData();
        recyclerViewData.setImage_resources(R.drawable.toystory);
        recyclerViewData.setTitle("This is toystory Title...................................................................................");
        recyclerViewData.setContent("This is toystory Content...................................................................................");
        mRecyclerViewDataArrayList.add(recyclerViewData);

        recyclerViewData = new RecyclerViewData();
        recyclerViewData.setImage_resources(R.drawable.insideout);
        recyclerViewData.setTitle("This is insideout Title...................................................................................");
        recyclerViewData.setContent("This is insideout Content...................................................................................");
        mRecyclerViewDataArrayList.add(recyclerViewData);

        recyclerViewData = new RecyclerViewData();
        recyclerViewData.setImage_resources(R.drawable.toystory);
        recyclerViewData.setTitle("This is toystory Title...................................................................................");
        recyclerViewData.setContent("This is toystory Content...................................................................................");
        mRecyclerViewDataArrayList.add(recyclerViewData);

        recyclerViewData = new RecyclerViewData();
        recyclerViewData.setImage_resources(R.drawable.insideout);
        recyclerViewData.setTitle("This is insideout Title...................................................................................");
        recyclerViewData.setContent("This is insideout Content...................................................................................");
        mRecyclerViewDataArrayList.add(recyclerViewData);

        recyclerViewData = new RecyclerViewData();
        recyclerViewData.setImage_resources(R.drawable.toystory);
        recyclerViewData.setTitle("This is toystory Title...................................................................................");
        recyclerViewData.setContent("This is toystory Content...................................................................................");
        mRecyclerViewDataArrayList.add(recyclerViewData);

        recyclerViewData = new RecyclerViewData();
        recyclerViewData.setImage_resources(R.drawable.insideout);
        recyclerViewData.setTitle("This is insideout Title...................................................................................");
        recyclerViewData.setContent("This is insideout Content...................................................................................");
        mRecyclerViewDataArrayList.add(recyclerViewData);

        recyclerViewData = new RecyclerViewData();
        recyclerViewData.setImage_resources(R.drawable.toystory);
        recyclerViewData.setTitle("This is toystory Title...................................................................................");
        recyclerViewData.setContent("This is toystory Content...................................................................................");
        mRecyclerViewDataArrayList.add(recyclerViewData);

        recyclerViewData = new RecyclerViewData();
        recyclerViewData.setImage_resources(R.drawable.insideout);
        recyclerViewData.setTitle("This is insideout Title...................................................................................");
        recyclerViewData.setContent("This is insideout Content...................................................................................");
        mRecyclerViewDataArrayList.add(recyclerViewData);

        recyclerViewData = new RecyclerViewData();
        recyclerViewData.setImage_resources(R.drawable.toystory);
        recyclerViewData.setTitle("This is toystory Title...................................................................................");
        recyclerViewData.setContent("This is toystory Content...................................................................................");
        mRecyclerViewDataArrayList.add(recyclerViewData);

        recyclerViewData = new RecyclerViewData();
        recyclerViewData.setImage_resources(R.drawable.insideout);
        recyclerViewData.setTitle("This is insideout Title...................................................................................");
        recyclerViewData.setContent("This is insideout Content...................................................................................");
        mRecyclerViewDataArrayList.add(recyclerViewData);

        recyclerViewData = new RecyclerViewData();
        recyclerViewData.setImage_resources(R.drawable.toystory);
        recyclerViewData.setTitle("This is toystory Title...................................................................................");
        recyclerViewData.setContent("This is toystory Content...................................................................................");
        mRecyclerViewDataArrayList.add(recyclerViewData);

        recyclerViewData = new RecyclerViewData();
        recyclerViewData.setImage_resources(R.drawable.insideout);
        recyclerViewData.setTitle("This is insideout Title...................................................................................");
        recyclerViewData.setContent("This is insideout Content...................................................................................");
        mRecyclerViewDataArrayList.add(recyclerViewData);

        recyclerViewData = new RecyclerViewData();
        recyclerViewData.setImage_resources(R.drawable.toystory);
        recyclerViewData.setTitle("This is toystory Title...................................................................................");
        recyclerViewData.setContent("This is toystory Content...................................................................................");
        mRecyclerViewDataArrayList.add(recyclerViewData);

        recyclerViewData = new RecyclerViewData();
        recyclerViewData.setImage_resources(R.drawable.insideout);
        recyclerViewData.setTitle("This is insideout Title...................................................................................");
        recyclerViewData.setContent("This is insideout Content...................................................................................");
        mRecyclerViewDataArrayList.add(recyclerViewData);

        recyclerViewData = new RecyclerViewData();
        recyclerViewData.setImage_resources(R.drawable.toystory);
        recyclerViewData.setTitle("This is toystory Title...................................................................................");
        recyclerViewData.setContent("This is toystory Content...................................................................................");
        mRecyclerViewDataArrayList.add(recyclerViewData);

        recyclerViewData = new RecyclerViewData();
        recyclerViewData.setImage_resources(R.drawable.insideout);
        recyclerViewData.setTitle("This is insideout Title...................................................................................");
        recyclerViewData.setContent("This is insideout Content...................................................................................");
        mRecyclerViewDataArrayList.add(recyclerViewData);

        recyclerViewData = new RecyclerViewData();
        recyclerViewData.setImage_resources(R.drawable.toystory);
        recyclerViewData.setTitle("This is toystory Title...................................................................................");
        recyclerViewData.setContent("This is toystory Content...................................................................................");
        mRecyclerViewDataArrayList.add(recyclerViewData);

        recyclerViewData = new RecyclerViewData();
        recyclerViewData.setImage_resources(R.drawable.insideout);
        recyclerViewData.setTitle("This is insideout Title...................................................................................");
        recyclerViewData.setContent("This is insideout Content...................................................................................");
        mRecyclerViewDataArrayList.add(recyclerViewData);

        recyclerViewData = new RecyclerViewData();
        recyclerViewData.setImage_resources(R.drawable.toystory);
        recyclerViewData.setTitle("This is toystory Title...................................................................................");
        recyclerViewData.setContent("This is toystory Content...................................................................................");
        mRecyclerViewDataArrayList.add(recyclerViewData);

        recyclerViewData = new RecyclerViewData();
        recyclerViewData.setImage_resources(R.drawable.insideout);
        recyclerViewData.setTitle("This is insideout Title...................................................................................");
        recyclerViewData.setContent("This is insideout Content...................................................................................");
        mRecyclerViewDataArrayList.add(recyclerViewData);

        recyclerViewData = new RecyclerViewData();
        recyclerViewData.setImage_resources(R.drawable.toystory);
        recyclerViewData.setTitle("This is toystory Title...................................................................................");
        recyclerViewData.setContent("This is toystory Content...................................................................................");
        mRecyclerViewDataArrayList.add(recyclerViewData);

        recyclerViewData = new RecyclerViewData();
        recyclerViewData.setImage_resources(R.drawable.insideout);
        recyclerViewData.setTitle("This is insideout Title...................................................................................");
        recyclerViewData.setContent("This is insideout Content...................................................................................");
        mRecyclerViewDataArrayList.add(recyclerViewData);

        recyclerViewData = new RecyclerViewData();
        recyclerViewData.setImage_resources(R.drawable.toystory);
        recyclerViewData.setTitle("This is toystory Title...................................................................................");
        recyclerViewData.setContent("This is toystory Content...................................................................................");
        mRecyclerViewDataArrayList.add(recyclerViewData);

        recyclerViewData = new RecyclerViewData();
        recyclerViewData.setImage_resources(R.drawable.insideout);
        recyclerViewData.setTitle("This is insideout Title...................................................................................");
        recyclerViewData.setContent("This is insideout Content...................................................................................");
        mRecyclerViewDataArrayList.add(recyclerViewData);

        recyclerViewData = new RecyclerViewData();
        recyclerViewData.setImage_resources(R.drawable.toystory);
        recyclerViewData.setTitle("This is toystory Title...................................................................................");
        recyclerViewData.setContent("This is toystory Content...................................................................................");
        mRecyclerViewDataArrayList.add(recyclerViewData);
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
                setDisplayRecyclerView();
                break;
            case 0:
                setDisplayRecyclerView();
                break;
            case 1:
                setDisplayRecyclerView();
                break;
            case 2:
                setDisplayRecyclerView();
                break;
            case 3:
                setDisplayRecyclerView();
                break;
        }

        mDrawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }
}
