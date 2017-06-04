package gachon.mobile.programming.android.finalproject.views;

/**
 * Created by JJSOFT-DESKTOP on 2017-05-09.
 */

public interface LoginActivityView {

    String getEmail();

    String getPassword();

    boolean getAutoLoginCheckBox();

    void setEmailError(String message);

    void setPasswordError(String message);

    void validateSuccess();

    void validateFailure(String message);

    interface UserInteractions{
        void attemptLogin();
    }
}
