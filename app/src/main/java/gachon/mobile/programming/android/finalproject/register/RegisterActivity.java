package gachon.mobile.programming.android.finalproject.register;

import android.content.Intent;
import android.os.Bundle;

import gachon.mobile.programming.android.finalproject.R;
import gachon.mobile.programming.android.finalproject.init.InitActivity;
import gachon.mobile.programming.android.finalproject.utils.BaseActivity;

public class RegisterActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(getApplicationContext(), InitActivity.class).addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY));
        super.onBackPressed();
    }
}
