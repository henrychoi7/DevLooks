package gachon.mobile.programming.android.finalproject.activities;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Bundle;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.ImageView;

import gachon.mobile.programming.android.finalproject.R;
import gachon.mobile.programming.android.finalproject.utils.BaseActivity;

import static gachon.mobile.programming.android.finalproject.utils.ApplicationClass.PREF_ID;

/**
 * Created by JJSOFT-DESKTOP on 2017-05-09.
 */

public class SplashActivity extends BaseActivity {
    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        // 이미지뷰에 애니메이션 작업
        bindLogo();
        // 2초의 딜레이 후에 자동로그인 여부 확인
        new Handler().postDelayed(() -> {
            final SharedPreferences sharedPreferences = getSharedPreferences(PREF_ID, Activity.MODE_PRIVATE);
            final boolean isCheckedAutoLogin = sharedPreferences.getBoolean("is_checked_auto_login", false);
            if (isCheckedAutoLogin) {
                startActivity(new Intent(getApplicationContext(), LoginActivity.class).addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY));
            } else {
                startActivity(new Intent(getApplicationContext(), InitActivity.class).addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY));
            }
            finish();
        }, 2000);
    }

    private void bindLogo(){
        // Start animating the image
        final ImageView splash = (ImageView) findViewById(R.id.iv_splash_logo);
        final AlphaAnimation animation1 = new AlphaAnimation(0.2f, 1.0f);
        animation1.setDuration(700);
        final AlphaAnimation animation2 = new AlphaAnimation(1.0f, 0.2f);
        animation2.setDuration(700);
        animation1.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationEnd(final Animation arg0) {
                splash.startAnimation(animation2);
            }
            @Override
            public void onAnimationRepeat(Animation arg0) {}
            @Override
            public void onAnimationStart(Animation arg0) {}
        });

        animation2.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationEnd(final Animation arg0) {
                splash.startAnimation(animation1);
            }
            @Override
            public void onAnimationRepeat(Animation arg0) {}
            @Override
            public void onAnimationStart(Animation arg0) {}
        });

        splash.startAnimation(animation1);
    }

    @Override
    public void onBackPressed() {
    }

}
