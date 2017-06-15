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

import static gachon.mobile.programming.android.finalproject.utils.ApplicationClass.OKKY;
import static gachon.mobile.programming.android.finalproject.utils.ApplicationClass.ON_OFF_MIX;
import static gachon.mobile.programming.android.finalproject.utils.ApplicationClass.STACK_OVERFLOW;

/**
 * Created by JJSOFT-DESKTOP on 2017-05-21.
 */

public class SubActivityPresenter implements SubActivityView.UserInteractions {
    private final SubActivityView mSubActivityView;
    private final Context mContext;

    public SubActivityPresenter(final Context context, final SubActivityView subActivityView) {
        this.mContext = context;
        this.mSubActivityView = subActivityView;
    }

    private void setStackOverflowData(final String baseUrl) {
        final ArrayList<RecyclerViewData> recyclerViewDataArrayList = new ArrayList<>();
        final ProgressDialog subscribeProgressDialog = new ProgressDialog(mContext);

        Observable.fromCallable(() -> {
            final Document document = Jsoup.connect(baseUrl).get();
            return document.select("div#questions.content-padding div.question-summary");
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Elements>() {
                    @Override
                    public void onSubscribe(@NonNull final Disposable d) {
                        mSubActivityView.showProgressDialog(subscribeProgressDialog);
                    }

                    @Override
                    public void onNext(@NonNull final Elements elements) {
                        for (final Element element : elements) {
                            final RecyclerViewData recyclerViewData = new RecyclerViewData();
                            recyclerViewData.setTitle(element.select("div.summary h3 a.question-hyperlink").text());
                            recyclerViewData.setContent(element.select("div.summary div.excerpt").text());
                            recyclerViewData.setImageUrl(element.select("div.started.fr div.user-info div.user-gravatar32 a div.gravatar-wrapper-32 img").attr("src"));
                            recyclerViewData.setContentUrl("https://stackoverflow.com" + element.select("div.summary h3 a.question-hyperlink").attr("href"));
                            recyclerViewData.setType(STACK_OVERFLOW);

                            recyclerViewDataArrayList.add(recyclerViewData);
                        }
                    }

                    @Override
                    public void onError(@NonNull final Throwable e) {
                        mSubActivityView.dismissProgressDialog(subscribeProgressDialog);
                        mSubActivityView.showCustomToast(e.getMessage());
                    }

                    @Override
                    public void onComplete() {
                        mSubActivityView.setDisplayRecyclerView(recyclerViewDataArrayList);
                        mSubActivityView.dismissProgressDialog(subscribeProgressDialog);
                    }
                });
    }

    private void setOKKYData(final String baseUrl) {
        final ArrayList<RecyclerViewData> recyclerViewDataArrayList = new ArrayList<>();
        final ProgressDialog subscribeProgressDialog = new ProgressDialog(mContext);

        Observable.fromCallable(() -> {
            final Document document = Jsoup.connect(baseUrl).get();
            return document.select("ul.list-group li.list-group-item");
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Elements>() {
                    @Override
                    public void onSubscribe(@NonNull final Disposable d) {
                        mSubActivityView.showProgressDialog(subscribeProgressDialog);
                    }

                    @Override
                    public void onNext(@NonNull final Elements elements) {
                        for (final Element element : elements) {
                            final RecyclerViewData recyclerViewData = new RecyclerViewData();
                            recyclerViewData.setTitle(element.select("div.list-title-wrapper.clearfix h5.list-group-item-heading a").text());
                            recyclerViewData.setContent(element.select("div.list-title-wrapper.clearfix a.list-group-item-text item-tag label label-info").text());
                            recyclerViewData.setImageUrl("http:" + element.select("div.list-group-item-author.clearfix a.avatar-photo img").attr("src"));
                            recyclerViewData.setContentUrl("https://okky.kr/article/" + element.select("div.list-title-wrapper.clearfix span.list-group-item-text.article-id").text().replace("#", ""));
                            recyclerViewData.setType(OKKY);
                            recyclerViewDataArrayList.add(recyclerViewData);
                        }
                    }

                    @Override
                    public void onError(@NonNull final Throwable e) {
                        mSubActivityView.dismissProgressDialog(subscribeProgressDialog);
                        mSubActivityView.showCustomToast(e.getMessage());
                    }

                    @Override
                    public void onComplete() {
                        mSubActivityView.setDisplayRecyclerView(recyclerViewDataArrayList);
                        mSubActivityView.dismissProgressDialog(subscribeProgressDialog);
                    }
                });
    }

    private void setOnOffMixData(final String baseUrl) {
        final ArrayList<RecyclerViewData> recyclerViewDataArrayList = new ArrayList<>();
        final ProgressDialog subscribeProgressDialog = new ProgressDialog(mContext);

        final Retrofit RETROFIT_BUILDER = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        final RetrofitInterface RETROFIT_INTERFACE = RETROFIT_BUILDER.create(RetrofitInterface.class);

        RETROFIT_INTERFACE.OnOffMixRx("api.onoffmix.com/event/list", "json", 12,
                "if(recruitEndDateTime-NOW()>0# 1# 0)|DESC,FIND_IN_SET('advance'#wayOfRegistration)|DESC,popularity|DESC,idx|DESC", 1, "", "", "", "true", "true", "true", "개발", "", "", 1)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<OnOffMixData>() {
                    @Override
                    public void onSubscribe(@NonNull final Disposable d) {
                        mSubActivityView.showProgressDialog(subscribeProgressDialog);
                    }

                    @Override
                    public void onNext(@NonNull final OnOffMixData onOffMixData) {
                        if (onOffMixData.getError().getCode() == 0) {
                            for (final OnOffMixEventListData eventListData : onOffMixData.getEventList()) {
                                final RecyclerViewData recyclerViewData = new RecyclerViewData();
                                recyclerViewData.setTitle(eventListData.getTitle());
                                //recyclerViewData.setContent(eventListData.getTotalCanAttend() + mContext.getString(R.string.onOffMix_attend));
                                recyclerViewData.setImageUrl(eventListData.getBannerUrl());
                                recyclerViewData.setContentUrl(eventListData.getEventUrl());
                                recyclerViewData.setType(ON_OFF_MIX);

                                recyclerViewDataArrayList.add(recyclerViewData);
                            }
                        } else {
                            mSubActivityView.dismissProgressDialog(subscribeProgressDialog);
                            mSubActivityView.showCustomToast(onOffMixData.getError().getMessage());
                        }
                    }

                    @Override
                    public void onError(@NonNull final Throwable e) {
                        mSubActivityView.dismissProgressDialog(subscribeProgressDialog);
                        mSubActivityView.showCustomToast(e.getMessage());
                    }

                    @Override
                    public void onComplete() {
                        mSubActivityView.setDisplayRecyclerView(recyclerViewDataArrayList);
                        mSubActivityView.dismissProgressDialog(subscribeProgressDialog);
                    }
                });
    }

    @Override
    public void refreshDisplay(final int groupValue, final String childTitle) {
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
