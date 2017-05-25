package gachon.mobile.programming.android.finalproject.presenters;

import android.app.ProgressDialog;
import android.content.Context;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;

import gachon.mobile.programming.android.finalproject.R;
import gachon.mobile.programming.android.finalproject.enums.ExpandableMenuEnum;
import gachon.mobile.programming.android.finalproject.models.MenuData;
import gachon.mobile.programming.android.finalproject.models.NavigationMenuData;
import gachon.mobile.programming.android.finalproject.models.RecyclerViewData;
import gachon.mobile.programming.android.finalproject.views.MainActivityView;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

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

    /*private ArrayList<RecyclerViewData> getCategoryData(int categoryId) {

        *//*try {

            for (Element element : elements) {
                String a = element.addClass(".list-group-item-heading").text();
                testList.add(a);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }*//*


        //return getRecyclerViewData(categoryId);
        return recyclerViewDataArrayList;
    }*/

    @Override
    public void changeCategory(int categoryId) {
        //2017.05.21
        //이미 refreshDisplay 를 통해서 즐겨찾기가 구성되어있는 상태이므로 메뉴를 재구성 하지 못하도록하자
        //(선택되어있던 카테고리가 즐겨찾기에서 제거됬었을경우, 메인화면을 일일히 refresh 하지 못하기 때문에 MainActivity 에 새로 접근할때만 메뉴 재구성하는것으로,
        //카테고리 즐겨찾기는 꼭 다른 창에서 고를 수 있도록)
        //카테고리 즐겨찾기는 회원가입때 고를 수 있게하거나, 개인정보 수정창에서 고를 수 있도록하자!
        //mMainActivityView.setBottomMenuItems(getMenuItems());

        //mMainActivityView.setDisplayRecyclerView(getCategoryData(categoryId));

        ArrayList<RecyclerViewData> recyclerViewDataArrayList = new ArrayList<>();
        ProgressDialog subscribeProgressDialog = new ProgressDialog(mContext);

        Observable.fromCallable(() -> {
            Document document = Jsoup.connect("https://okky.kr/articles/tech").get();
            return document.select("ul.list-group li.list-group-item");
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Elements>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        mMainActivityView.showProgressDialog(subscribeProgressDialog);
                    }

                    @Override
                    public void onNext(@NonNull Elements elements) {
                        for (Element element : elements) {
                            RecyclerViewData recyclerViewData = new RecyclerViewData();
                            recyclerViewData.setTitle(element.select("div.list-title-wrapper.clearfix h5.list-group-item-heading a").text());
                            recyclerViewData.setContent(element.select("div.list-group-item-author.clearfix a.nickname").text());
                            recyclerViewData.setImageUrl(element.select("div.list-group-item-author.clearfix a.avatar-photo img").attr("src"));
                            recyclerViewDataArrayList.add(recyclerViewData);
                        }
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        mMainActivityView.dismissProgressDialog(subscribeProgressDialog);
                    }

                    @Override
                    public void onComplete() {
                        mMainActivityView.setDisplayRecyclerView(recyclerViewDataArrayList);
                        mMainActivityView.dismissProgressDialog(subscribeProgressDialog);
                    }
                });

    }

    @Override
    public void refreshDisplay() {
        mMainActivityView.setBottomMenuItems(getMenuItems());

        //mMainActivityView.setDisplayRecyclerView(getCategoryData(HOME_VALUE));
        ArrayList<RecyclerViewData> recyclerViewDataArrayList = new ArrayList<>();
        ProgressDialog subscribeProgressDialog = new ProgressDialog(mContext);

        Observable.fromCallable(() -> {
            Document document = Jsoup.connect("https://okky.kr/articles/tech").get();
            return document.select("ul.list-group li.list-group-item");
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Elements>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        mMainActivityView.showProgressDialog(subscribeProgressDialog);
                    }

                    @Override
                    public void onNext(@NonNull Elements elements) {
                        for (Element element : elements) {
                            RecyclerViewData recyclerViewData = new RecyclerViewData();
                            recyclerViewData.setTitle(element.select("div.list-title-wrapper.clearfix h5.list-group-item-heading a").text());
                            recyclerViewData.setContent(element.select("div.list-group-item-author.clearfix a.nickname").text());
                            recyclerViewData.setImageUrl(element.select("div.list-group-item-author.clearfix a.avatar-photo img").attr("src"));
                            recyclerViewDataArrayList.add(recyclerViewData);
                        }
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        mMainActivityView.dismissProgressDialog(subscribeProgressDialog);
                    }

                    @Override
                    public void onComplete() {
                        mMainActivityView.setDisplayRecyclerView(recyclerViewDataArrayList);
                        mMainActivityView.dismissProgressDialog(subscribeProgressDialog);
                    }
                });
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
                childMenuData.setImageResource(R.drawable.ic_menu_share);
                //childMenuData.setImageResource(null);
                childMenuDataArrayList.add(childMenuData);
            }
            groupMenuData.setInvisibleChildren(childMenuDataArrayList);
            groupMenuDataArrayList.add(groupMenuData);
        }

        return groupMenuDataArrayList;
    }
}
