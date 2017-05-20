package gachon.mobile.programming.android.finalproject.utils;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

import com.trello.rxlifecycle2.components.support.RxAppCompatActivity;
import com.tsengvn.typekit.TypekitContextWrapper;

import gachon.mobile.programming.android.finalproject.R;

import static gachon.mobile.programming.android.finalproject.utils.ApplicationClass.DisplayCustomToast;

/**
 * Created by JJSOFT-DESKTOP on 2017-05-09.
 */

public class BaseActivity extends RxAppCompatActivity {
    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(TypekitContextWrapper.wrap(newBase));
    }

    protected boolean checkPermissionAndSetDisplayData() {
        if (!checkAndExplainPermission(Manifest.permission.INTERNET)) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.INTERNET}, 1);
            return false;
        }
        return true;
    }

    private boolean checkAndExplainPermission(String requestPermission) {
        /* 권한이 이미 부여되어 있을 수도 있으므로 checkSelfPermission() 메서드를 호출하여
           권한이 부여되어 있는지 체크
           - 현재 앱이 특정 권한을 갖고 있는지 확인 가능
         */
        int permissionCheck = ContextCompat.checkSelfPermission(this, requestPermission);
        return permissionCheck == PackageManager.PERMISSION_GRANTED;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case 1: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    DisplayCustomToast(getApplicationContext(), "엇 내부로직이다, 인터넷 권한은 얻음");
                } else {
                    DisplayCustomToast(getApplicationContext(), getString(R.string.explain_permission));
                }
                return;
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
}
