package gachon.mobile.programming.android.finalproject.utils;

import gachon.mobile.programming.android.finalproject.DTO.LoginData;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * Created by JJSoft on 2017-05-08.
 */

public interface RetrofitInterface {
    //URL encoding하여 보냅니다.
    // POST 방식,
    // 파라메터는 @Field("파라메터명") 으로 보낼 수 있습니다.
    // Json형식에 맞게 Bean객체를 만들어 두면 설정항 Parser가 자동으로 컨버팅해 돌려 줍니다.
    @FormUrlEncoded
    @POST("auth/login")
    Call<LoginData> Login(@Field("email") String email, @Field("password") String password);
}
