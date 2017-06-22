package gachon.mobile.programming.android.finalproject.views;

public interface LoginActivityView {

    String getEmail();

    String getPassword();

    boolean getAutoLoginCheckBox();

    void setEmailError(final String message);

    void setPasswordError(final String message);

    void validateSuccess();

    void validateFailure(final String message);

    interface UserInteractions{
        void attemptLogin();
    }
}
