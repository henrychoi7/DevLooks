package gachon.mobile.programming.android.finalproject.utils;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.os.Build;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.tsengvn.typekit.Typekit;

import gachon.mobile.programming.android.finalproject.R;

public class ApplicationClass extends Application {

    private static Boolean gIsBackPressedOnce = false;

    @Override
    public void onCreate() {
        super.onCreate();
        Typekit.getInstance()
                .addNormal(Typekit.createFromAsset(this, "fonts/BMJUA_ttf.ttf"))
                .addBold(Typekit.createFromAsset(this, "fonts/BMJUA_ttf.ttf"));
    }

    public static void handleUserApplicationExit(Context context, Activity activity) {
        if (gIsBackPressedOnce) {
            activity.finish();
            return;
        }

        gIsBackPressedOnce = true;
        DisplayCustomToast(context, context.getResources().getString(R.string.dismiss_activity));

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                gIsBackPressedOnce = false;
            }
        }, 2000);
    }

    public static void DisplayCustomToast(Context context, String toastText) {
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View customToastLayout = layoutInflater.inflate(R.layout.toast_custom, null);

        TextView customTextView = (TextView) customToastLayout.findViewById(R.id.custom_tv);
        customTextView.setText(toastText);
        customTextView.setTypeface(Typekit.createFromAsset(context, "fonts/BMJUA_ttf.ttf"));
        Toast customToast = new Toast(context);
        customToast.setView(customToastLayout);
        customToast.setDuration(Toast.LENGTH_SHORT);
        customToast.show();
    }
}