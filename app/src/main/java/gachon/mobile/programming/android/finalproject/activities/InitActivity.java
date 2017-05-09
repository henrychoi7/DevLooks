package gachon.mobile.programming.android.finalproject.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import gachon.mobile.programming.android.finalproject.R;
import gachon.mobile.programming.android.finalproject.utils.BaseActivity;

/**
 * Created by JJSOFT-DESKTOP on 2017-05-09.
 */

public class InitActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_init);

        findViewById(R.id.goRegisterActivity).setOnClickListener(onClickListener);
        findViewById(R.id.goLoginActivity).setOnClickListener(onClickListener);
    }

    /**
     * @param View v
     */
    private Button.OnClickListener onClickListener = v -> {
        switch (v.getId()) {
            case R.id.goRegisterActivity:
                startActivity(new Intent(getApplicationContext(), RegisterActivity.class).addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY));
                break;
            case R.id.goLoginActivity:
                startActivity(new Intent(getApplicationContext(), LoginActivity.class).addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY));
                break;
        }
    };
}
