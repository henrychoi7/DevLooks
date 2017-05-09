package gachon.mobile.programming.android.finalproject.views;

import org.json.JSONObject;

/**
 * Created by JJSOFT-DESKTOP on 2017-05-09.
 */

public interface LoginActivityView {

    String getEmail();

    String getPassword();

    void setEmailError(String message);

    void setPasswordError(String message);

    void validateSuccess(JSONObject jsonObject);

    void validateFailure(String message);

    interface UserInteractions{
        void attemptLogin();
    }
}
