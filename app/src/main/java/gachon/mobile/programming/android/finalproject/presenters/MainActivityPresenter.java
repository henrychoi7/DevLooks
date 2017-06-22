package gachon.mobile.programming.android.finalproject.presenters;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.TypedArray;
import android.view.MenuItem;

import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.text.Collator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Locale;

import gachon.mobile.programming.android.finalproject.R;
import gachon.mobile.programming.android.finalproject.enums.CategoryMenuEnum;
import gachon.mobile.programming.android.finalproject.enums.ExpandableMenuEnum;
import gachon.mobile.programming.android.finalproject.models.FavoritesCategoryCodeData;
import gachon.mobile.programming.android.finalproject.models.FavoritesCategoryData;
import gachon.mobile.programming.android.finalproject.models.FavoritesContentData;
import gachon.mobile.programming.android.finalproject.models.FavoritesContentDetailData;
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
import static gachon.mobile.programming.android.finalproject.utils.ApplicationClass.OKKY;
import static gachon.mobile.programming.android.finalproject.utils.ApplicationClass.ON_OFF_MIX;
import static gachon.mobile.programming.android.finalproject.utils.ApplicationClass.PREF_ID;
import static gachon.mobile.programming.android.finalproject.utils.ApplicationClass.RETROFIT_INTERFACE;
import static gachon.mobile.programming.android.finalproject.utils.ApplicationClass.STACK_OVERFLOW;
import static gachon.mobile.programming.android.finalproject.utils.ApplicationClass.STACK_OVERFLOW_MAIN;
import static gachon.mobile.programming.android.finalproject.utils.ApplicationClass.getBitmapFromVectorDrawable;

public class MainActivityPresenter implements MainActivityView.UserInteractions {
    private final MainActivityView mMainActivityView;
    private final Context mContext;

    private final Comparator mComparator = new Comparator() {
        private final Collator collator = Collator.getInstance();

        @Override
        public int compare(Object objectFront, Object objectBack) {
            final RecyclerViewData recyclerViewDataFront = (RecyclerViewData) objectFront;
            final RecyclerViewData recyclerViewDataBack = (RecyclerViewData) objectBack;
            return collator.compare(String.valueOf(recyclerViewDataBack.getFavoritesCount().length()), String.valueOf(recyclerViewDataFront.getFavoritesCount().length()));
        }
    };

    public MainActivityPresenter(final Context context, final MainActivityView mainActivityView) {
        this.mContext = context;
        this.mMainActivityView = mainActivityView;
    }

    private Integer getCategoryIcon(final String categoryCode) {
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

    private ArrayList<NavigationMenuData> getExpandableMenuData(final ArrayList<MenuData> menuDataArrayList) {
        TypedArray category_icon = mContext.getResources().obtainTypedArray(R.array.category_default_image);

        final ArrayList<NavigationMenuData> groupMenuDataArrayList = new ArrayList<>();
        for (int i = 0; i < EXPANDABLE_MENU_COUNT; i++) {
            final NavigationMenuData groupMenuData = new NavigationMenuData();
            groupMenuData.setType(ExpandableMenuEnum.GROUP.getTypeValue());
            final String categoryCode = String.format(Locale.KOREAN, "%03d", i + 1);
            final String groupMenuTitle = CategoryMenuEnum.findNameByValue(categoryCode, mContext);
            groupMenuData.setTitle(groupMenuTitle);
            groupMenuData.setImageResource(R.drawable.ic_contracted_24dp);
            for (final MenuData menuData : menuDataArrayList)
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
                final NavigationMenuData childMenuData = new NavigationMenuData();
                childMenuData.setType(i + 1);
                childMenuData.setTitle(category_name[j]);
                childMenuData.setImageResource(category_icon.getResourceId(j, -1));
                childMenuDataArrayList.add(childMenuData);
            }
            groupMenuData.setInvisibleChildren(childMenuDataArrayList);
            groupMenuDataArrayList.add(groupMenuData);
        }

        category_icon.recycle();
        return groupMenuDataArrayList;
    }

