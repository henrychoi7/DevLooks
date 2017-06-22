package gachon.mobile.programming.android.finalproject.presenters;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import org.json.JSONException;
import org.json.JSONObject;

import gachon.mobile.programming.android.finalproject.R;
import gachon.mobile.programming.android.finalproject.models.SingleData;
import gachon.mobile.programming.android.finalproject.models.UserInfoData;
import gachon.mobile.programming.android.finalproject.utils.ExceptionHelper;
import gachon.mobile.programming.android.finalproject.views.SettingActivityView;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.RequestBody;

import static gachon.mobile.programming.android.finalproject.utils.ApplicationClass.MEDIA_TYPE_JSON;
import static gachon.mobile.programming.android.finalproject.utils.ApplicationClass.PREF_ID;
import static gachon.mobile.programming.android.finalproject.utils.ApplicationClass.RETROFIT_INTERFACE;

/**
 * Created by henryman on 05/06/2017.
 */

public class SettingActivityPresenter implements SettingActivityView.UserInteractions {
    private final SettingActivityView mSettingActivityView;
    private final Context mContext;

    public SettingActivityPresenter(final Context context, final SettingActivityView settingActivityView) {
        this.mContext = context;
        this.mSettingActivityView = settingActivityView;
    }

    private void validateAndUpdateUserInfo() {
        final String username = mSettingActivityView.getUsername();
        final String email = mSettingActivityView.getEmail();
        final SharedPreferences sharedPreferences = mContext.getSharedPreferences(PREF_ID, Activity.MODE_PRIVATE);
        final String password = sharedPreferences.getString("password", null);
        final String originalPassword = mSettingActivityView.getOriginalPassword();
        final String newPassword = mSettingActivityView.getNewPassword();
        final String newPasswordConfirm = mSettingActivityView.getNewPasswordConfirm();
        final String phone = mSettingActivityView.getPhone();

        boolean isChangePassword = false;

        if (!isEmailValid(email)) {
            mSettingActivityView.setEmailError(mContext.getString(R.string.error_invalid_email));
            return;
        }

        if (!isPasswordValid(password, originalPassword)) {
            mSettingActivityView.setOriginalPasswordError(mContext.getString(R.string.unmatched_value));
            return;
        }

        if (!TextUtils.isEmpty(newPassword)) {
            isChangePassword = true;
            if (!isNewPasswordConfirmValid(newPassword, newPasswordConfirm)) {
                mSettingActivityView.setNewPasswordConfirmError(mContext.getString(R.string.error_invalid_password_confirm));
                return;
            }
        }

        if (!isUsernameValid(username)) {
            mSettingActivityView.setUsernameError(mContext.getString(R.string.error_invalid_username));
            return;
        }

        if (!isPhoneValid(phone)) {
            mSettingActivityView.setPhoneError(mContext.getString(R.string.error_invalid_phone));
            return;
        }

        final JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("name", username);
            jsonObject.put("email", email);
            jsonObject.put("password", originalPassword);
            if (isChangePassword) {
                jsonObject.put("new_password", newPassword);
                jsonObject.put("new_password_confirmation", newPasswordConfirm);
            } else {
                jsonObject.put("new_password", originalPassword);
                jsonObject.put("new_password_confirmation", originalPassword);
            }
            jsonObject.put("phone_number", phone);
        } catch (final JSONException e) {
            mSettingActivityView.showCustomToast(ExceptionHelper.getApplicationExceptionMessage(e));
            return;
        }

