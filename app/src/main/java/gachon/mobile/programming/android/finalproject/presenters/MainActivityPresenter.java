package gachon.mobile.programming.android.finalproject.presenters;

import android.content.Context;

import java.util.ArrayList;

import gachon.mobile.programming.android.finalproject.R;
import gachon.mobile.programming.android.finalproject.enums.ExpandableMenuEnum;
import gachon.mobile.programming.android.finalproject.models.MenuData;
import gachon.mobile.programming.android.finalproject.models.NavigationMenuData;
import gachon.mobile.programming.android.finalproject.models.RecyclerViewData;
import gachon.mobile.programming.android.finalproject.views.MainActivityView;

import static gachon.mobile.programming.android.finalproject.utils.ApplicationClass.HOME_VALUE;
import static gachon.mobile.programming.android.finalproject.utils.ApplicationClass.MAX_BOTTOM_NAV_COUNT;

/**
 * Created by JJSOFT-DESKTOP on 2017-05-21.
 */

public class MainActivityPresenter implements MainActivityView.UserInteractions {
    private final MainActivityView mMainActivityView;
    private final Context mContext;

    public MainActivityPresenter(Context context, MainActivityView mainActivityView) {
        this.mContext = context;
        this.mMainActivityView = mainActivityView;
    }

    // 없앨거임 -- 테스트용
    private ArrayList<RecyclerViewData> getRecyclerViewData(int categoryId) {
        ArrayList<RecyclerViewData> recyclerViewDataArrayList = new ArrayList<>();
        RecyclerViewData recyclerViewData = new RecyclerViewData();
        recyclerViewData.setImage_resources(R.drawable.insideout);
        recyclerViewData.setTitle("This is insideout Title" + String.valueOf(categoryId));
        recyclerViewData.setContent("This is insideout Content" + String.valueOf(categoryId));
        recyclerViewDataArrayList.add(recyclerViewData);

        recyclerViewData = new RecyclerViewData();
        recyclerViewData.setImage_resources(R.drawable.toystory);
        recyclerViewData.setTitle("This is toystory Title");
        recyclerViewData.setContent("This is toystory Content");
        recyclerViewDataArrayList.add(recyclerViewData);

        recyclerViewData = new RecyclerViewData();
        recyclerViewData.setImage_resources(R.drawable.insideout);
        recyclerViewData.setTitle("This is insideout Title");
        recyclerViewData.setContent("This is insideout Content");
        recyclerViewDataArrayList.add(recyclerViewData);

        recyclerViewData = new RecyclerViewData();
        recyclerViewData.setImage_resources(R.drawable.toystory);
        recyclerViewData.setTitle("This is toystory Title");
        recyclerViewData.setContent("This is toystory Content");
        recyclerViewDataArrayList.add(recyclerViewData);

        recyclerViewData = new RecyclerViewData();
        recyclerViewData.setImage_resources(R.drawable.insideout);
        recyclerViewData.setTitle("This is insideout Title");
        recyclerViewData.setContent("This is insideout Content");
        recyclerViewDataArrayList.add(recyclerViewData);

        recyclerViewData = new RecyclerViewData();
        recyclerViewData.setImage_resources(R.drawable.toystory);
        recyclerViewData.setTitle("This is toystory Title");
        recyclerViewData.setContent("This is toystory Content");
        recyclerViewDataArrayList.add(recyclerViewData);

        recyclerViewData = new RecyclerViewData();
        recyclerViewData.setImage_resources(R.drawable.insideout);
        recyclerViewData.setTitle("This is insideout Title");
        recyclerViewData.setContent("This is insideout Content");
        recyclerViewDataArrayList.add(recyclerViewData);

        recyclerViewData = new RecyclerViewData();
        recyclerViewData.setImage_resources(R.drawable.toystory);
        recyclerViewData.setTitle("This is toystory Title");
        recyclerViewData.setContent("This is toystory Content");
        recyclerViewDataArrayList.add(recyclerViewData);

        recyclerViewData = new RecyclerViewData();
        recyclerViewData.setImage_resources(R.drawable.insideout);
        recyclerViewData.setTitle("This is insideout Title");
        recyclerViewData.setContent("This is insideout Content");
        recyclerViewDataArrayList.add(recyclerViewData);

        recyclerViewData = new RecyclerViewData();
        recyclerViewData.setImage_resources(R.drawable.toystory);
        recyclerViewData.setTitle("This is toystory Title");
        recyclerViewData.setContent("This is toystory Content");
        recyclerViewDataArrayList.add(recyclerViewData);

        recyclerViewData = new RecyclerViewData();
        recyclerViewData.setImage_resources(R.drawable.insideout);
        recyclerViewData.setTitle("This is insideout Title");
        recyclerViewData.setContent("This is insideout Content");
        recyclerViewDataArrayList.add(recyclerViewData);

        recyclerViewData = new RecyclerViewData();
        recyclerViewData.setImage_resources(R.drawable.toystory);
        recyclerViewData.setTitle("This is toystory Title");
        recyclerViewData.setContent("This is toystory Content");
        recyclerViewDataArrayList.add(recyclerViewData);

        recyclerViewData = new RecyclerViewData();
        recyclerViewData.setImage_resources(R.drawable.insideout);
        recyclerViewData.setTitle("This is insideout Title");
        recyclerViewData.setContent("This is insideout Content");
        recyclerViewDataArrayList.add(recyclerViewData);

        recyclerViewData = new RecyclerViewData();
        recyclerViewData.setImage_resources(R.drawable.toystory);
        recyclerViewData.setTitle("This is toystory Title");
        recyclerViewData.setContent("This is toystory Content");
        recyclerViewDataArrayList.add(recyclerViewData);

        recyclerViewData = new RecyclerViewData();
        recyclerViewData.setImage_resources(R.drawable.insideout);
        recyclerViewData.setTitle("This is insideout Title");
        recyclerViewData.setContent("This is insideout Content");
        recyclerViewDataArrayList.add(recyclerViewData);

        recyclerViewData = new RecyclerViewData();
        recyclerViewData.setImage_resources(R.drawable.toystory);
        recyclerViewData.setTitle("This is toystory Title");
        recyclerViewData.setContent("This is toystory Content");
        recyclerViewDataArrayList.add(recyclerViewData);

        recyclerViewData = new RecyclerViewData();
        recyclerViewData.setImage_resources(R.drawable.insideout);
        recyclerViewData.setTitle("This is insideout Title");
        recyclerViewData.setContent("This is insideout Content");
        recyclerViewDataArrayList.add(recyclerViewData);

        recyclerViewData = new RecyclerViewData();
        recyclerViewData.setImage_resources(R.drawable.toystory);
        recyclerViewData.setTitle("This is toystory Title");
        recyclerViewData.setContent("This is toystory Content");
        recyclerViewDataArrayList.add(recyclerViewData);

        recyclerViewData = new RecyclerViewData();
        recyclerViewData.setImage_resources(R.drawable.insideout);
        recyclerViewData.setTitle("This is insideout Title");
        recyclerViewData.setContent("This is insideout Content");
        recyclerViewDataArrayList.add(recyclerViewData);

        recyclerViewData = new RecyclerViewData();
        recyclerViewData.setImage_resources(R.drawable.toystory);
        recyclerViewData.setTitle("This is toystory Title");
        recyclerViewData.setContent("This is toystory Content");
        recyclerViewDataArrayList.add(recyclerViewData);

        recyclerViewData = new RecyclerViewData();
        recyclerViewData.setImage_resources(R.drawable.insideout);
        recyclerViewData.setTitle("This is insideout Title");
        recyclerViewData.setContent("This is insideout Content");
        recyclerViewDataArrayList.add(recyclerViewData);

        recyclerViewData = new RecyclerViewData();
        recyclerViewData.setImage_resources(R.drawable.toystory);
        recyclerViewData.setTitle("This is toystory Title");
        recyclerViewData.setContent("This is toystory Content");
        recyclerViewDataArrayList.add(recyclerViewData);

        recyclerViewData = new RecyclerViewData();
        recyclerViewData.setImage_resources(R.drawable.insideout);
        recyclerViewData.setTitle("This is insideout Title");
        recyclerViewData.setContent("This is insideout Content");
        recyclerViewDataArrayList.add(recyclerViewData);

        recyclerViewData = new RecyclerViewData();
        recyclerViewData.setImage_resources(R.drawable.toystory);
        recyclerViewData.setTitle("This is toystory Title");
        recyclerViewData.setContent("This is toystory Content");
        recyclerViewDataArrayList.add(recyclerViewData);

        recyclerViewData = new RecyclerViewData();
        recyclerViewData.setImage_resources(R.drawable.insideout);
        recyclerViewData.setTitle("This is insideout Title");
        recyclerViewData.setContent("This is insideout Content");
        recyclerViewDataArrayList.add(recyclerViewData);

        recyclerViewData = new RecyclerViewData();
        recyclerViewData.setImage_resources(R.drawable.toystory);
        recyclerViewData.setTitle("This is toystory Title");
        recyclerViewData.setContent("This is toystory Content");
        recyclerViewDataArrayList.add(recyclerViewData);

        recyclerViewData = new RecyclerViewData();
        recyclerViewData.setImage_resources(R.drawable.insideout);
        recyclerViewData.setTitle("This is insideout Title");
        recyclerViewData.setContent("This is insideout Content");
        recyclerViewDataArrayList.add(recyclerViewData);

        recyclerViewData = new RecyclerViewData();
        recyclerViewData.setImage_resources(R.drawable.toystory);
        recyclerViewData.setTitle("This is toystory Title");
        recyclerViewData.setContent("This is toystory Content");
        recyclerViewDataArrayList.add(recyclerViewData);

        recyclerViewData = new RecyclerViewData();
        recyclerViewData.setImage_resources(R.drawable.insideout);
        recyclerViewData.setTitle("This is insideout Title");
        recyclerViewData.setContent("This is insideout Content");
        recyclerViewDataArrayList.add(recyclerViewData);

        recyclerViewData = new RecyclerViewData();
        recyclerViewData.setImage_resources(R.drawable.toystory);
        recyclerViewData.setTitle("This is toystory Title");
        recyclerViewData.setContent("This is toystory Content");
        recyclerViewDataArrayList.add(recyclerViewData);

        recyclerViewData = new RecyclerViewData();
        recyclerViewData.setImage_resources(R.drawable.insideout);
        recyclerViewData.setTitle("This is insideout Title");
        recyclerViewData.setContent("This is insideout Content");
        recyclerViewDataArrayList.add(recyclerViewData);

        recyclerViewData = new RecyclerViewData();
        recyclerViewData.setImage_resources(R.drawable.toystory);
        recyclerViewData.setTitle("This is toystory Title");
        recyclerViewData.setContent("This is toystory Content");
        recyclerViewDataArrayList.add(recyclerViewData);

        return recyclerViewDataArrayList;
    }

