package gachon.mobile.programming.android.finalproject.presenters;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

import org.json.JSONException;
import org.json.JSONObject;

import gachon.mobile.programming.android.finalproject.R;
import gachon.mobile.programming.android.finalproject.models.FavoritesContentData;
import gachon.mobile.programming.android.finalproject.models.UserData;
import gachon.mobile.programming.android.finalproject.models.UserInfoData;
import gachon.mobile.programming.android.finalproject.utils.ExceptionHelper;
import gachon.mobile.programming.android.finalproject.views.LoginActivityView;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import okhttp3.RequestBody;

import static gachon.mobile.programming.android.finalproject.utils.ApplicationClass.MEDIA_TYPE_JSON;
import static gachon.mobile.programming.android.finalproject.utils.ApplicationClass.PREF_ID;
import static gachon.mobile.programming.android.finalproject.utils.ApplicationClass.RETROFIT_INTERFACE;

public class LoginActivityPresenter implements LoginActivityView.UserInteractions {
    private final LoginActivityView mLoginActivityView;
    private final Context mContext;

    public LoginActivityPresenter(final Context context, final LoginActivityView loginActivityView) {
        this.mContext = context;
        this.mLoginActivityView = loginActivityView;
    }

    private void validateAndLogin() {
        final String email = mLoginActivityView.getEmail();
        final String password = mLoginActivityView.getPassword();
        final boolean isCheckedAutoLogin = mLoginActivityView.getAutoLoginCheckBox();

        if (!isEmailValid(email)) {
            mLoginActivityView.setEmailError(mContext.getString(R.string.error_invalid_email));
            return;
        }

        if (!isPasswordValid(password)) {
            mLoginActivityView.setPasswordError(mContext.getString(R.string.error_invalid_password));
            return;
        }

        final JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("email", email);
            jsonObject.put("password", password);
        } catch (final JSONException e) {
            mLoginActivityView.validateFailure(e.getMessage());
            return;
        }

        final Observable<UserData> loginRx = RETROFIT_INTERFACE.LoginRx(RequestBody.create(MEDIA_TYPE_JSON, jsonObject.toString()));
        loginRx.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(t -> {
                            if (t.isSuccess()) {
                                final SharedPreferences.Editor sharedPreferencesEditors = mContext.getSharedPreferences(PREF_ID, Activity.MODE_PRIVATE).edit();
                                sharedPreferencesEditors.putBoolean("is_checked_auto_login", isCheckedAutoLogin);
                                sharedPreferencesEditors.putString("email", email);
                                sharedPreferencesEditors.putString("password", password);

                                final UserInfoData userInfoData = t.getData().get(0);
                                sharedPreferencesEditors.putString("name", userInfoData.getName());
                                sharedPreferencesEditors.putString("phone_number", userInfoData.getPhoneNumber());
                                sharedPreferencesEditors.apply();
                                mLoginActivityView.validateSuccess();
                            } else {
                                mLoginActivityView.validateFailure(t.getData().get(0).getName());
                            }
                        },
                        e -> mLoginActivityView.validateFailure(ExceptionHelper.getApplicationExceptionMessage((Exception) e)));
    }

    private boolean isEmailValid(final String email) {
        return email.contains("@") && email.length() <= 30;
    }

    private boolean isPasswordValid(final String password) {
        return password.length() >= 6 && password.length() <= 60;
    }

    @Override
    public void attemptLogin() {
        validateAndLogin();
    }
}
