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

// Rxandroid Library작업을 위한 RxAppCompatActivity를 상속받은 BaseActivity
public class BaseActivity extends RxAppCompatActivity {

    // ApplicationClass에서 작업한 폰트를 적용하는 작업
    @Override
    protected void attachBaseContext(final Context newBase) {
        super.attachBaseContext(TypekitContextWrapper.wrap(newBase));
    }

    // 권한이 있는지 확인하는 작업
    protected boolean checkPermissionAndSetDisplayData() {
        if (!checkAndExplainPermission(Manifest.permission.INTERNET)) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.INTERNET}, 1);
            return false;
        }
        return true;
    }

    private boolean checkAndExplainPermission(final String requestPermission) {
        /* 권한이 이미 부여되어 있을 수도 있으므로 checkSelfPermission() 메서드를 호출하여
           권한이 부여되어 있는지 체크
           - 현재 앱이 특정 권한을 갖고 있는지 확인 가능
         */
        final int permissionCheck = ContextCompat.checkSelfPermission(this, requestPermission);
        return permissionCheck == PackageManager.PERMISSION_GRANTED;
    }

    @Override
    public void onRequestPermissionsResult(final int requestCode, @NonNull final String[] permissions, @NonNull final int[] grantResults) {
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
