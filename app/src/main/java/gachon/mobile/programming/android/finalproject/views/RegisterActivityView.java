package gachon.mobile.programming.android.finalproject.views;

/**
 * Created by henryman on 05/06/2017.
 */

public interface RegisterActivityView {

    String getUsername();

    String getEmail();

    String getPassword();

    String getPasswordConfirm();

    String getPhone();

    void setUsernameError(String message);

    void setEmailError(String message);

    void setPasswordError(String message);

    void setPasswordConfirmError(String message);

    void setPhoneError(String message);

    void validateSuccess();

    void validateFailure(String message);

    interface UserInteractions{
        void attemptSignUp();
    }
}
