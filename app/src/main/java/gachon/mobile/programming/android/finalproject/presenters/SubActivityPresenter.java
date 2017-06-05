package gachon.mobile.programming.android.finalproject.presenters;

import android.app.ProgressDialog;
import android.content.Context;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.Locale;

import gachon.mobile.programming.android.finalproject.R;
import gachon.mobile.programming.android.finalproject.enums.CategoryMenuEnum;
import gachon.mobile.programming.android.finalproject.enums.ExpandableMenuEnum;
import gachon.mobile.programming.android.finalproject.models.OnOffMixData;
import gachon.mobile.programming.android.finalproject.models.OnOffMixEventListData;
import gachon.mobile.programming.android.finalproject.models.RecyclerViewData;
import gachon.mobile.programming.android.finalproject.utils.ExceptionHelper;
import gachon.mobile.programming.android.finalproject.utils.RetrofitInterface;
import gachon.mobile.programming.android.finalproject.views.SubActivityView;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by JJSOFT-DESKTOP on 2017-05-21.
 */

public class SubActivityPresenter implements SubActivityView.UserInteractions {
    private final SubActivityView mSubActivityView;
    private final Context mContext;

    public SubActivityPresenter(Context context, SubActivityView subActivityView) {
        this.mContext = context;
        this.mSubActivityView = subActivityView;
    }

    private void setStackOverflowData(String baseUrl) {
        ArrayList<RecyclerViewData> recyclerViewDataArrayList = new ArrayList<>();
        ProgressDialog subscribeProgressDialog = new ProgressDialog(mContext);

        Observable.fromCallable(() -> {
            Document document = Jsoup.connect(baseUrl).get();
            return document.select("div#questions.content-padding div.question-summary");
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Elements>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        mSubActivityView.showProgressDialog(subscribeProgressDialog);
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
                        mSubActivityView.dismissProgressDialog(subscribeProgressDialog);
                    }

                    @Override
                    public void onComplete() {
                        mSubActivityView.setDisplayRecyclerView(recyclerViewDataArrayList);
                        mSubActivityView.dismissProgressDialog(subscribeProgressDialog);
                    }
                });
    }

    private void setOKKYData(String baseUrl) {
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
                        mSubActivityView.showProgressDialog(subscribeProgressDialog);
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
                        mSubActivityView.dismissProgressDialog(subscribeProgressDialog);
                    }

                    @Override
                    public void onComplete() {
                        mSubActivityView.setDisplayRecyclerView(recyclerViewDataArrayList);
                        mSubActivityView.dismissProgressDialog(subscribeProgressDialog);
                    }
                });
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
                        mSubActivityView.showProgressDialog(subscribeProgressDialog);
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
                            mSubActivityView.dismissProgressDialog(subscribeProgressDialog);
                            mSubActivityView.showCustomToast(onOffMixData.getError().getMessage());
                        }
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        mSubActivityView.dismissProgressDialog(subscribeProgressDialog);
                    }

                    @Override
                    public void onComplete() {
                        mSubActivityView.setDisplayRecyclerView(recyclerViewDataArrayList);
                        mSubActivityView.dismissProgressDialog(subscribeProgressDialog);
                    }
                });
    }

    @Override
    public void refreshDisplay(int groupValue, String childTitle) {
        final String categoryCode = String.format(Locale.KOREAN, "%03d", groupValue);
        final String groupValueTitle = CategoryMenuEnum.findNameByValue(categoryCode, mContext);
        if (groupValueTitle != null) {
            if (groupValueTitle.equals(mContext.getString(R.string.etc))) {
                setOnOffMixData("http://onoffmix.com/");
            } else {
                String categoryParam = "";
                if (groupValueTitle.equals(mContext.getString(R.string.android))) {
                    categoryParam = "android";
                } else if (groupValueTitle.equals(mContext.getString(R.string.java))) {
                    categoryParam = "java";
                } else if (groupValueTitle.equals(mContext.getString(R.string.python))) {
                    categoryParam = "python";
                } else if (groupValueTitle.equals(mContext.getString(R.string.php))) {
                    categoryParam = "php";
                } else if (groupValueTitle.equals(mContext.getString(R.string.javascript))) {
                    categoryParam = "javascript";
                }

                if (childTitle.equals(mContext.getString(R.string.stack_overflow))) {
                    setStackOverflowData("https://stackoverflow.com/questions/tagged/" + categoryParam);
                } else if (childTitle.equals(mContext.getString(R.string.okky_tech))) {
                    setOKKYData("https://okky.kr/articles/tech?query=" + categoryParam + "&sort=id&order=desc");
                } else if (childTitle.equals(mContext.getString(R.string.okky_qna))) {
                    setOKKYData("https://okky.kr/articles/questions?query=" + categoryParam + "&sort=id&order=desc");
                } else {
                    mSubActivityView.showCustomToast(mContext.getString(R.string.load_error));
                }
            }
        }
    }
}
