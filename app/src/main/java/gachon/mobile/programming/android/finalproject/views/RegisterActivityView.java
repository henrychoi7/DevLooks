package gachon.mobile.programming.android.finalproject.views;

import android.app.ProgressDialog;

/**
 * Created by henryman on 05/06/2017.
 */

public interface RegisterActivityView {

    void showProgressDialog(final ProgressDialog subscribeProgressDialog);

    void dismissProgressDialog(final ProgressDialog subscribeProgressDialog);

    String getUsername();

    String getEmail();

    String getPassword();

    String getPasswordConfirm();

    String getPhone();

    void setUsernameError(final String message);

    void setEmailError(final String message);

    void setPasswordError(final String message);

    void setPasswordConfirmError(final String message);

    void setPhoneError(final String message);

    void validateSuccess();

    void validateFailure(final String message);

    interface UserInteractions{
        void attemptSignUp();
    }
}
