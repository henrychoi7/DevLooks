package gachon.mobile.programming.android.finalproject.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import gachon.mobile.programming.android.finalproject.R;
import gachon.mobile.programming.android.finalproject.utils.BaseActivity;

/**
 * Created by JJSOFT-DESKTOP on 2017-05-09.
 */

public class InitActivity extends BaseActivity implements View.OnClickListener {

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_init);

        findViewById(R.id.goRegisterActivity).setOnClickListener(this);
        findViewById(R.id.goLoginActivity).setOnClickListener(this);
    }

    @Override
    public void onClick(final View v) {
        // 회원가입, 로그인창으로 이동 / FLAG를 통해 스택에 쌓이지 않도록 작업
        switch (v.getId()) {
            case R.id.goRegisterActivity:
                startActivity(new Intent(getApplicationContext(), RegisterActivity.class).addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY));
                break;
            case R.id.goLoginActivity:
                startActivity(new Intent(getApplicationContext(), LoginActivity.class).addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY));
                break;
        }
    }
}
