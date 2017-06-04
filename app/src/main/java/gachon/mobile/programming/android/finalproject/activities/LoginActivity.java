package gachon.mobile.programming.android.finalproject.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;

import gachon.mobile.programming.android.finalproject.R;
import gachon.mobile.programming.android.finalproject.presenters.LoginActivityPresenter;
import gachon.mobile.programming.android.finalproject.utils.BaseActivity;
import gachon.mobile.programming.android.finalproject.utils.ClearEditText;
import gachon.mobile.programming.android.finalproject.utils.PasswordEditText;
import gachon.mobile.programming.android.finalproject.views.LoginActivityView;

import static gachon.mobile.programming.android.finalproject.utils.ApplicationClass.DisplayCustomToast;

/**
 * Created by JJSOFT-DESKTOP on 2017-05-09.
 */

public class LoginActivity extends BaseActivity implements LoginActivityView {
    private ClearEditText mEmailView;
    private PasswordEditText mPasswordView;

    private LoginActivityView.UserInteractions mLoginActivityPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        final Toolbar toolbarLogin = (Toolbar) findViewById(R.id.toolbar_login);
        setSupportActionBar(toolbarLogin);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayShowTitleEnabled(false);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        mLoginActivityPresenter = new LoginActivityPresenter(getApplicationContext(), this);

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
        mPasswordView = (PasswordEditText) findViewById(R.id.password);
        TextView passwordTextView = (TextView) findViewById(R.id.password_text_edited);
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
            public void afterTextChanged(Editable s) {
                passwordTextView.setText(String.valueOf(s.length()));
            }
        });

        mEmailView.setText("dngus@dngus.com");
        mPasswordView.setText("dngus2929");

        Button mEmailSignInButton = (Button) findViewById(R.id.email_sign_in_button);
        mEmailSignInButton.setOnClickListener(view -> mLoginActivityPresenter.attemptLogin());
        //mEmailSignInButton.setOnClickListener(view -> startActivity(new Intent(getApplicationContext(), MainActivity.class)));
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(getApplicationContext(), InitActivity.class).addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY));
        super.onBackPressed();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            startActivity(new Intent(getApplicationContext(), InitActivity.class).addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY));
        }

        return super.onOptionsItemSelected(item);
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
        mEmailView.requestFocus();
        mEmailView.setError(message);
    }

    @Override
    public void setPasswordError(String message) {
        mPasswordView.requestFocus();
        mPasswordView.setError(message);
    }

    @Override
    public void validateSuccess() {
        startActivity(new Intent(getApplicationContext(), MainActivity.class));
    }

    @Override
    public void validateFailure(String message) {
        DisplayCustomToast(getApplicationContext(), message);
    }
}