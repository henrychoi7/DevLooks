package gachon.mobile.programming.android.finalproject.activities;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.widget.CheckBox;
import android.widget.TextView;

import gachon.mobile.programming.android.finalproject.R;
import gachon.mobile.programming.android.finalproject.presenters.LoginActivityPresenter;
import gachon.mobile.programming.android.finalproject.utils.BaseActivity;
import gachon.mobile.programming.android.finalproject.utils.ClearEditText;
import gachon.mobile.programming.android.finalproject.utils.PasswordEditText;
import gachon.mobile.programming.android.finalproject.views.LoginActivityView;

import static gachon.mobile.programming.android.finalproject.utils.ApplicationClass.DisplayCustomToast;
import static gachon.mobile.programming.android.finalproject.utils.ApplicationClass.PREF_ID;

public class LoginActivity extends BaseActivity implements LoginActivityView {
    private ClearEditText mEmailView;
    private PasswordEditText mPasswordView;
    private CheckBox mAutoLoginCheckBox;

    private LoginActivityView.UserInteractions mLoginActivityPresenter;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        final Toolbar toolbarLogin = (Toolbar) findViewById(R.id.toolbar_login);
        setSupportActionBar(toolbarLogin);

        final ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayShowTitleEnabled(false);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        mLoginActivityPresenter = new LoginActivityPresenter(getApplicationContext(), this);

        mAutoLoginCheckBox = (CheckBox) findViewById(R.id.auto_login_check_box);

        mEmailView = (ClearEditText) findViewById(R.id.email);
        final TextView emailTextView = (TextView) findViewById(R.id.email_text_edited);
        mEmailView.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(final Editable editable) {
                emailTextView.setText(String.valueOf(editable.length()));
            }
        });
        mPasswordView = (PasswordEditText) findViewById(R.id.password);
        final TextView passwordTextView = (TextView) findViewById(R.id.password_text_edited);
        mPasswordView.setOnEditorActionListener((textView, id, keyEvent) -> {
            mLoginActivityPresenter.attemptLogin();
            return true;
        });
        mPasswordView.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(final Editable editable) {
                passwordTextView.setText(String.valueOf(editable.length()));
            }
        });

        // SharedPreference를 통하여 자동로그인 여부 확인
        final SharedPreferences sharedPreferences = getSharedPreferences(PREF_ID, Activity.MODE_PRIVATE);
        final boolean isCheckedAutoLogin = sharedPreferences.getBoolean("is_checked_auto_login", false);
        mAutoLoginCheckBox.setChecked(isCheckedAutoLogin);

        if (isCheckedAutoLogin) {
            mEmailView.setText(sharedPreferences.getString("email", null));
            mPasswordView.setText(sharedPreferences.getString("password", null));
            mLoginActivityPresenter.attemptLogin();
        }

        findViewById(R.id.email_sign_in_button).setOnClickListener(view -> mLoginActivityPresenter.attemptLogin());
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(getApplicationContext(), InitActivity.class).addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY));
        super.onBackPressed();
    }

    @Override
    public boolean onOptionsItemSelected(final MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            startActivity(new Intent(getApplicationContext(), InitActivity.class).addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY));
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean getAutoLoginCheckBox() {
        return mAutoLoginCheckBox.isChecked();
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
    public void setEmailError(final String message) {
        mEmailView.requestFocus();
        mEmailView.setError(message);
    }

    @Override
    public void setPasswordError(final String message) {
        mPasswordView.requestFocus();
        mPasswordView.setError(message);
    }

    @Override
    public void validateSuccess() {
        startActivity(new Intent(getApplicationContext(), MainActivity.class));
    }

    @Override
    public void validateFailure(final String message) {
        DisplayCustomToast(getApplicationContext(), message);
    }
}