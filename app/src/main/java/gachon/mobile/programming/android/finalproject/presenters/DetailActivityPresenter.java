package gachon.mobile.programming.android.finalproject.presenters;

import android.app.ProgressDialog;
import android.content.Context;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import gachon.mobile.programming.android.finalproject.views.DetailActivityView;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

import static gachon.mobile.programming.android.finalproject.utils.ApplicationClass.OKKY;
import static gachon.mobile.programming.android.finalproject.utils.ApplicationClass.ON_OFF_MIX;
import static gachon.mobile.programming.android.finalproject.utils.ApplicationClass.STACK_OVERFLOW;

/**
 * Created by JJSOFT-DESKTOP on 2017-05-21.
 */

public class DetailActivityPresenter implements DetailActivityView.UserInteractions{
    private final DetailActivityView mDetailActivityView;
    private final Context mContext;

    public DetailActivityPresenter(final Context context, final DetailActivityView detailActivityView) {
        this.mContext = context;
        this.mDetailActivityView = detailActivityView;
    }

    private void setStackOverflowData(final String baseUrl) {
        final ProgressDialog subscribeProgressDialog = new ProgressDialog(mContext);

        Observable.fromCallable(() -> {
            final Document document = Jsoup.connect(baseUrl).get();
            //return document.select("div.container div#content.snippet-hidden div#mainbar");
            return document.select("div.container div#content.snippet-hidden");
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Elements>() {
                    @Override
                    public void onSubscribe(@NonNull final Disposable d) {
                        mDetailActivityView.showProgressDialog(subscribeProgressDialog);
                    }

                    @Override
                    public void onNext(@NonNull final Elements elements) {
                        for (final Element element : elements) {
                            /*String htmlSources = "<div> <table>";
                            htmlSources += element.select("div#question.question table").html();
                            htmlSources += "</table>";
                            htmlSources += element.select("div#answers div.answer.accepted-answer").html();
                            htmlSources += element.select("div#answers div.answer").html();
                            htmlSources += "</div>";*/
                            String htmlSources = element.html();
                            //mDetailActivityView.setWebViewFromHtml("https://stackoverflow.com", htmlSources);
                            mDetailActivityView.setWebViewFromHtml(baseUrl, htmlSources);
                        }
                    }

                    @Override
                    public void onError(@NonNull final Throwable e) {
                        mDetailActivityView.dismissProgressDialog(subscribeProgressDialog);
                        mDetailActivityView.showCustomToast(e.getMessage());
                    }

                    @Override
                    public void onComplete() {
                        mDetailActivityView.dismissProgressDialog(subscribeProgressDialog);
                    }
                });
    }

    private void setOKKYData(final String baseUrl) {
        final ProgressDialog subscribeProgressDialog = new ProgressDialog(mContext);

        Observable.fromCallable(() -> {
            final Document document = Jsoup.connect(baseUrl).get();
            return document.select("div.layout-container div.main div#article.content");
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Elements>() {
                    @Override
                    public void onSubscribe(@NonNull final Disposable d) {
                        mDetailActivityView.showProgressDialog(subscribeProgressDialog);
                    }

                    @Override
                    public void onNext(@NonNull final Elements elements) {
                        for (final Element element : elements) {
                            String htmlSources = "<?xml version=\"1.0\" encoding=\"UTF-8\" ?>"+
                                            "<html><head>"+
                                            "<meta http-equiv=\"content-type\" content=\"text/html; charset=utf-8\" />"+
                                            "<head><body>";
                            htmlSources += "<div>";
                            for (Element detailElement : element.select("div.panel.panel-default.clearfix")) {
                                htmlSources += detailElement.select("div.content-container.clearfix").html();
                            }
                            htmlSources += "</div></body></html>";
                            mDetailActivityView.setWebViewFromHtml("https://okky.kr", htmlSources);
                            //mDetailActivityView.setWebViewFromHtml(element.html());
                        }
                    }

                    @Override
                    public void onError(@NonNull final Throwable e) {
                        mDetailActivityView.dismissProgressDialog(subscribeProgressDialog);
                        mDetailActivityView.showCustomToast(e.getMessage());
                    }

                    @Override
                    public void onComplete() {
                        mDetailActivityView.dismissProgressDialog(subscribeProgressDialog);
                    }
                });
    }

    private void setOnOffMixData(final String selectedUrl) {
        final ProgressDialog subscribeProgressDialog = new ProgressDialog(mContext);

        Observable.fromCallable(() -> {
            Document document = Jsoup.connect(selectedUrl).get();
            return document.select("div#content.content div.eventContent.box div.eventDescription div#description.html4style");
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Elements>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        mDetailActivityView.showProgressDialog(subscribeProgressDialog);
                    }

                    @Override
                    public void onNext(@NonNull Elements elements) {
                        for (Element element : elements) {
                            String htmlSources = element.html();
                            mDetailActivityView.setWebViewFromHtml("http://onoffmix.com", htmlSources);
                        }
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        mDetailActivityView.dismissProgressDialog(subscribeProgressDialog);
                        mDetailActivityView.showCustomToast(e.getMessage());
                    }

                    @Override
                    public void onComplete() {
                        mDetailActivityView.dismissProgressDialog(subscribeProgressDialog);
                    }
                });
    }

    @Override
    public void refreshDisplay(String selectedUrl, String selectedType) {
        if (selectedType.equals(STACK_OVERFLOW)) {
            setStackOverflowData(selectedUrl);
        } else if (selectedType.equals(OKKY)) {
            setOKKYData(selectedUrl);
        } else if (selectedType.equals(ON_OFF_MIX)) {
            setOnOffMixData(selectedUrl);
        }
    }
}
