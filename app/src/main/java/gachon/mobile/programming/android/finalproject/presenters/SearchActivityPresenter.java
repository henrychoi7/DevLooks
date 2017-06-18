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

import gachon.mobile.programming.android.finalproject.R;
import gachon.mobile.programming.android.finalproject.models.OnOffMixData;
import gachon.mobile.programming.android.finalproject.models.OnOffMixEventListData;
import gachon.mobile.programming.android.finalproject.models.RecyclerViewData;
import gachon.mobile.programming.android.finalproject.utils.RetrofitInterface;
import gachon.mobile.programming.android.finalproject.views.SearchActivityView;
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
import static gachon.mobile.programming.android.finalproject.utils.ApplicationClass.STACK_OVERFLOW_MAIN;
import static gachon.mobile.programming.android.finalproject.utils.ApplicationClass.getBitmapFromVectorDrawable;

public class SearchActivityPresenter implements SearchActivityView.UserInteractions {
    private final SearchActivityView mSearchActivityView;
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

    public SearchActivityPresenter(final Context context, final SearchActivityView searchActivityView) {
        this.mContext = context;
        this.mSearchActivityView = searchActivityView;
    }

    private ArrayList<RecyclerViewData> setStackOverflowSearchData(final ArrayList<RecyclerViewData> recyclerViewDataArrayList, final Elements elements) {
        for (final Element element : elements) {
            final RecyclerViewData recyclerViewData = new RecyclerViewData();
            recyclerViewData.setTitle(element.select("div.summary div.result-link a").text());
            final Elements tagElements = element.select("div.summary div.tags a");
            String tagString = "";
            for (final Element tagElement : tagElements) {
                tagString += " [" + tagElement.text() + "]";
            }
            recyclerViewData.setTags(tagString);
            recyclerViewData.setContent(element.select("div.summary div.excerpt").text());
            recyclerViewData.setSubInfo(element.select("div.summary div.started").text());
            recyclerViewData.setWatchCount(element.select("div.statscontainer div.stats div.status.answered strong").text());
            recyclerViewData.setFavoritesCount(element.select("div.statscontainer div.stats div.vote div.votes span").text());
            recyclerViewData.setImageResources(getBitmapFromVectorDrawable(mContext, R.drawable.ic_stack_overflow_24dp));
            recyclerViewData.setImageUrl(null);
            recyclerViewData.setContentUrl("https://stackoverflow.com" + element.select("div.summary div.result-link a").attr("href"));
            recyclerViewData.setType(STACK_OVERFLOW_MAIN);
            recyclerViewDataArrayList.add(recyclerViewData);
        }
        return recyclerViewDataArrayList;
    }

    private ArrayList<RecyclerViewData> setStackOverflowTagData(final ArrayList<RecyclerViewData> recyclerViewDataArrayList, final Elements elements) {
        for (final Element element : elements) {
            final RecyclerViewData recyclerViewData = new RecyclerViewData();
            recyclerViewData.setTitle(element.select("div.summary h3 a.question-hyperlink").text());
            final Elements tagElements = element.select("div.summary div.tags a");
            String tagString = "";
            for (final Element tagElement : tagElements) {
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
    }

    private ArrayList<RecyclerViewData> setOKKYTechData(final ArrayList<RecyclerViewData> recyclerViewDataArrayList, final Elements elements) {
        for (final Element element : elements) {
            final RecyclerViewData recyclerViewData = new RecyclerViewData();
            recyclerViewData.setTitle(element.select("div.list-title-wrapper.clearfix h5.list-group-item-heading a").text());
            final Elements tagElements = element.select("div.list-title-wrapper.clearfix a.list-group-item-text");
            String tagString = "";
            for (final Element tagElement : tagElements) {
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
    }

    private ArrayList<RecyclerViewData> setOKKYQnAData(final ArrayList<RecyclerViewData> recyclerViewDataArrayList, final Elements elements) {
        for (final Element element : elements) {
            final RecyclerViewData recyclerViewData = new RecyclerViewData();
            recyclerViewData.setTitle(element.select("div.list-title-wrapper.clearfix h5.list-group-item-heading a").text());
            final Elements tagElements = element.select("div.list-title-wrapper.clearfix a.list-group-item-text");
            String tagString = "";
            for (final Element tagElement : tagElements) {
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
    }

    @Override
    public void refreshDisplay(final String searchValue) {
        final ArrayList<RecyclerViewData> recyclerViewDataArrayList = new ArrayList<>();
        final ProgressDialog subscribeProgressDialog = new ProgressDialog(mContext);

        final Observable<ArrayList<RecyclerViewData>> stackOverflowData = Observable.fromCallable(() -> {
            final Document document = Jsoup.connect("https://stackoverflow.com/search?page=1&tab=Relevance&q=" + searchValue.replace(" ", "+")).get();
            Elements elements = document.select("div.search-results.js-search-results div.question-summary.search-result");
            if (elements.size() > 0) {
                return setStackOverflowSearchData(recyclerViewDataArrayList, elements);
            } else {
                elements = document.select("div#questions.content-padding div.question-summary");
                return setStackOverflowTagData(recyclerViewDataArrayList, elements);
            }
        });

        final Observable<ArrayList<RecyclerViewData>> okkyTechData = Observable.fromCallable(() -> {
            final Document document = Jsoup.connect("https://okky.kr/articles/tech?query=" + searchValue + "&sort=id&order=desc").get();
            final Elements elements = document.select("ul.list-group li.list-group-item");
            return setOKKYTechData(recyclerViewDataArrayList, elements);
        });

        final Observable<ArrayList<RecyclerViewData>> okkyQnAData = Observable.fromCallable(() -> {
            final Document document = Jsoup.connect("https://okky.kr/articles/questions?query=" + searchValue + "&sort=id&order=desc").get();
            final Elements elements = document.select("ul.list-group li.list-group-item");
            return setOKKYQnAData(recyclerViewDataArrayList, elements);
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
                        mSearchActivityView.showProgressDialog(subscribeProgressDialog);
                    }

                    @Override
                    public void onNext(@NonNull final ArrayList<RecyclerViewData> recyclerViewTotalData) {
                        finalRecyclerViewData = recyclerViewTotalData;
                        if (finalRecyclerViewData.size() == 0) {
                            mSearchActivityView.setDisplayRecyclerView(finalRecyclerViewData);
                        }
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        mSearchActivityView.dismissProgressDialog(subscribeProgressDialog);
                        mSearchActivityView.showCustomToast(e.getMessage());
                    }

                    @Override
                    public void onComplete() {
                        Collections.sort(finalRecyclerViewData, mComparator);
                        mSearchActivityView.setDisplayRecyclerView(finalRecyclerViewData);
                        mSearchActivityView.dismissProgressDialog(subscribeProgressDialog);
                    }
                });
    }
}