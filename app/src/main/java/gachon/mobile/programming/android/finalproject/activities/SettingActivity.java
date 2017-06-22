package gachon.mobile.programming.android.finalproject.activities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.widget.TextView;

import gachon.mobile.programming.android.finalproject.R;

import com.yarolegovich.lovelydialog.LovelyTextInputDialog;

import gachon.mobile.programming.android.finalproject.models.UserInfoData;
import gachon.mobile.programming.android.finalproject.presenters.SettingActivityPresenter;
import gachon.mobile.programming.android.finalproject.utils.BaseActivity;
import gachon.mobile.programming.android.finalproject.utils.ClearEditText;
import gachon.mobile.programming.android.finalproject.utils.PasswordEditText;
import gachon.mobile.programming.android.finalproject.views.SettingActivityView;

import static gachon.mobile.programming.android.finalproject.utils.ApplicationClass.DisplayCustomToast;
import static gachon.mobile.programming.android.finalproject.utils.ApplicationClass.PREF_ID;
import static gachon.mobile.programming.android.finalproject.utils.ApplicationClass.clearPreferencesData;

public class SettingActivity extends BaseActivity implements SettingActivityView {
    private SharedPreferences mSharedPreferences;

    private ClearEditText mSettingUsernameView;
    private ClearEditText mSettingEmailView;
    private PasswordEditText mSettingOriginalPasswordView;
    private PasswordEditText mSettingNewPasswordView;
    private PasswordEditText mSettingNewPasswordConfirmView;
    private ClearEditText mSettingPhoneView;
    private SettingActivityView.UserInteractions mSettingActivityPresenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        final Toolbar toolbarRegister = (Toolbar) findViewById(R.id.toolbar_setting);
        setSupportActionBar(toolbarRegister);

        final ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayShowTitleEnabled(false);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        mSettingActivityPresenter = new SettingActivityPresenter(SettingActivity.this, this);
        mSharedPreferences = getSharedPreferences(PREF_ID, Activity.MODE_PRIVATE);
        final String email = mSharedPreferences.getString("email", null);
        if (email == null) {
            showCustomToast("사용자 정보를 불러오는데 실패하였습니다.\n다시 로그인 해주시기 바랍니다.");
            clearPreferencesData(mSharedPreferences);
            startActivity(new Intent(getApplicationContext(), LoginActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK));
        }
        final String userName = mSharedPreferences.getString("name", null);
        final String phoneNumber = mSharedPreferences.getString("phone_number", null);

