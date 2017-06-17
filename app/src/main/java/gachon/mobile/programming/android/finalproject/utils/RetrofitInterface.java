package gachon.mobile.programming.android.finalproject.utils;

import gachon.mobile.programming.android.finalproject.models.FavoritesCategoryData;
import gachon.mobile.programming.android.finalproject.models.FavoritesContentData;
import gachon.mobile.programming.android.finalproject.models.SingleData;
import gachon.mobile.programming.android.finalproject.models.OnOffMixData;
import gachon.mobile.programming.android.finalproject.models.UserData;
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
    Observable<UserData> LoginRx(@Body RequestBody params);

    @POST("auth/register")
    Observable<SingleData> RegisterRx(@Body RequestBody params);

    @POST("auth/favorites/content/call")
    Observable<FavoritesContentData> CallContentRx(@Body RequestBody params);

    @POST("auth/favorites/content/store")
    Observable<SingleData> StoreContentRx(@Body RequestBody params);

    @POST("auth/favorites/category/call")
    Observable<FavoritesCategoryData> CallCategoryRx(@Body RequestBody params);

    @POST("auth/favorites/category/store")
    Observable<SingleData> StoreCategoryRx(@Body RequestBody params);

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
