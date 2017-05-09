package gachon.mobile.programming.android.finalproject.utils;

import gachon.mobile.programming.android.finalproject.models.LoginData;
import io.reactivex.Observable;
import okhttp3.RequestBody;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * Created by JJSOFT-DESKTOP on 2017-05-09.
 */

public interface RetrofitInterface {
    @POST("auth/login")
    Observable<LoginData> LoginRx(@Body RequestBody params);
}