    private ArrayList<MenuData> getMenuData(final ArrayList<FavoritesCategoryCodeData> categoryNameArrayList) {
        final ArrayList<MenuData> menuDataArrayList = new ArrayList<>();

        for (int i = 0; i < categoryNameArrayList.size(); i++) {
            if (categoryNameArrayList.get(i).getCategoryCode().equals("")) {
                continue;
            }

            String categoryCode = categoryNameArrayList.get(i).getCategoryCode();
            if (categoryCode.equals("")) {
                continue;
            }

            final String categoryTitle = CategoryMenuEnum.findNameByValue(categoryCode, mContext);
            if (categoryTitle == null) {
                continue;
            }

            final MenuData menuData = new MenuData(0, 0);
            menuData.setItemId(Integer.parseInt(categoryCode));
            menuData.setTitle(categoryTitle);
            menuData.setResourceIcon(getCategoryIcon(categoryCode));
            menuDataArrayList.add(menuData);
        }

        return menuDataArrayList;
    }

    private void setHomeData() {
        final SharedPreferences sharedPreferences = mContext.getSharedPreferences(PREF_ID, Activity.MODE_PRIVATE);
        final String email = sharedPreferences.getString("email", null);
        final String password = sharedPreferences.getString("password", null);

        final JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("email", email);
            jsonObject.put("password", password);
        } catch (final JSONException e) {
            mMainActivityView.showCustomToast(ExceptionHelper.getApplicationExceptionMessage(e));
            return;
        }

        final Observable<FavoritesContentData> callContentRx = RETROFIT_INTERFACE.CallContentRx(RequestBody.create(MEDIA_TYPE_JSON, jsonObject.toString()));
        callContentRx.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<FavoritesContentData>() {
                    final ArrayList<RecyclerViewData> recyclerViewDataArrayList = new ArrayList<>();
                    final ProgressDialog subscribeProgressDialog = new ProgressDialog(mContext);

                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        mMainActivityView.showProgressDialog(subscribeProgressDialog, mContext.getResources().getString(R.string.loadingHomeData));
                    }

                    @Override
                    public void onNext(@NonNull FavoritesContentData favoritesContentData) {
                        final ArrayList<FavoritesContentDetailData> contentDetailDataArrayList = favoritesContentData.getData();
                        for (final FavoritesContentDetailData detailData : contentDetailDataArrayList) {
                            final RecyclerViewData recyclerViewData = new RecyclerViewData();
                            if (detailData.getContentUrl().equals("")) {
                                continue;
                            }
                            recyclerViewData.setTitle(detailData.getContentTitle());
                            recyclerViewData.setTags(detailData.getContentTag());
                            recyclerViewData.setContent(detailData.getContentSummary());
                            recyclerViewData.setWatchCount(detailData.getContentWatchCount());
                            recyclerViewData.setFavoritesCount(detailData.getContentFavoritesCount());
                            recyclerViewData.setImageResources(getBitmapFromVectorDrawable(mContext, R.drawable.ic_favorites_fill_24dp));
                            recyclerViewData.setImageUrl(null);
                            recyclerViewData.setContentUrl(detailData.getContentUrl());
                            recyclerViewData.setType("main");
                            recyclerViewDataArrayList.add(recyclerViewData);
                        }
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        mMainActivityView.dismissProgressDialog(subscribeProgressDialog);
                        mMainActivityView.showCustomToast(ExceptionHelper.getApplicationExceptionMessage((Exception) e));
                    }

