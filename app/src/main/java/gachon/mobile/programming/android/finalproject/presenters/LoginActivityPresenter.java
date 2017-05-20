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
    private final LoginActivityView mLoginActivityView;
    private final Context mContext;

    public LoginActivityPresenter(Context context, LoginActivityView loginActivityView) {
        this.mContext = context;
        this.mLoginActivityView = loginActivityView;
    }

    private void validateAndLogin() {
        String email = mLoginActivityView.getEmail();
        String password = mLoginActivityView.getPassword();

        if (!isEmailValid(email)) {
            mLoginActivityView.setEmailError(mContext.getString(R.string.error_invalid_email));
            return;
        }

        if (!isPasswordValid(password)) {
            mLoginActivityView.setPasswordError(mContext.getString(R.string.error_invalid_password));
            return;
        }

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("email", email);
            jsonObject.put("password", password);
        } catch (JSONException e) {
            mLoginActivityView.validateFailure(e.getMessage());
            return;
        }

        mLoginActivityView.validateSuccess(jsonObject);
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
