package gachon.mobile.programming.android.finalproject.presenters;

import android.app.ProgressDialog;
import android.content.Context;
import android.text.TextUtils;

import org.json.JSONException;
import org.json.JSONObject;

import gachon.mobile.programming.android.finalproject.R;
import gachon.mobile.programming.android.finalproject.models.SingleData;
import gachon.mobile.programming.android.finalproject.utils.ExceptionHelper;
import gachon.mobile.programming.android.finalproject.views.RegisterActivityView;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.RequestBody;

import static gachon.mobile.programming.android.finalproject.utils.ApplicationClass.MEDIA_TYPE_JSON;
import static gachon.mobile.programming.android.finalproject.utils.ApplicationClass.RETROFIT_INTERFACE;

/**
 * Created by henryman on 05/06/2017.
 */

public class RegisterActivityPresenter implements RegisterActivityView.UserInteractions {
    private final RegisterActivityView mRegisterActivityView;
    private final Context mContext;

    public RegisterActivityPresenter(final Context context, final RegisterActivityView registerActivityView) {
        this.mContext = context;
        this.mRegisterActivityView = registerActivityView;
    }

    private void validateAndSignUp() {
        final String username = mRegisterActivityView.getUsername();
        final String email = mRegisterActivityView.getEmail();
        final String password = mRegisterActivityView.getPassword();
        final String passwordConfirm = mRegisterActivityView.getPasswordConfirm();
        final String phone = mRegisterActivityView.getPhone();

        if (!isUsernameValid(username)) {
            mRegisterActivityView.setUsernameError(mContext.getString(R.string.error_invalid_username));
            return;
        }

        if (!isEmailValid(email)) {
            mRegisterActivityView.setEmailError(mContext.getString(R.string.error_invalid_email));
            return;
        }

        if (!isPasswordValid(password)) {
            mRegisterActivityView.setPasswordError(mContext.getString(R.string.error_invalid_password));
            return;
        }

        if (!isPasswordConfirmValid(password, passwordConfirm)) {
            mRegisterActivityView.setPasswordConfirmError(mContext.getString(R.string.error_invalid_password_confirm));
            return;
        }

        if (!isPhoneValid(phone)) {
            mRegisterActivityView.setPhoneError(mContext.getString(R.string.error_invalid_phone));
            return;
        }

        final JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("name", username);
            jsonObject.put("email", email);
            jsonObject.put("password", password);
            jsonObject.put("password_confirmation", passwordConfirm);
            jsonObject.put("phone_number", phone);
        } catch (final JSONException e) {
            mRegisterActivityView.validateFailure(e.getMessage());
            return;
        }

        final Observable<SingleData> registerRx = RETROFIT_INTERFACE.RegisterRx(RequestBody.create(MEDIA_TYPE_JSON, jsonObject.toString()));
        registerRx.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<SingleData>() {
                    final ProgressDialog subscribeProgressDialog = new ProgressDialog(mContext);

                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        mRegisterActivityView.showProgressDialog(subscribeProgressDialog);
                    }

                    @Override
                    public void onNext(@NonNull final SingleData singleData) {
                        mRegisterActivityView.dismissProgressDialog(subscribeProgressDialog);
                        if (singleData.isSuccess()) {
                            mRegisterActivityView.validateSuccess();
                        } else {
                            mRegisterActivityView.validateFailure(singleData.getData());
                        }
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        mRegisterActivityView.dismissProgressDialog(subscribeProgressDialog);
                        mRegisterActivityView.validateFailure(ExceptionHelper.getApplicationExceptionMessage((Exception) e));
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

    private boolean isPasswordValid(final String password) {
        return !TextUtils.isEmpty(password) && password.length() <= 60 && password.length() >= 6;
    }

    private boolean isPasswordConfirmValid(final String password, final String passwordconfirm) {
        return passwordconfirm.equals(password) && passwordconfirm.length() <= 60 && passwordconfirm.length() >= 4;
    }

    private boolean isPhoneValid(final String phone) {
        return TextUtils.isDigitsOnly(phone) && phone.length() <= 11 && phone.length() >= 10;
    }

    @Override
    public void attemptSignUp() {
        validateAndSignUp();
    }

}
