package gachon.mobile.programming.android.finalproject.presenters;

import android.content.Context;

import org.json.JSONException;
import org.json.JSONObject;

import gachon.mobile.programming.android.finalproject.R;
import gachon.mobile.programming.android.finalproject.views.LoginActivityView;

/**
 * Created by JJSOFT-DESKTOP on 2017-05-09.
 */

public class LoginActivityPresenter implements LoginActivityView.UserInteractions {
    private final LoginActivityView loginActivityView;
    private final Context mContext;

    public LoginActivityPresenter(Context context, LoginActivityView loginActivityView) {
        this.mContext = context;
        this.loginActivityView = loginActivityView;
    }

    private void validateAndLogin() {
        String email = loginActivityView.getEmail();
        String password = loginActivityView.getPassword();

        if (!isEmailValid(email)) {
            loginActivityView.setEmailError(mContext.getString(R.string.error_invalid_email));
            return;
        }

        if (!isPasswordValid(password)) {
            loginActivityView.setPasswordError(mContext.getString(R.string.error_invalid_password));
            return;
        }

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("email", email);
            jsonObject.put("password", password);
        } catch (JSONException e) {
            loginActivityView.validateFailure(e.getMessage());
            return;
        }

        loginActivityView.validateSuccess(jsonObject);
    }

    private boolean isEmailValid(String email) {
        return email.contains("@") && email.length() <= 30;
    }

    private boolean isPasswordValid(String password) {
        return password.length() >= 6 && password.length() <= 60;
    }

    @Override
    public void attemptLogin() {
        validateAndLogin();
    }
}
