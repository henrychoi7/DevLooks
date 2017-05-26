package gachon.mobile.programming.android.finalproject.utils;

import gachon.mobile.programming.android.finalproject.models.LoginData;
import gachon.mobile.programming.android.finalproject.models.OnOffMixData;
import io.reactivex.Observable;
import okhttp3.RequestBody;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Created by JJSOFT-DESKTOP on 2017-05-09.
 */

public interface RetrofitInterface {
    @POST("auth/login")
    Observable<LoginData> LoginRx(@Body RequestBody params);

    @GET("_/xmlProxy/xmlProxy.ofm")
    Observable<OnOffMixData> OnOffMixRx(
            @Query("url") String url,
            @Query("output") String output,
            @Query("pageRows") int pageRows,
            @Query("sort") String sort,
            @Query("page") int page,
            @Query("searchAll") String searchAll,
            @Query("exclude") String exclude,
            @Query("numLT") String numLT,
            @Query("getPinCount") String getPinCount,
            @Query("getAttendCount") String getAttendCount,
            @Query("blockAbuse") String blockAbuse,
            @Query("s") String searchWord,
            @Query("eventStartDate") String eventStartDate,
            @Query("eventEndDate") String eventEndDate,
            @Query("isFree") int isFree
    );
}