    private ArrayList<MenuData> getMenuItems() {
        ArrayList<MenuData> menuDataArrayList = new ArrayList<>();
        for (int i = 1; i < MAX_BOTTOM_NAV_COUNT; i++) {
            MenuData menuData = new MenuData(0, 0);
            menuData.setItemId(i);
            menuData.setTitle(mContext.getString(R.string.app_name) + String.valueOf(i));
            menuData.setResourceIcon(R.drawable.ic_search_white_24dp);
            menuDataArrayList.add(menuData);
        }

        return menuDataArrayList;
    }

    private ArrayList<RecyclerViewData> getCategoryData(int categoryId) {
        return getRecyclerViewData(categoryId);
    }

    @Override
    public void changeCategory(int categoryId) {
        //2017.05.21
        //이미 refreshDisplay 를 통해서 즐겨찾기가 구성되어있는 상태이므로 메뉴를 재구성 하지 못하도록하자
        //(선택되어있던 카테고리가 즐겨찾기에서 제거됬었을경우, 메인화면을 일일히 refresh 하지 못하기 때문에 MainActivity 에 새로 접근할때만 메뉴 재구성하는것으로,
        //카테고리 즐겨찾기는 꼭 다른 창에서 고를 수 있도록)
        //카테고리 즐겨찾기는 회원가입때 고를 수 있게하거나, 개인정보 수정창에서 고를 수 있도록하자!
        //mMainActivityView.setBottomMenuItems(getMenuItems());
        mMainActivityView.setDisplayRecyclerView(getCategoryData(categoryId));
    }

