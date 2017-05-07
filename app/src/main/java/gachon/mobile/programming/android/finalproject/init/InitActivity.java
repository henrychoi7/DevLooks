package gachon.mobile.programming.android.finalproject.init;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import gachon.mobile.programming.android.finalproject.login.LoginActivity;
import gachon.mobile.programming.android.finalproject.R;
import gachon.mobile.programming.android.finalproject.register.RegisterActivity;
import gachon.mobile.programming.android.finalproject.utils.BaseActivity;

public class InitActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_init);
    }

    public void CustomOnClick(View view) {
        switch (view.getId()) {
            case R.id.register:
                startActivity(new Intent(getApplicationContext(), RegisterActivity.class).addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY));
                break;
            case R.id.login:
                startActivity(new Intent(getApplicationContext(), LoginActivity.class).addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY));
                break;
        }
    }
}
