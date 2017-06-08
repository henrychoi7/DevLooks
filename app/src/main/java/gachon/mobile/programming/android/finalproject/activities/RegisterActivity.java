package gachon.mobile.programming.android.finalproject.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.TextView;

import gachon.mobile.programming.android.finalproject.R;
import gachon.mobile.programming.android.finalproject.presenters.RegisterActivityPresenter;
import gachon.mobile.programming.android.finalproject.utils.BaseActivity;
import gachon.mobile.programming.android.finalproject.utils.ClearEditText;
import gachon.mobile.programming.android.finalproject.utils.PasswordEditText;
import gachon.mobile.programming.android.finalproject.views.RegisterActivityView;

import static gachon.mobile.programming.android.finalproject.utils.ApplicationClass.DisplayCustomToast;

/**
 * Created by henryman on 05/06/2017.
 */

public class RegisterActivity extends BaseActivity implements RegisterActivityView {
    private ClearEditText mRegisterUsernameView;
    private ClearEditText mRegisterEmailView;
    private PasswordEditText mRegisterPasswordView;
    private PasswordEditText mRegisterPasswordConfirmView;
    private ClearEditText mRegisterPhoneView;
    private RegisterActivityView.UserInteractions mRegisterActivityPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        final Toolbar toolbarRegister = (Toolbar) findViewById(R.id.toolbar_register);
        setSupportActionBar(toolbarRegister);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayShowTitleEnabled(false);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        mRegisterActivityPresenter = new RegisterActivityPresenter(getApplicationContext(), this);

        mRegisterUsernameView = (ClearEditText) findViewById(R.id.username);
        TextView usernameTextView = (TextView) findViewById(R.id.username_text_edited);
        mRegisterUsernameView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                usernameTextView.setText(String.valueOf(s.length()));
            }
        });

        mRegisterEmailView = (ClearEditText) findViewById(R.id.email);
        TextView emailTextView = (TextView) findViewById(R.id.email_text_edited);
        mRegisterEmailView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                emailTextView.setText(String.valueOf(s.length()));
            }
        });

        mRegisterPasswordView = (PasswordEditText) findViewById(R.id.password);
        TextView passwordTextView = (TextView) findViewById(R.id.password_text_edited);

        mRegisterPasswordView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                passwordTextView.setText(String.valueOf(s.length()));
            }
        });

        mRegisterPasswordConfirmView = (PasswordEditText) findViewById(R.id.password_confirm);
        TextView passwordconfirmTextView = (TextView) findViewById(R.id.password_confirm_text_edited);

        mRegisterPasswordConfirmView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                passwordconfirmTextView.setText(String.valueOf(s.length()));
            }
        });

        mRegisterPhoneView = (ClearEditText) findViewById(R.id.phone);
        TextView phoneTextView = (TextView) findViewById(R.id.phone_text_edited);
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
            public void afterTextChanged(Editable s) {
                phoneTextView.setText(String.valueOf(s.length()));
            }
        });

        Button mRegisterSignUpButton = (Button) findViewById(R.id.register_sign_up_button);
        mRegisterSignUpButton.setOnClickListener(view -> mRegisterActivityPresenter.attemptSignUp());
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(getApplicationContext(), InitActivity.class).addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY));
        super.onBackPressed();
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
    public void setUsernameError(String message) {
        mRegisterUsernameView.requestFocus();
        mRegisterUsernameView.setError(message);
    }

    @Override
    public void setEmailError(String message) {
        mRegisterEmailView.requestFocus();
        mRegisterEmailView.setError(message);
    }

    @Override
    public void setPasswordError(String message) {
        mRegisterPasswordView.requestFocus();
        mRegisterPasswordView.setError(message);
    }

    @Override
    public void setPasswordConfirmError(String message) {
        mRegisterPasswordConfirmView.requestFocus();
        mRegisterPasswordConfirmView.setError(message);
    }

    @Override
    public void setPhoneError(String message) {
        mRegisterPhoneView.requestFocus();
        mRegisterPhoneView.setError(message);
    }

    @Override
    public void validateSuccess() {
        startActivity(new Intent(getApplicationContext(), LoginActivity.class));
    }

    @Override
    public void validateFailure(String message) {
        DisplayCustomToast(getApplicationContext(), message);
    }
}