        final Observable<SingleData> updateUserInfoRx = RETROFIT_INTERFACE.UpdateUserInfoRx(RequestBody.create(MEDIA_TYPE_JSON, jsonObject.toString()));
        boolean finalIsChangePassword = isChangePassword;
        updateUserInfoRx.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<SingleData>() {
                    final ProgressDialog subscribeProgressDialog = new ProgressDialog(mContext);

                    @Override
                    public void onSubscribe(Disposable d) {
                        mSettingActivityView.showProgressDialog(subscribeProgressDialog);
                    }

                    @Override
                    public void onNext(final SingleData singleData) {
                        mSettingActivityView.dismissProgressDialog(subscribeProgressDialog);
                        mSettingActivityView.showCustomToast(singleData.getData());
                        if (singleData.isSuccess()) {
                            final SharedPreferences.Editor sharedPreferencesEditors = mContext.getSharedPreferences(PREF_ID, Activity.MODE_PRIVATE).edit();
                            sharedPreferencesEditors.putString("email", email);
                            if (finalIsChangePassword) {
                                sharedPreferencesEditors.putString("password", newPasswordConfirm);
                            } else {
                                sharedPreferencesEditors.putString("password", originalPassword);
                            }
                            sharedPreferencesEditors.putString("name", username);
                            sharedPreferencesEditors.putString("phone_number", phone);
                            sharedPreferencesEditors.apply();

                            mSettingActivityView.validateAndUpdateSuccess();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        mSettingActivityView.dismissProgressDialog(subscribeProgressDialog);
                        mSettingActivityView.showCustomToast(ExceptionHelper.getApplicationExceptionMessage((Exception) e));
                    }

                    @Override
                    public void onComplete() {
                    }
                });
    }

    private void validateAndDeleteUserInfo() {
        final SharedPreferences sharedPreferences = mContext.getSharedPreferences(PREF_ID, Activity.MODE_PRIVATE);
        final String email = sharedPreferences.getString("email", "");
        final String password = sharedPreferences.getString("password", "");

        final JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("email", email);
            jsonObject.put("password", password);
        } catch (final JSONException e) {
            mSettingActivityView.showCustomToast(ExceptionHelper.getApplicationExceptionMessage(e));
            return;
        }

        final Observable<SingleData> deleteUserInfoRx = RETROFIT_INTERFACE.DeleteUserInfoRx(RequestBody.create(MEDIA_TYPE_JSON, jsonObject.toString()));
        deleteUserInfoRx.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<SingleData>() {
                    final ProgressDialog subscribeProgressDialog = new ProgressDialog(mContext);

                    @Override
                    public void onSubscribe(Disposable d) {
                        mSettingActivityView.showProgressDialog(subscribeProgressDialog);
                    }

                    @Override
                    public void onNext(final SingleData singleData) {
                        mSettingActivityView.dismissProgressDialog(subscribeProgressDialog);
                        mSettingActivityView.showCustomToast(singleData.getData());
                        if (singleData.isSuccess()) {
                            mSettingActivityView.validateAndDeleteSuccess();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        mSettingActivityView.dismissProgressDialog(subscribeProgressDialog);
                        mSettingActivityView.showCustomToast(ExceptionHelper.getApplicationExceptionMessage((Exception) e));
                    }

                    @Override
                    public void onComplete() {
                    }
                });
    }

    private boolean isUsernameValid(final String username) {
        return !TextUtils.isEmpty(username) && username.length() <= 20 && username.length() >= 4;
    }

    private boolean isEmailValid(final String email) {
        return email.contains("@") && email.length() <= 30;
    }

    private boolean isPasswordValid(final String password, final String originalPassword) {
        return originalPassword.equals(password) && !TextUtils.isEmpty(originalPassword) && originalPassword.length() <= 60 && originalPassword.length() >= 6;
    }

    private boolean isNewPasswordConfirmValid(final String password, final String passwordconfirm) {
        return passwordconfirm.equals(password) && passwordconfirm.length() <= 60 && passwordconfirm.length() >= 6;
    }

    private boolean isPhoneValid(final String phone) {
        return TextUtils.isDigitsOnly(phone) && phone.length() <= 11 && phone.length() >= 10;
    }

    @Override
    public void attemptUpdateInfo() {
        validateAndUpdateUserInfo();
    }

    @Override
    public void attemptDeleteInfo() {
        validateAndDeleteUserInfo();
    }
}
