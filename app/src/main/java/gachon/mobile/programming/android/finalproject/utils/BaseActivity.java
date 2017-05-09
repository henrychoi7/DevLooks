package gachon.mobile.programming.android.finalproject.utils;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;

import com.trello.rxlifecycle2.components.RxActivity;
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity;
import com.tsengvn.typekit.TypekitContextWrapper;

/**
 * Created by JJSOFT-DESKTOP on 2017-05-09.
 */

public class BaseActivity extends RxAppCompatActivity {
    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(TypekitContextWrapper.wrap(newBase));
    }
}