    @Override
    public void refreshDisplay() {
        mMainActivityView.setBottomMenuItems(getMenuItems());
        mMainActivityView.setDisplayRecyclerView(getCategoryData(HOME_VALUE));
    }

    @Override
    public ArrayList<NavigationMenuData> getExpandableMenuData() {
        ArrayList<NavigationMenuData> groupMenuDataArrayList = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            NavigationMenuData groupMenuData = new NavigationMenuData();
            groupMenuData.setType(ExpandableMenuEnum.GROUP.getTypeValue());
            groupMenuData.setTitle("Group Test" + String.valueOf(i));
            groupMenuData.setImageResource(R.drawable.ic_contracted_24dp);
            //groupMenuData.setImageResource(null);

            ArrayList<NavigationMenuData> childMenuDataArrayList = new ArrayList<>();
            for (int j = 0; j < 5; j++) {
                NavigationMenuData childMenuData = new NavigationMenuData();
                childMenuData.setType(ExpandableMenuEnum.CHILD.getTypeValue());
                childMenuData.setTitle("Child Test" + String.valueOf(j));
                //childMenuData.setImageResource(R.drawable.ic_menu_share);
                childMenuData.setImageResource(null);
                childMenuDataArrayList.add(childMenuData);
            }
            groupMenuData.setInvisibleChildren(childMenuDataArrayList);
            groupMenuDataArrayList.add(groupMenuData);
        }

        return groupMenuDataArrayList;
    }
}
