package gachon.mobile.programming.android.finalproject.activities;

import android.content.Intent;
import android.os.Handler;
import android.os.Bundle;

import gachon.mobile.programming.android.finalproject.R;
import gachon.mobile.programming.android.finalproject.utils.BaseActivity;

/**
 * Created by JJSOFT-DESKTOP on 2017-05-09.
 */

public class SplashActivity extends BaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        new Handler().postDelayed(() -> {
            //TODO-자동로그인 체크 했는지 여부에 따라 바로 메인화면 갈지, 시작화면 갈지 처리 로직 추가해야함.
            /*startActivity(new Intent(getApplicationContext(), LoginActivity.class));
            finish();*/
            startActivity(new Intent(getApplicationContext(), InitActivity.class).addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY));
            //startActivity(new Intent(getApplicationContext(), MainActivity.class).addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY));
            finish();
        }, 2000);
    }

    @Override
    public void onBackPressed() {}

}
