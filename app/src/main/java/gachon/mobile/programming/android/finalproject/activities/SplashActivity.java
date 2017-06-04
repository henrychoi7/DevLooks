package gachon.mobile.programming.android.finalproject.activities;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Bundle;

import gachon.mobile.programming.android.finalproject.R;
import gachon.mobile.programming.android.finalproject.utils.BaseActivity;

import static gachon.mobile.programming.android.finalproject.utils.ApplicationClass.PREF_ID;

/**
 * Created by JJSOFT-DESKTOP on 2017-05-09.
 */

public class SplashActivity extends BaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        new Handler().postDelayed(() -> {
            SharedPreferences sharedPreferences = getSharedPreferences(PREF_ID, Activity.MODE_PRIVATE);
            boolean isCheckedAutoLogin = sharedPreferences.getBoolean("is_checked_auto_login", false);
            if (isCheckedAutoLogin) {
                startActivity(new Intent(getApplicationContext(), LoginActivity.class).addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY));
            } else {
                startActivity(new Intent(getApplicationContext(), InitActivity.class).addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY));
            }
            finish();
        }, 2000);
    }

    @Override
    public void onBackPressed() {
    }

}
