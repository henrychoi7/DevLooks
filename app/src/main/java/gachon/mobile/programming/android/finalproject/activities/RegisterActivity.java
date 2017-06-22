package gachon.mobile.programming.android.finalproject.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.widget.TextView;

import gachon.mobile.programming.android.finalproject.R;
import gachon.mobile.programming.android.finalproject.presenters.RegisterActivityPresenter;
import gachon.mobile.programming.android.finalproject.utils.BaseActivity;
import gachon.mobile.programming.android.finalproject.utils.ClearEditText;
import gachon.mobile.programming.android.finalproject.utils.PasswordEditText;
import gachon.mobile.programming.android.finalproject.views.RegisterActivityView;

import static gachon.mobile.programming.android.finalproject.utils.ApplicationClass.DisplayCustomToast;

public class RegisterActivity extends BaseActivity implements RegisterActivityView {
    private ClearEditText mRegisterUsernameView;
    private ClearEditText mRegisterEmailView;
    private PasswordEditText mRegisterPasswordView;
    private PasswordEditText mRegisterPasswordConfirmView;
    private ClearEditText mRegisterPhoneView;
    private RegisterActivityView.UserInteractions mRegisterActivityPresenter;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        final Toolbar toolbarRegister = (Toolbar) findViewById(R.id.toolbar_register);
        setSupportActionBar(toolbarRegister);

        final ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayShowTitleEnabled(false);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        mRegisterActivityPresenter = new RegisterActivityPresenter(RegisterActivity.this, this);

        mRegisterUsernameView = (ClearEditText) findViewById(R.id.username);
        final TextView usernameTextView = (TextView) findViewById(R.id.username_text_edited);
        mRegisterUsernameView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(final Editable editable) {
                usernameTextView.setText(String.valueOf(editable.length()));
            }
        });

        mRegisterEmailView = (ClearEditText) findViewById(R.id.email);
        final TextView emailTextView = (TextView) findViewById(R.id.email_text_edited);
        mRegisterEmailView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(final Editable editable) {
                emailTextView.setText(String.valueOf(editable.length()));
            }
        });

        mRegisterPasswordView = (PasswordEditText) findViewById(R.id.password);
        final TextView passwordTextView = (TextView) findViewById(R.id.password_text_edited);

        mRegisterPasswordView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                passwordTextView.setText(String.valueOf(editable.length()));
            }
        });

        mRegisterPasswordConfirmView = (PasswordEditText) findViewById(R.id.password_confirm);
        final TextView passwordConfirmTextView = (TextView) findViewById(R.id.password_confirm_text_edited);

        mRegisterPasswordConfirmView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                passwordConfirmTextView.setText(String.valueOf(editable.length()));
            }
        });

        mRegisterPhoneView = (ClearEditText) findViewById(R.id.phone);
        final TextView phoneTextView = (TextView) findViewById(R.id.phone_text_edited);
        mRegisterPhoneView.setOnEditorActionListener((textView, id, keyEvent) -> {
            mRegisterActivityPresenter.attemptSignUp();
            return true;
        });
        mRegisterPhoneView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(final Editable editable) {
                phoneTextView.setText(String.valueOf(editable.length()));
            }
        });

        // 회원가입 버튼을 통한 서버와의 통신
        findViewById(R.id.register_sign_up_button).setOnClickListener(view -> mRegisterActivityPresenter.attemptSignUp());
    }

    @Override
    public void showProgressDialog(final ProgressDialog subscribeProgressDialog) {
        subscribeProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        subscribeProgressDialog.setMessage(getResources().getString(R.string.loading));
        subscribeProgressDialog.setCancelable(false);
        subscribeProgressDialog.show();
    }

    @Override
    public void dismissProgressDialog(final ProgressDialog subscribeProgressDialog) {
        subscribeProgressDialog.dismiss();
    }

    @Override
    public String getUsername() {
        return TextUtils.isEmpty(mRegisterUsernameView.getText()) ? "" : mRegisterUsernameView.getText().toString();
    }

    @Override
    public String getEmail() {
        return TextUtils.isEmpty(mRegisterEmailView.getText()) ? "" : mRegisterEmailView.getText().toString();
    }

    @Override
    public String getPassword() {
        return TextUtils.isEmpty(mRegisterPasswordView.getText()) ? "" : mRegisterPasswordView.getText().toString();
    }

    @Override
    public String getPasswordConfirm() {
        return TextUtils.isEmpty(mRegisterPasswordConfirmView.getText()) ? "" : mRegisterPasswordConfirmView.getText().toString();
    }

    @Override
    public String getPhone() {
        return TextUtils.isEmpty(mRegisterPhoneView.getText()) ? "" : mRegisterPhoneView.getText().toString();
    }

    @Override
    public void setUsernameError(final String message) {
        mRegisterUsernameView.requestFocus();
        mRegisterUsernameView.setError(message);
    }

    @Override
    public void setEmailError(final String message) {
        mRegisterEmailView.requestFocus();
        mRegisterEmailView.setError(message);
    }

    @Override
    public void setPasswordError(final String message) {
        mRegisterPasswordView.requestFocus();
        mRegisterPasswordView.setError(message);
    }

    @Override
    public void setPasswordConfirmError(final String message) {
        mRegisterPasswordConfirmView.requestFocus();
        mRegisterPasswordConfirmView.setError(message);
    }

    @Override
    public void setPhoneError(final String message) {
        mRegisterPhoneView.requestFocus();
        mRegisterPhoneView.setError(message);
    }

    @Override
    public void validateSuccess() {
        DisplayCustomToast(getApplicationContext(), getString(R.string.complete_to_sign_up));
        startActivity(new Intent(getApplicationContext(), LoginActivity.class).addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY));
    }

    @Override
    public void validateFailure(final String message) {
        DisplayCustomToast(getApplicationContext(), message);
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
}