package gachon.mobile.programming.android.finalproject.presenters;

import android.app.ProgressDialog;
import android.content.Context;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;

import gachon.mobile.programming.android.finalproject.models.RecyclerViewData;
import gachon.mobile.programming.android.finalproject.views.DetailActivityView;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

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

    @Override
    public void refreshDisplay(String selectedUrl) {
        final ProgressDialog subscribeProgressDialog = new ProgressDialog(mContext);
        //String htmlSources = "";

        Observable.fromCallable(() -> {
            Document document = Jsoup.connect(selectedUrl).get();
            return document.select("div#content.content");
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
                            mDetailActivityView.setTextViewFromHtml(htmlSources);
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
}
