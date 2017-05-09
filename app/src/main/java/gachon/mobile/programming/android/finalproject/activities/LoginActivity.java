package gachon.mobile.programming.android.finalproject.activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONObject;

import gachon.mobile.programming.android.finalproject.R;
import gachon.mobile.programming.android.finalproject.models.LoginData;
import gachon.mobile.programming.android.finalproject.presenters.LoginActivityPresenter;
import gachon.mobile.programming.android.finalproject.utils.BaseActivity;
import gachon.mobile.programming.android.finalproject.utils.ClearEditText;
import gachon.mobile.programming.android.finalproject.views.LoginActivityView;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import okhttp3.RequestBody;

import static gachon.mobile.programming.android.finalproject.utils.ApplicationClass.DisplayCustomToast;
import static gachon.mobile.programming.android.finalproject.utils.ApplicationClass.MEDIA_TYPE_JSON;
import static gachon.mobile.programming.android.finalproject.utils.ApplicationClass.RETROFIT_INTERFACE;

/**
 * Created by JJSOFT-DESKTOP on 2017-05-09.
 */

public class LoginActivity extends BaseActivity implements LoginActivityView {
    private ClearEditText mEmailView;
    private EditText mPasswordView;

    LoginActivityView.UserInteractions loginActivityPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        loginActivityPresenter = new LoginActivityPresenter(getApplicationContext(), this);

        mEmailView = (ClearEditText) findViewById(R.id.email);
        TextView emailTextView = (TextView) findViewById(R.id.email_text_edited);
        mEmailView.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                emailTextView.setText(String.valueOf(s.length()));
            }
        });
        mPasswordView = (EditText) findViewById(R.id.password);
        mPasswordView.setOnEditorActionListener((textView, id, keyEvent) -> {
            if (id == R.id.login || id == EditorInfo.IME_NULL) {
                loginActivityPresenter.attemptLogin();
                return true;
            }
            return false;
        });

        Button mEmailSignInButton = (Button) findViewById(R.id.email_sign_in_button);
        mEmailSignInButton.setOnClickListener(view -> loginActivityPresenter.attemptLogin());
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(getApplicationContext(), InitActivity.class).addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY));
        super.onBackPressed();
    }

    @Override
    public String getEmail() {
        return TextUtils.isEmpty(mEmailView.getText()) ? "" : mEmailView.getText().toString();
    }

    @Override
    public String getPassword() {
        return TextUtils.isEmpty(mPasswordView.getText()) ? "" : mPasswordView.getText().toString();
    }

    @Override
    public void setEmailError(String message) {
        mEmailView.setError(message);
    }

    @Override
    public void setPasswordError(String message) {
        mPasswordView.setError(message);
    }

    @Override
    public void validateSuccess(JSONObject jsonObject) {
        Observable<LoginData> loginRx = RETROFIT_INTERFACE.LoginRx(RequestBody.create(MEDIA_TYPE_JSON, jsonObject.toString()));
        loginRx.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .compose(bindToLifecycle())
                .subscribe(t -> {
                            if (t.isSuccess()) {
                                DisplayCustomToast(getApplicationContext(), t.getData());
                                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                            } else {
                                DisplayCustomToast(getApplicationContext(), t.getData());
                            }
                        },
                        e -> DisplayCustomToast(getApplicationContext(), e.getMessage()));
    }

    @Override
    public void validateFailure(String message) {
        DisplayCustomToast(getApplicationContext(), message);
    }
}