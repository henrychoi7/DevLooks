package gachon.mobile.programming.android.finalproject.presenters;

import android.app.ProgressDialog;
import android.content.Context;

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
import gachon.mobile.programming.android.finalproject.models.OnOffMixData;
import gachon.mobile.programming.android.finalproject.models.OnOffMixEventListData;
import gachon.mobile.programming.android.finalproject.models.RecyclerViewData;
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
import static gachon.mobile.programming.android.finalproject.utils.ApplicationClass.getBitmapFromVectorDrawable;

public class SubActivityPresenter implements SubActivityView.UserInteractions {
    private final SubActivityView mSubActivityView;
    private final Context mContext;

    private final Comparator mComparator = new Comparator() {
        private final Collator collator = Collator.getInstance();

        @Override
        public int compare(Object objectFront, Object objectBack) {
            RecyclerViewData recyclerViewDataFront = (RecyclerViewData) objectFront;
            RecyclerViewData recyclerViewDataBack = (RecyclerViewData) objectBack;
            return collator.compare(String.valueOf(recyclerViewDataBack.getFavoritesCount().length()), String.valueOf(recyclerViewDataFront.getFavoritesCount().length()));
        }
    };

    public SubActivityPresenter(final Context context, final SubActivityView subActivityView) {
        this.mContext = context;
        this.mSubActivityView = subActivityView;
    }

    private void setStackOverflowData(final String baseUrl, int pageCount) {
        final ArrayList<RecyclerViewData> recyclerViewDataArrayList = new ArrayList<>();
        final ProgressDialog subscribeProgressDialog = new ProgressDialog(mContext);

        Observable<ArrayList<RecyclerViewData>> stackOverflowData = Observable.fromCallable(() -> {
            Document document = Jsoup.connect(baseUrl).get();
            Elements elements = document.select("div#questions.content-padding div.question-summary");
            for (final Element element : elements) {
                final RecyclerViewData recyclerViewData = new RecyclerViewData();
                recyclerViewData.setTitle(element.select("div.summary h3 a.question-hyperlink").text());
                Elements tagElements = element.select("div.summary div.tags a");
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

        stackOverflowData.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ArrayList<RecyclerViewData>>() {
                    ArrayList<RecyclerViewData> finalRecyclerViewData = new ArrayList<>();

                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        mSubActivityView.showProgressDialog(subscribeProgressDialog);
                    }

                    @Override
                    public void onNext(@NonNull ArrayList<RecyclerViewData> recyclerViewTotalData) {
                        finalRecyclerViewData = recyclerViewTotalData;
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        mSubActivityView.dismissProgressDialog(subscribeProgressDialog);
                        mSubActivityView.showCustomToast(e.getMessage());
                    }

                    @Override
                    public void onComplete() {
                        Collections.sort(finalRecyclerViewData, mComparator);
                        if (pageCount == 1) {
                            mSubActivityView.setDisplayRecyclerView(finalRecyclerViewData);
                        } else {
                            mSubActivityView.addAdditionalData(finalRecyclerViewData);
                        }

                        mSubActivityView.dismissProgressDialog(subscribeProgressDialog);
                    }
                });
    }

    private void setOKKYTechData(final String baseUrl, int pageCount) {
        final ArrayList<RecyclerViewData> recyclerViewDataArrayList = new ArrayList<>();
        final ProgressDialog subscribeProgressDialog = new ProgressDialog(mContext);

        Observable<ArrayList<RecyclerViewData>> okkyTechData = Observable.fromCallable(() -> {
            final Document document = Jsoup.connect(baseUrl).get();
            Elements elements = document.select("ul.list-group li.list-group-item");
            for (final Element element : elements) {
                final RecyclerViewData recyclerViewData = new RecyclerViewData();
                recyclerViewData.setTitle(element.select("div.list-title-wrapper.clearfix h5.list-group-item-heading a").text());
                Elements tagElements = element.select("div.list-title-wrapper.clearfix a.list-group-item-text");
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

        okkyTechData.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ArrayList<RecyclerViewData>>() {
                    ArrayList<RecyclerViewData> finalRecyclerViewData = new ArrayList<>();

                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        mSubActivityView.showProgressDialog(subscribeProgressDialog);
                    }

                    @Override
                    public void onNext(@NonNull ArrayList<RecyclerViewData> recyclerViewTotalData) {
                        finalRecyclerViewData = recyclerViewTotalData;
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        mSubActivityView.dismissProgressDialog(subscribeProgressDialog);
                        mSubActivityView.showCustomToast(e.getMessage());
                    }

                    @Override
                    public void onComplete() {
                        Collections.sort(finalRecyclerViewData, mComparator);
                        if (pageCount == 1) {
                            mSubActivityView.setDisplayRecyclerView(finalRecyclerViewData);
                        } else {
                            mSubActivityView.addAdditionalData(finalRecyclerViewData);
                        }

                        mSubActivityView.dismissProgressDialog(subscribeProgressDialog);
                    }
                });
    }

    private void setOKKYQnAData(final String baseUrl, int pageCount) {
        final ArrayList<RecyclerViewData> recyclerViewDataArrayList = new ArrayList<>();
        final ProgressDialog subscribeProgressDialog = new ProgressDialog(mContext);

        Observable<ArrayList<RecyclerViewData>> okkyQnAData = Observable.fromCallable(() -> {
            final Document document = Jsoup.connect(baseUrl).get();
            Elements elements = document.select("ul.list-group li.list-group-item");
            for (final Element element : elements) {
                final RecyclerViewData recyclerViewData = new RecyclerViewData();
                recyclerViewData.setTitle(element.select("div.list-title-wrapper.clearfix h5.list-group-item-heading a").text());
                Elements tagElements = element.select("div.list-title-wrapper.clearfix a.list-group-item-text");
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

        okkyQnAData.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ArrayList<RecyclerViewData>>() {
                    ArrayList<RecyclerViewData> finalRecyclerViewData = new ArrayList<>();

                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        mSubActivityView.showProgressDialog(subscribeProgressDialog);
                    }

                    @Override
                    public void onNext(@NonNull ArrayList<RecyclerViewData> recyclerViewTotalData) {
                        finalRecyclerViewData = recyclerViewTotalData;
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        mSubActivityView.dismissProgressDialog(subscribeProgressDialog);
                        mSubActivityView.showCustomToast(e.getMessage());
                    }

                    @Override
                    public void onComplete() {
                        Collections.sort(finalRecyclerViewData, mComparator);
                        if (pageCount == 1) {
                            mSubActivityView.setDisplayRecyclerView(finalRecyclerViewData);
                        } else {
                            mSubActivityView.addAdditionalData(finalRecyclerViewData);
                        }

                        mSubActivityView.dismissProgressDialog(subscribeProgressDialog);
                    }
                });
    }

    private void setOnOffMixData(final String baseUrl, int pageCount) {
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
                        mSubActivityView.showProgressDialog(subscribeProgressDialog);
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
                        if (pageCount == 1) {
                            mSubActivityView.setDisplayRecyclerView(recyclerViewDataArrayList);
                        } else {
                            mSubActivityView.addAdditionalData(recyclerViewDataArrayList);
                        }

                        mSubActivityView.dismissProgressDialog(subscribeProgressDialog);
                    }
                });
    }

