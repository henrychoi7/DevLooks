package gachon.mobile.programming.android.finalproject.presenters;

import android.app.ProgressDialog;
import android.content.Context;

import java.util.ArrayList;

import gachon.mobile.programming.android.finalproject.R;
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
    public void refreshDisplay() {
        setOnOffMixData("http://onoffmix.com/");
    }
}
