package gachon.mobile.programming.android.finalproject.views;

import android.app.ProgressDialog;

public interface SettingActivityView {

    void showProgressDialog(final ProgressDialog subscribeProgressDialog);

    void dismissProgressDialog(final ProgressDialog subscribeProgressDialog);

    String getUsername();

    String getEmail();

    String getOriginalPassword();

    String getNewPassword();

    String getNewPasswordConfirm();

    String getPhone();

    void setUsernameError(final String message);

    void setEmailError(final String message);

    void setOriginalPasswordError(final String message);

    void setNewPasswordConfirmError(final String message);

    void setPhoneError(final String message);

    void validateAndUpdateSuccess();

    void validateAndDeleteSuccess();

    void showCustomToast(final String message);

    interface UserInteractions{
        void attemptUpdateInfo();

        void attemptDeleteInfo();
    }
}