        mSettingEmailView = (ClearEditText) findViewById(R.id.setting_email);
        mSettingEmailView.setText(email);
        final TextView emailTextView = (TextView) findViewById(R.id.setting_email_text_edited);
        emailTextView.setText(String.valueOf(email.length()));
        mSettingEmailView.addTextChangedListener(new TextWatcher() {
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

        mSettingOriginalPasswordView = (PasswordEditText) findViewById(R.id.setting_original_password);
        final TextView originalPasswordTextView = (TextView) findViewById(R.id.setting_original_password_text_edited);
        mSettingOriginalPasswordView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                originalPasswordTextView.setText(String.valueOf(editable.length()));
            }
        });

        mSettingNewPasswordView = (PasswordEditText) findViewById(R.id.setting_new_password);
        final TextView newPasswordTextView = (TextView) findViewById(R.id.setting_new_password_text_edited);
        mSettingNewPasswordView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                newPasswordTextView.setText(String.valueOf(editable.length()));
            }
        });

        mSettingNewPasswordConfirmView = (PasswordEditText) findViewById(R.id.setting_new_password_confirm);
        final TextView newPasswordConfirmTextView = (TextView) findViewById(R.id.setting_new_password_confirm_text_edited);
        mSettingNewPasswordConfirmView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                newPasswordConfirmTextView.setText(String.valueOf(editable.length()));
            }
        });

        mSettingUsernameView = (ClearEditText) findViewById(R.id.setting_username);
        mSettingUsernameView.setText(userName);
        final TextView usernameTextView = (TextView) findViewById(R.id.setting_username_text_edited);
        usernameTextView.setText(String.valueOf(userName.length()));
        mSettingUsernameView.addTextChangedListener(new TextWatcher() {
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

        mSettingPhoneView = (ClearEditText) findViewById(R.id.setting_phone);
        mSettingPhoneView.setText(phoneNumber);
        final TextView phoneTextView = (TextView) findViewById(R.id.setting_phone_text_edited);
        phoneTextView.setText(String.valueOf(phoneNumber.length()));
        mSettingPhoneView.addTextChangedListener(new TextWatcher() {
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

        mSettingPhoneView.setOnEditorActionListener((textView, id, keyEvent) -> {
            mSettingActivityPresenter.attemptUpdateInfo();
            return true;
        });

        // 수정하기 버튼과 탈퇴하기 버튼 - 서버와의 통신
        findViewById(R.id.confirm_update_info).setOnClickListener(view -> mSettingActivityPresenter.attemptUpdateInfo());
        findViewById(R.id.confirm_delete_info).setOnClickListener(view -> new LovelyTextInputDialog(this)
                .setTopColorRes(R.color.colorPrimary)
                .setIcon(R.drawable.ic_clear_white_36dp)
                .setTitle(getString(R.string.delete_user_info))
                .setMessage(getString(R.string.for_delete) + email + getString(R.string.required_input_email_text_same))
                .setInputFilter(getString(R.string.unmatched_value), text -> text.matches(email))
                .setConfirmButton(R.string.okay, text -> mSettingActivityPresenter.attemptDeleteInfo())
                .setErrorMessageColor(ContextCompat.getColor(getApplicationContext(), R.color.colorAccent))
                .setNegativeButton(getString(R.string.cancel), null)
                .show());
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
        return TextUtils.isEmpty(mSettingUsernameView.getText()) ? "" : mSettingUsernameView.getText().toString();
    }

    @Override
    public String getEmail() {
        return TextUtils.isEmpty(mSettingEmailView.getText()) ? "" : mSettingEmailView.getText().toString();
    }

    @Override
    public String getOriginalPassword() {
        return TextUtils.isEmpty(mSettingOriginalPasswordView.getText()) ? "" : mSettingOriginalPasswordView.getText().toString();
    }

    @Override
    public String getNewPassword() {
        return TextUtils.isEmpty(mSettingNewPasswordView.getText()) ? "" : mSettingNewPasswordView.getText().toString();
    }

    @Override
    public String getNewPasswordConfirm() {
        return TextUtils.isEmpty(mSettingNewPasswordConfirmView.getText()) ? "" : mSettingNewPasswordConfirmView.getText().toString();
    }

    @Override
    public String getPhone() {
        return TextUtils.isEmpty(mSettingPhoneView.getText()) ? "" : mSettingPhoneView.getText().toString();
    }

    @Override
    public void setUsernameError(final String message) {
        mSettingUsernameView.requestFocus();
        mSettingUsernameView.setError(message);
    }

    @Override
    public void setEmailError(final String message) {
        mSettingEmailView.requestFocus();
        mSettingEmailView.setError(message);
    }

    @Override
    public void setOriginalPasswordError(final String message) {
        mSettingOriginalPasswordView.requestFocus();
        mSettingOriginalPasswordView.setError(message);
    }

    @Override
    public void setNewPasswordConfirmError(final String message) {
        mSettingNewPasswordConfirmView.requestFocus();
        mSettingNewPasswordConfirmView.setError(message);
    }

    @Override
    public void setPhoneError(final String message) {
        mSettingPhoneView.requestFocus();
        mSettingPhoneView.setError(message);
    }

    @Override
    public void validateAndDeleteSuccess() {
        clearPreferencesData(mSharedPreferences);
        startActivity(new Intent(getApplicationContext(), LoginActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK));
    }

    @Override
    public void validateAndUpdateSuccess() {
        startActivity(new Intent(getApplicationContext(), MainActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
    }

    @Override
    public void showCustomToast(final String message) {
        DisplayCustomToast(getApplicationContext(), message);
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(getApplicationContext(), MainActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
        super.onBackPressed();
    }

    @Override
    public boolean onOptionsItemSelected(final MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            startActivity(new Intent(getApplicationContext(), MainActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
        }

        return super.onOptionsItemSelected(item);
    }
}
