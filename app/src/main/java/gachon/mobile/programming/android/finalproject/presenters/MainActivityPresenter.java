package gachon.mobile.programming.android.finalproject.presenters;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.TypedArray;

import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.Locale;

import gachon.mobile.programming.android.finalproject.R;
import gachon.mobile.programming.android.finalproject.enums.CategoryMenuEnum;
import gachon.mobile.programming.android.finalproject.enums.ExpandableMenuEnum;
import gachon.mobile.programming.android.finalproject.models.FavoritesCategoryCodeData;
import gachon.mobile.programming.android.finalproject.models.FavoritesCategoryData;
import gachon.mobile.programming.android.finalproject.models.MenuData;
import gachon.mobile.programming.android.finalproject.models.NavigationMenuData;
import gachon.mobile.programming.android.finalproject.models.OnOffMixData;
import gachon.mobile.programming.android.finalproject.models.OnOffMixEventListData;
import gachon.mobile.programming.android.finalproject.models.RecyclerViewData;
import gachon.mobile.programming.android.finalproject.utils.ExceptionHelper;
import gachon.mobile.programming.android.finalproject.utils.RetrofitInterface;
import gachon.mobile.programming.android.finalproject.views.MainActivityView;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.RequestBody;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

import static gachon.mobile.programming.android.finalproject.utils.ApplicationClass.EXPANDABLE_MENU_COUNT;
import static gachon.mobile.programming.android.finalproject.utils.ApplicationClass.MEDIA_TYPE_JSON;
import static gachon.mobile.programming.android.finalproject.utils.ApplicationClass.PREF_ID;
import static gachon.mobile.programming.android.finalproject.utils.ApplicationClass.RETROFIT_INTERFACE;

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

    private Integer getCategoryIcon(String categoryCode) {
        if (categoryCode.equals(CategoryMenuEnum.ANDROID.getValue())) {
            return R.drawable.ic_android_24dp;
        } else if (categoryCode.equals(CategoryMenuEnum.JAVA.getValue())) {
            return R.drawable.ic_java_24dp;
        } else if (categoryCode.equals(CategoryMenuEnum.PYTHON.getValue())) {
            return R.drawable.ic_python_24dp;
        } else if (categoryCode.equals(CategoryMenuEnum.PHP.getValue())) {
            return R.drawable.ic_elephant_24dp;
        } else if (categoryCode.equals(CategoryMenuEnum.JAVASCRIPT.getValue())) {
            return R.drawable.ic_javascript_24dp;
        } else if (categoryCode.equals(CategoryMenuEnum.ETC.getValue())) {
            return R.drawable.ic_more_horiz_black_24dp;
        } else {
            return null;
        }
    }

    private ArrayList<NavigationMenuData> getExpandableMenuData(ArrayList<MenuData> menuDataArrayList) {
        TypedArray category_icon = mContext.getResources().obtainTypedArray(R.array.category_default_image);

        ArrayList<NavigationMenuData> groupMenuDataArrayList = new ArrayList<>();
        for (int i = 0; i < EXPANDABLE_MENU_COUNT; i++) {
            final NavigationMenuData groupMenuData = new NavigationMenuData();
            groupMenuData.setType(ExpandableMenuEnum.GROUP.getTypeValue());
            final String categoryCode = String.format(Locale.KOREAN, "%03d", i + 1);
            final String groupMenuTitle = CategoryMenuEnum.findNameByValue(categoryCode, mContext);
            groupMenuData.setTitle(groupMenuTitle);
            groupMenuData.setImageResource(R.drawable.ic_contracted_24dp);
            for (MenuData menuData : menuDataArrayList)
                if (menuData.getTitle().equals(groupMenuTitle)) {
                    groupMenuData.setFavorite(true);
                }

            final ArrayList<NavigationMenuData> childMenuDataArrayList = new ArrayList<>();
            String[] category_name;
            if (CategoryMenuEnum.ETC.getValue().equals(categoryCode)) {
                category_name = mContext.getResources().getStringArray(R.array.category_etc_title);
                category_icon = mContext.getResources().obtainTypedArray(R.array.category_etc_image);
            } else {
                category_name = mContext.getResources().getStringArray(R.array.category_default_title);
            }

            for (int j = 0; j < category_name.length; j++) {
                NavigationMenuData childMenuData = new NavigationMenuData();
                childMenuData.setType(i + 1);
                childMenuData.setTitle(category_name[j]);
                childMenuData.setImageResource(category_icon.getResourceId(j, -1));
                /*if (j > category_icon.length() - 1) {
                    childMenuData.setImageResource(R.drawable.ic_android_24dp);
                } else {
                    childMenuData.setImageResource(category_icon.getResourceId(j , R.drawable.ic_android_24dp));
                }*/
                childMenuDataArrayList.add(childMenuData);
            }
            groupMenuData.setInvisibleChildren(childMenuDataArrayList);
            groupMenuDataArrayList.add(groupMenuData);
        }

        /*final NavigationMenuData logOutMenuData = new NavigationMenuData();
        logOutMenuData.setType(ExpandableMenuEnum.CHILD.getTypeValue());
        logOutMenuData.setTitle("로그아웃");
        logOutMenuData.setImageResource(null);
        groupMenuDataArrayList.add(logOutMenuData);*/

        category_icon.recycle();
        return groupMenuDataArrayList;
    }

    private ArrayList<MenuData> getMenuData(ArrayList<FavoritesCategoryCodeData> categoryNameArrayList) {
        ArrayList<MenuData> menuDataArrayList = new ArrayList<>();

        for (int i = 0; i < categoryNameArrayList.size(); i++) {
            if (categoryNameArrayList.get(i).getCategoryCode().equals("")) {
                continue;
            }
            MenuData menuData = new MenuData(0, 0);
            menuData.setItemId(i + 1);

            String categoryCode = categoryNameArrayList.get(i).getCategoryCode();
            if (categoryCode.equals("")) {
                continue;
            }

            String categoryTitle = CategoryMenuEnum.findNameByValue(categoryCode, mContext);
            if (categoryTitle == null) {
                continue;
            }

            menuData.setTitle(categoryTitle);
            menuData.setResourceIcon(getCategoryIcon(categoryCode));
            menuDataArrayList.add(menuData);
        }

        return menuDataArrayList;
    }

    private void setOnOffMixData(final String baseUrl) {
        ArrayList<RecyclerViewData> recyclerViewDataArrayList = new ArrayList<>();
        ProgressDialog subscribeProgressDialog = new ProgressDialog(mContext);

        final Retrofit RETROFIT_BUILDER = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        final RetrofitInterface RETROFIT_INTERFACE = RETROFIT_BUILDER.create(RetrofitInterface.class);


        Observable<OnOffMixData> onOffMixRx = RETROFIT_INTERFACE.OnOffMixRx("api.onoffmix.com/event/list", "json", 12,
                "if(recruitEndDateTime-NOW()>0# 1# 0)|DESC,FIND_IN_SET('advance'#wayOfRegistration)|DESC,popularity|DESC,idx|DESC", 1, "", "", "", "true", "true", "true", "개발", "", "", 1);
        onOffMixRx.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<OnOffMixData>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        mMainActivityView.showProgressDialog(subscribeProgressDialog);
                    }

                    @Override
                    public void onNext(@NonNull OnOffMixData onOffMixData) {
                        if (onOffMixData.getError().getCode() == 0) {
                            for (OnOffMixEventListData eventListData : onOffMixData.getEventList()) {
                                RecyclerViewData recyclerViewData = new RecyclerViewData();
                                recyclerViewData.setTitle(eventListData.getTitle());
                                recyclerViewData.setContent(eventListData.getTotalCanAttend() + mContext.getString(R.string.onOffMix_attend));
                                recyclerViewData.setImageUrl(eventListData.getBannerUrl());
                                recyclerViewData.setContentUrl(eventListData.getEventUrl());

                                recyclerViewDataArrayList.add(recyclerViewData);
                            }
                        } else {
                            mMainActivityView.dismissProgressDialog(subscribeProgressDialog);
                            mMainActivityView.showCustomToast(onOffMixData.getError().getMessage());
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

    private void setOKKYData(final String baseUrl) {
        ArrayList<RecyclerViewData> recyclerViewDataArrayList = new ArrayList<>();
        ProgressDialog subscribeProgressDialog = new ProgressDialog(mContext);

        Observable.fromCallable(() -> {
            Document document = Jsoup.connect(baseUrl).get();
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
                            recyclerViewData.setContent(element.select("div.list-title-wrapper.clearfix a.list-group-item-text item-tag label label-info").text());
                            recyclerViewData.setImageUrl("http:" + element.select("div.list-group-item-author.clearfix a.avatar-photo img").attr("src"));
                            recyclerViewData.setContentUrl("https://okky.kr/articles/" + element.select("div.list-title-wrapper.clearfix span.list-group-item-text article-id").text().replace("#", ""));
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

    private void setLeftNavigationMenuItems() {
        SharedPreferences sharedPreferences = mContext.getSharedPreferences(PREF_ID, Activity.MODE_PRIVATE);
        String email = sharedPreferences.getString("email", null);
        String password = sharedPreferences.getString("password", null);

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("email", email);
            jsonObject.put("password", password);
        } catch (JSONException e) {
            mMainActivityView.showCustomToast(e.getMessage());
            return;
        }

        Observable<FavoritesCategoryData> callCategoryRx = RETROFIT_INTERFACE.CallCategoryRx(RequestBody.create(MEDIA_TYPE_JSON, jsonObject.toString()));
        callCategoryRx.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(t -> {
                            if (t.isSuccess()) {
                                ArrayList<FavoritesCategoryCodeData> categoryNameArrayList = t.getData();
                                ArrayList<MenuData> menuDataArrayList = getMenuData(categoryNameArrayList);
                                ArrayList<NavigationMenuData> groupMenuDataArrayList = getExpandableMenuData(menuDataArrayList);

                                mMainActivityView.setBottomMenuItems(menuDataArrayList);
                                mMainActivityView.setExpandableMenuItems(groupMenuDataArrayList);
                            } else {
                                mMainActivityView.showCustomToast(t.getData().get(0).getCategoryCode());
                            }
                        },
                        e -> mMainActivityView.showCustomToast(ExceptionHelper.getApplicationExceptionMessage((Exception) e)));
    }

    @Override
    public void changeCategory(int categoryId) {
        //2017.05.21
        //이미 refreshDisplay 를 통해서 즐겨찾기가 구성되어있는 상태이므로 메뉴를 재구성 하지 못하도록하자
        //(선택되어있던 카테고리가 즐겨찾기에서 제거됬었을경우, 메인화면을 일일히 refresh 하지 못하기 때문에 MainActivity 에 새로 접근할때만 메뉴 재구성하는것으로,
        //카테고리 즐겨찾기는 꼭 다른 창에서 고를 수 있도록)
        //카테고리 즐겨찾기는 회원가입때 고를 수 있게하거나, 개인정보 수정창에서 고를 수 있도록하자!
        //mMainActivityView.setBottomMenuItems(getMenuItems());
        //2017.06.05
        //인텐트 FLAG -> CLEAR_TOP 을 써서 해결

        switch (categoryId) {
            case 1:
                //setOnOffMixData("http://onoffmix.com/");
                break;
            case 2:
                //setOKKYData("https://okky.kr/articles/tech");
                break;
        }
    }

    @Override
    public void refreshDisplay() {
        //mMainActivityView.setDisplayRecyclerView(getCategoryData(HOME_VALUE));
        ArrayList<RecyclerViewData> recyclerViewDataArrayList = new ArrayList<>();
        ProgressDialog subscribeProgressDialog = new ProgressDialog(mContext);

        setLeftNavigationMenuItems();
        //setOnOffMixData("http://onoffmix.com/");

        /*Observable.merge(Observable.fromCallable(() -> {
            Document document = Jsoup.connect("https://stackoverflow.com/questions/tagged/android").get();
            return document.select("div#questions.content-padding div.question-summary");
        }), Observable.fromCallable(() -> {
            Document document = Jsoup.connect("https://okky.kr/articles/tech?query=android&sort=id&order=desc").get();
            return document.select("ul.list-group li.list-group-item");
        }), Observable.fromCallable(() -> {
            Document document = Jsoup.connect("https://okky.kr/articles/questions?query=android&sort=id&order=desc").get();
            return document.select("ul.list-group li.list-group-item");
        }))*/
        /*Observable.merge(Observable.fromCallable(() -> {
            Document document = Jsoup.connect("https://okky.kr/articles/tech?query=android&sort=id&order=desc").get();
            return document.select("ul.list-group li.list-group-item");
        }), Observable.fromCallable(() -> {
            Document document = Jsoup.connect("https://okky.kr/articles/questions?query=android&sort=id&order=desc").get();
            return document.select("ul.list-group li.list-group-item");
        })).subscribeOn(Schedulers.io())
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
                            recyclerViewData.setTitle(element.select("div.summary h3 a.question-hyperlink").text());
                            recyclerViewData.setContent(element.select("div.summary div.excerpt").text());
                            recyclerViewData.setImageUrl(element.select("div div.user-info div.user-gravatar32 a div.gravatar-wrapper-32 img").attr("src"));
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
                });*/

        /*final Retrofit RETROFIT_BUILDER = new Retrofit.Builder()
                .baseUrl("http://onoffmix.com/")
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        final RetrofitInterface RETROFIT_INTERFACE = RETROFIT_BUILDER.create(RetrofitInterface.class);

        Observable<OnOffMixData> onOffMixRx = RETROFIT_INTERFACE.OnOffMixRx("api.onoffmix.com/event/list", "json", 12,
                "if(recruitEndDateTime-NOW()>0# 1# 0)|DESC,FIND_IN_SET('advance'#wayOfRegistration)|DESC,popularity|DESC,idx|DESC", 1, "", "", "", "true", "true", "true", "개발", "", "", 1);
        onOffMixRx.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<OnOffMixData>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        mMainActivityView.showProgressDialog(subscribeProgressDialog);
                    }

                    @Override
                    public void onNext(@NonNull OnOffMixData onOffMixData) {
                        if (onOffMixData.getError().getCode() == 0) {
                            for (OnOffMixEventListData eventListData : onOffMixData.getEventList()) {
                                RecyclerViewData recyclerViewData = new RecyclerViewData();
                                recyclerViewData.setTitle(eventListData.getTitle());
                                recyclerViewData.setContent(eventListData.getTotalCanAttend() + mContext.getString(R.string.onOffMix_attend));
                                recyclerViewData.setImageUrl(eventListData.getBannerUrl());

                                recyclerViewDataArrayList.add(recyclerViewData);
                            }
                        } else {
                            mMainActivityView.dismissProgressDialog(subscribeProgressDialog);
                            mMainActivityView.showCustomToast(onOffMixData.getError().getMessage());
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
                            recyclerViewData.setImageUrl("http:" + element.select("div.list-group-item-author.clearfix a.avatar-photo img").attr("src"));
                            recyclerViewDataArrayList.add(recyclerViewData);
                        }
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        mMainActivityView.dismissProgressDialog(subscribeProgressDialog);
                    }

                    @Override
                    public void onComplete() {
                        //mMainActivityView.setDisplayRecyclerView(recyclerViewDataArrayList);
                        mMainActivityView.dismissProgressDialog(subscribeProgressDialog);
                    }
                });*/

        //TODO Observable Merge 작업해보기.
        // http://www.introtorx.com/content/v1.0.10621.0/12_CombiningSequences.html
        //mMainActivityView.setDisplayRecyclerView(recyclerViewDataArrayList);
    }
}
