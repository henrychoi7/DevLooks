package gachon.mobile.programming.android.finalproject.activities;

import android.content.Intent;
import android.os.Bundle;

import gachon.mobile.programming.android.finalproject.R;
import gachon.mobile.programming.android.finalproject.utils.BaseActivity;

public class SearchActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(getApplicationContext(), MainActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
        super.onBackPressed();
    }
}