    @Override
    public void refreshDisplay(final int groupValue, final String childTitle, int pageCount) {
        final String categoryCode = String.format(Locale.KOREAN, "%03d", groupValue);
        final String groupValueTitle = CategoryMenuEnum.findNameByValue(categoryCode, mContext);
        if (groupValueTitle != null) {
            if (groupValueTitle.equals(mContext.getString(R.string.etc))) {
                setOnOffMixData("http://onoffmix.com/", pageCount);
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

                int okkyOffsetCount = (pageCount - 1) * 20;
                if (childTitle.equals(mContext.getString(R.string.stack_overflow))) {
                    setStackOverflowData("https://stackoverflow.com/questions/tagged/" + categoryParam + "?page=" + pageCount + "&sort=frequent&pagesize=15", pageCount);
                } else if (childTitle.equals(mContext.getString(R.string.okky_tech))) {
                    setOKKYTechData("https://okky.kr/articles/tech?offset=" + okkyOffsetCount + "&max=20&sort=voteCount&order=desc&query=" + categoryParam, pageCount);
                } else if (childTitle.equals(mContext.getString(R.string.okky_qna))) {
                    setOKKYQnAData("https://okky.kr/articles/questions?offset=" + okkyOffsetCount + "&max=20&sort=voteCount&order=desc&query=" + categoryParam, pageCount);
                } else {
                    mSubActivityView.showCustomToast(mContext.getString(R.string.load_error));
                }
            }
        }
    }
}