                    @Override
                    public void onComplete() {
                        mMainActivityView.dismissProgressDialog(subscribeProgressDialog);
                        mMainActivityView.setDisplayRecyclerView(recyclerViewDataArrayList);
                        setCrawlingData(subscribeProgressDialog);
                    }
                });
    }

    private void setCrawlingData(ProgressDialog subscribeProgressDialog) {
        final ArrayList<RecyclerViewData> recyclerViewDataArrayList = new ArrayList<>();

        final Observable<ArrayList<RecyclerViewData>> onOffMixData = Observable.fromCallable(() -> {
            final Document document = Jsoup.connect("http://onoffmix.com/event?s=개발").get();
            final Elements elements = document.select("div#content.content > div.eventMain > div.sideLeft > div.contentBox.todayEventArea > ul:not(.todayEvent.noneEvent, .todayEvent.alwaysShow)");
            for (final Element element : elements) {
                final RecyclerViewData recyclerViewData = new RecyclerViewData();
                recyclerViewData.setTitle(element.select("li.eventTitle").text());
                recyclerViewData.setTags(null);
                recyclerViewData.setContent(null);
                recyclerViewData.setWatchCount(element.select("li.eventBottomArea ul li.eventPersonnel a span:not(.entered)").text());
                recyclerViewData.setFavoritesCount(element.select("li.eventBottomArea ul li.eventPin span.pinNumber").text());
                recyclerViewData.setImageResources(getBitmapFromVectorDrawable(mContext, R.drawable.ic_onoffmix_24dp));
                recyclerViewData.setImageUrl(element.select("li.eventThumbnail a img").attr("src"));
                recyclerViewData.setContentUrl(element.select("li.eventThumbnail a").attr("href").replace("http://onoffmix", "http://m.onoffmix"));
                recyclerViewData.setType(ON_OFF_MIX);
                recyclerViewDataArrayList.add(recyclerViewData);
            }
            return recyclerViewDataArrayList;
        });

        final Observable<ArrayList<RecyclerViewData>> stackOverflowData = Observable.fromCallable(() -> {
            final Document document = Jsoup.connect("https://stackoverflow.com/?tab=month").get();
            final Elements elements = document.select("div#question-mini-list div.question-summary.narrow");
            for (int i = 0; i <= 15; i++) {
                Element element = elements.get(i);
                RecyclerViewData recyclerViewData = new RecyclerViewData();
                recyclerViewData.setTitle(element.select("div.summary h3").text());
                final Elements tagElements = element.select("div.summary div.tags a");
                String tagString = "";
                for (Element tagElement : tagElements) {
                    tagString += " [" + tagElement.text() + "]";
                }
                recyclerViewData.setTags(tagString);
                recyclerViewData.setContent(element.select("div.summary h3").text());
                recyclerViewData.setSubInfo(element.select("div.summary div.started").text());
                recyclerViewData.setWatchCount(element.select("div.views span").text());
                recyclerViewData.setFavoritesCount(element.select("div.cp div.votes span").text());
                recyclerViewData.setImageResources(getBitmapFromVectorDrawable(mContext, R.drawable.ic_stack_overflow_24dp));
                recyclerViewData.setImageUrl(null);
                recyclerViewData.setContentUrl("https://stackoverflow.com" + element.select("div.summary h3 a").attr("href"));
                recyclerViewData.setType(STACK_OVERFLOW_MAIN);
                recyclerViewDataArrayList.add(recyclerViewData);
            }

            return recyclerViewDataArrayList;
        });

        final Observable<ArrayList<RecyclerViewData>> okkyData = Observable.fromCallable(() -> {
            final Document document = Jsoup.connect("https://okky.kr/articles/tech?query=&sort=scrapCount&order=desc").get();
            final Elements elements = document.select("ul.list-group li.list-group-item");
            for (final Element element : elements) {
                final RecyclerViewData recyclerViewData = new RecyclerViewData();
                recyclerViewData.setTitle(element.select("div.list-title-wrapper.clearfix h5.list-group-item-heading a").text());
                final Elements tagElements = element.select("div.list-title-wrapper.clearfix a.list-group-item-text");
                String tagString = "";
                for (Element tagElement : tagElements) {
                    tagString += " [" + tagElement.text() + "]";
                }
                recyclerViewData.setTags(tagString);
                recyclerViewData.setContent(element.select("div.list-title-wrapper.clearfix h5.list-group-item-heading a").text());
                recyclerViewData.setWatchCount(element.select("div.list-summary-wrapper.clearfix ul li").get(2).text());
                recyclerViewData.setFavoritesCount(element.select("div.list-summary-wrapper.clearfix ul li").get(1).text());
                recyclerViewData.setImageUrl("http:" + element.select("div.list-group-item-author.clearfix a.avatar-photo img").attr("src"));
                recyclerViewData.setContentUrl("https://okky.kr/article/" + element.select("div.list-title-wrapper.clearfix span.list-group-item-text.article-id").text().replace("#", ""));
                recyclerViewData.setType(OKKY);
                recyclerViewDataArrayList.add(recyclerViewData);
            }
            return recyclerViewDataArrayList;
        });

        // merge 참고
        // http://www.introtorx.com/content/v1.0.10621.0/12_CombiningSequences.html
        final Observable<ArrayList<RecyclerViewData>> mergedData = Observable.merge(onOffMixData, stackOverflowData, okkyData);

        mergedData.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ArrayList<RecyclerViewData>>() {
                    ArrayList<RecyclerViewData> finalRecyclerViewData = new ArrayList<>();
                    private final Comparator homeComparator = new Comparator() {
                        private final Collator collator = Collator.getInstance();

                        @Override
                        public int compare(Object objectFront, Object objectBack) {
                            final RecyclerViewData recyclerViewDataFront = (RecyclerViewData) objectFront;
                            final RecyclerViewData recyclerViewDataBack = (RecyclerViewData) objectBack;
                            return collator.compare(String.valueOf(recyclerViewDataFront.getFavoritesCount().length()), String.valueOf(recyclerViewDataBack.getFavoritesCount().length()));
                        }
                    };

                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        mMainActivityView.showProgressDialog(subscribeProgressDialog, mContext.getResources().getString(R.string.loadingHomeData));
                    }

                    @Override
                    public void onNext(@NonNull final ArrayList<RecyclerViewData> recyclerViewTotalData) {
                        finalRecyclerViewData = recyclerViewTotalData;
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        mMainActivityView.dismissProgressDialog(subscribeProgressDialog);
                        mMainActivityView.showCustomToast(e.getMessage());
                    }

                    @Override
                    public void onComplete() {
                        Collections.sort(finalRecyclerViewData, homeComparator);
                        mMainActivityView.addAdditionalData(finalRecyclerViewData);
                        mMainActivityView.dismissProgressDialog(subscribeProgressDialog);
                    }
                });
    }

    private void setCategoryData(final String categoryParam, final int pageCount) {
        final ArrayList<RecyclerViewData> recyclerViewDataArrayList = new ArrayList<>();
        final ProgressDialog subscribeProgressDialog = new ProgressDialog(mContext);

        final String stackOverflowUrl = "https://stackoverflow.com/questions/tagged/" + categoryParam + "?page=" + pageCount + "&sort=frequent&pagesize=15";

        final Observable<ArrayList<RecyclerViewData>> stackOverflowData = Observable.fromCallable(() -> {
            final Document document = Jsoup.connect(stackOverflowUrl).get();
            final Elements elements = document.select("div#questions.content-padding div.question-summary");
            for (final Element element : elements) {
                final RecyclerViewData recyclerViewData = new RecyclerViewData();
                recyclerViewData.setTitle(element.select("div.summary h3 a.question-hyperlink").text());
                final Elements tagElements = element.select("div.summary div.tags a");
                String tagString = "";
                for (Element tagElement : tagElements) {
                    tagString += " [" + tagElement.text() + "]";
                }
                recyclerViewData.setTags(tagString);
                recyclerViewData.setSubInfo(element.select("div.started.fr div.user-info div.user-action-time").text());
                recyclerViewData.setContent(element.select("div.summary div.excerpt").text());
                recyclerViewData.setWatchCount(element.select("div.statscontainer div.views").text());
                recyclerViewData.setFavoritesCount(element.select("div.statscontainer div.stats div.vote div.votes span strong").text());
                recyclerViewData.setImageUrl(element.select("div.started.fr div.user-info div.user-gravatar32 a div.gravatar-wrapper-32 img").attr("src"));
                recyclerViewData.setContentUrl("https://stackoverflow.com" + element.select("div.summary h3 a.question-hyperlink").attr("href"));
                recyclerViewData.setType(STACK_OVERFLOW);

                recyclerViewDataArrayList.add(recyclerViewData);
            }

            return recyclerViewDataArrayList;
        });

        final int okkyOffsetCount = (pageCount - 1) * 20;
        final String okkyTechUrl = "https://okky.kr/articles/tech?offset=" + okkyOffsetCount + "&max=20&sort=voteCount&order=desc&query=" + categoryParam;

        final Observable<ArrayList<RecyclerViewData>> okkyTechData = Observable.fromCallable(() -> {
            final Document document = Jsoup.connect(okkyTechUrl).get();
            final Elements elements = document.select("ul.list-group li.list-group-item");
            for (final Element element : elements) {
                final RecyclerViewData recyclerViewData = new RecyclerViewData();
                recyclerViewData.setTitle(element.select("div.list-title-wrapper.clearfix h5.list-group-item-heading a").text());
                final Elements tagElements = element.select("div.list-title-wrapper.clearfix a.list-group-item-text");
                String tagString = "";
                for (Element tagElement : tagElements) {
                    tagString += " [" + tagElement.text() + "]";
                }
                recyclerViewData.setTags(tagString);
                recyclerViewData.setContent(element.select("div.list-title-wrapper.clearfix h5.list-group-item-heading a").text());
                recyclerViewData.setWatchCount(element.select("div.list-summary-wrapper.clearfix ul li").get(2).text());
                recyclerViewData.setFavoritesCount(element.select("div.list-summary-wrapper.clearfix ul li").get(1).text());
                recyclerViewData.setImageUrl("http:" + element.select("div.list-group-item-author.clearfix a.avatar-photo img").attr("src"));
                recyclerViewData.setContentUrl("https://okky.kr/article/" + element.select("div.list-title-wrapper.clearfix span.list-group-item-text.article-id").text().replace("#", ""));
                recyclerViewData.setType(OKKY);
                recyclerViewDataArrayList.add(recyclerViewData);
            }
            return recyclerViewDataArrayList;
        });

        final String okkyQnAUrl = "https://okky.kr/articles/questions?offset=" + okkyOffsetCount + "&max=20&sort=voteCount&order=desc&query=" + categoryParam;

        final Observable<ArrayList<RecyclerViewData>> okkyQnAData = Observable.fromCallable(() -> {
            final Document document = Jsoup.connect(okkyQnAUrl).get();
            final Elements elements = document.select("ul.list-group li.list-group-item");
            for (final Element element : elements) {
                final RecyclerViewData recyclerViewData = new RecyclerViewData();
                recyclerViewData.setTitle(element.select("div.list-title-wrapper.clearfix h5.list-group-item-heading a").text());
                final Elements tagElements = element.select("div.list-title-wrapper.clearfix a.list-group-item-text");
                String tagString = "";
                for (Element tagElement : tagElements) {
                    tagString += " [" + tagElement.text() + "]";
                }
                recyclerViewData.setTags(tagString);
                recyclerViewData.setContent(element.select("div.list-title-wrapper.clearfix h5.list-group-item-heading a").text());
                recyclerViewData.setWatchCount(element.select("div.list-summary-wrapper.clearfix > div.item-evaluate-wrapper > div.item-evaluate > div.item-evaluate-count").get(1).text());
                recyclerViewData.setFavoritesCount(element.select("div.list-summary-wrapper.clearfix > div.item-evaluate-wrapper > div.item-evaluate > div.item-evaluate-count").get(0).text());
                recyclerViewData.setImageUrl("http:" + element.select("div.list-group-item-author.clearfix a.avatar-photo img").attr("src"));
                recyclerViewData.setContentUrl("https://okky.kr/article/" + element.select("div.list-title-wrapper.clearfix span.list-group-item-text.article-id").text().replace("#", ""));
                recyclerViewData.setType(OKKY);
                recyclerViewDataArrayList.add(recyclerViewData);
            }
            return recyclerViewDataArrayList;
        });

        // merge 참고
        // http://www.introtorx.com/content/v1.0.10621.0/12_CombiningSequences.html
        final Observable<ArrayList<RecyclerViewData>> mergedData = Observable.merge(stackOverflowData, okkyTechData, okkyQnAData);

        mergedData.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ArrayList<RecyclerViewData>>() {
                    ArrayList<RecyclerViewData> finalRecyclerViewData = new ArrayList<>();

                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        mMainActivityView.showProgressDialog(subscribeProgressDialog, mContext.getString(R.string.loading));
                    }

                    @Override
                    public void onNext(@NonNull final ArrayList<RecyclerViewData> recyclerViewTotalData) {
                        finalRecyclerViewData = recyclerViewTotalData;
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        mMainActivityView.dismissProgressDialog(subscribeProgressDialog);
                        mMainActivityView.showCustomToast(e.getMessage());
                    }

                    @Override
                    public void onComplete() {
                        Collections.sort(finalRecyclerViewData, mComparator);
                        if (pageCount == 1) {
                            mMainActivityView.setDisplayRecyclerView(finalRecyclerViewData);
                        } else {
                            mMainActivityView.addAdditionalData(finalRecyclerViewData);
                        }

                        mMainActivityView.dismissProgressDialog(subscribeProgressDialog);
                    }
                });
    }

    private void setOnOffMixData(final String baseUrl, final int pageCount) {
        final ArrayList<RecyclerViewData> recyclerViewDataArrayList = new ArrayList<>();
        final ProgressDialog subscribeProgressDialog = new ProgressDialog(mContext);

        final Retrofit RETROFIT_BUILDER = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        final RetrofitInterface RETROFIT_INTERFACE = RETROFIT_BUILDER.create(RetrofitInterface.class);

        RETROFIT_INTERFACE.OnOffMixRx("api.onoffmix.com/event/list", "json", 12,
                "if(recruitEndDateTime-NOW()>0# 1# 0)|DESC,FIND_IN_SET('advance'#wayOfRegistration)|DESC,popularity|DESC,idx|DESC", pageCount, "", "", "", "true", "true", "true", "개발", "", "", 1)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<OnOffMixData>() {
                    @Override
                    public void onSubscribe(@NonNull final Disposable d) {
                        mMainActivityView.showProgressDialog(subscribeProgressDialog, mContext.getString(R.string.loading));
                    }

                    @Override
                    public void onNext(@NonNull final OnOffMixData onOffMixData) {
                        if (onOffMixData.getError().getCode() == 0) {
                            for (final OnOffMixEventListData eventListData : onOffMixData.getEventList()) {
                                final RecyclerViewData recyclerViewData = new RecyclerViewData();
                                recyclerViewData.setTitle(eventListData.getTitle());
                                recyclerViewData.setContent(null);
                                recyclerViewData.setWatchCount(eventListData.getTotalCanAttend() + mContext.getString(R.string.onOffMix_attend));
                                recyclerViewData.setFavoritesCount(eventListData.getCategoryIdx());
                                recyclerViewData.setSubInfo(eventListData.getUsePayment().equals("n") ? "무료" : "유료" + eventListData.getRegTime());
                                recyclerViewData.setImageResources(getBitmapFromVectorDrawable(mContext, R.drawable.ic_onoffmix_24dp));
                                recyclerViewData.setImageUrl(eventListData.getBannerUrl());
                                recyclerViewData.setContentUrl(eventListData.getEventUrl().replace("http://onoffmix", "http://m.onoffmix"));
                                recyclerViewData.setType(ON_OFF_MIX);
                                recyclerViewDataArrayList.add(recyclerViewData);
                            }
                        } else {
                            mMainActivityView.dismissProgressDialog(subscribeProgressDialog);
                            mMainActivityView.showCustomToast(onOffMixData.getError().getMessage());
                        }
                    }

                    @Override
                    public void onError(@NonNull final Throwable e) {
                        mMainActivityView.dismissProgressDialog(subscribeProgressDialog);
                        mMainActivityView.showCustomToast(e.getMessage());
                    }

                    @Override
                    public void onComplete() {
                        if (pageCount == 1) {
                            mMainActivityView.setDisplayRecyclerView(recyclerViewDataArrayList);
                        } else {
                            mMainActivityView.addAdditionalData(recyclerViewDataArrayList);
                        }

                        mMainActivityView.dismissProgressDialog(subscribeProgressDialog);
                    }
                });
    }

    private void setLeftNavigationMenuItems() {
        final SharedPreferences sharedPreferences = mContext.getSharedPreferences(PREF_ID, Activity.MODE_PRIVATE);
        final String email = sharedPreferences.getString("email", null);
        final String password = sharedPreferences.getString("password", null);

        final JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("email", email);
            jsonObject.put("password", password);
        } catch (final JSONException e) {
            mMainActivityView.showCustomToast(e.getMessage());
            return;
        }

        final Observable<FavoritesCategoryData> callCategoryRx = RETROFIT_INTERFACE.CallCategoryRx(RequestBody.create(MEDIA_TYPE_JSON, jsonObject.toString()));
        callCategoryRx.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(t -> {
                            if (t.isSuccess()) {
                                final ArrayList<FavoritesCategoryCodeData> categoryNameArrayList = t.getData();
                                final ArrayList<MenuData> menuDataArrayList = getMenuData(categoryNameArrayList);
                                final ArrayList<NavigationMenuData> groupMenuDataArrayList = getExpandableMenuData(menuDataArrayList);

                                mMainActivityView.setBottomMenuItems(menuDataArrayList);
                                mMainActivityView.setExpandableMenuItems(groupMenuDataArrayList);
                            } else {
                                mMainActivityView.showCustomToast(t.getData().get(0).getCategoryCode());
                            }
                        },
                        e -> mMainActivityView.showCustomToast(ExceptionHelper.getApplicationExceptionMessage((Exception) e)));
    }

    @Override
    public void changeCategory(final MenuItem item, final int pageCount) {
        //2017.05.21
        //이미 refreshDisplay 를 통해서 즐겨찾기가 구성되어있는 상태이므로 메뉴를 재구성 하지 못하도록하자
        //(선택되어있던 카테고리가 즐겨찾기에서 제거됬었을경우, 메인화면을 일일히 refresh 하지 못하기 때문에 MainActivity 에 새로 접근할때만 메뉴 재구성하는것으로,
        //카테고리 즐겨찾기는 꼭 다른 창에서 고를 수 있도록)
        //카테고리 즐겨찾기는 회원가입때 고를 수 있게하거나, 개인정보 수정창에서 고를 수 있도록하자!
        //mMainActivityView.setBottomMenuItems(getMenuItems());
        //2017.06.05
        //인텐트 FLAG -> CLEAR_TOP 을 써서 해결

        String categoryParam = "";
        if (item.getTitle().equals(mContext.getString(R.string.android))) {
            categoryParam = "android";
        } else if (item.getTitle().equals(mContext.getString(R.string.java))) {
            categoryParam = "java";
        } else if (item.getTitle().equals(mContext.getString(R.string.python))) {
            categoryParam = "python";
        } else if (item.getTitle().equals(mContext.getString(R.string.php))) {
            categoryParam = "php";
        } else if (item.getTitle().equals(mContext.getString(R.string.javascript))) {
            categoryParam = "javascript";
        }

        switch (item.getItemId()) {
            case 0:
                setHomeData();
                break;
            case 6:
                setOnOffMixData("http://onoffmix.com/", pageCount);
                break;
            default:
                setCategoryData(categoryParam, pageCount);
                break;
        }
    }

    @Override
    public void refreshDisplay(final ArrayList<RecyclerViewData> finalRecyclerViewData) {
        // 서버에서 가져온 유저의 즐겨찾기 데이터를 바탕으로
        // 확장가능한 카테고리 네비게이션 메뉴 데이터 값 세팅해주기
        setLeftNavigationMenuItems();

        // 홈 카테고리일때 OnOffMix, StackOverflow, OKKY 사이트들의 인기글들을 세팅해주기
        setHomeData();
    }
}
