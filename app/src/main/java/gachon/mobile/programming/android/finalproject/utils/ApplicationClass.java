package gachon.mobile.programming.android.finalproject.utils;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Handler;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.widget.AppCompatDrawableManager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.tsengvn.typekit.Typekit;

import java.text.Collator;
import java.util.Comparator;

import gachon.mobile.programming.android.finalproject.R;
import okhttp3.MediaType;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by JJSOFT-DESKTOP on 2017-05-09.
 */

public class ApplicationClass extends Application {
    public static final MediaType MEDIA_TYPE_JSON = MediaType.parse("application/json; charset=utf-8");
    public static final String BASE_URL = "http://13.124.94.238/";
    private static Boolean mIsBackPressedOnce = false;
    public static int HOME_VALUE = 0;
    public static String PREF_ID = "DEV_LOOKS";
    public static int EXPANDABLE_MENU_COUNT = 6;
    public static String STACK_OVERFLOW = "stackOverflow";
    public static String STACK_OVERFLOW_MAIN = "stackOverflowMain";
    public static String OKKY = "okky";
    public static String ON_OFF_MIX = "onOffMix";
    public static String CUSTOM_FONT = "fonts/gyeonggi_ch_title_Medium.ttf";

    public static final Retrofit RETROFIT_BUILDER = new Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .build();

    public static final RetrofitInterface RETROFIT_INTERFACE = RETROFIT_BUILDER.create(RetrofitInterface.class);

    @Override
    public void onCreate() {
        super.onCreate();
        Typekit.getInstance()
                //.addNormal(Typekit.createFromAsset(this, "fonts/BMJUA_ttf.ttf"))
                //.addBold(Typekit.createFromAsset(this, "fonts/BMJUA_ttf.ttf"));
                .addNormal(Typekit.createFromAsset(this, CUSTOM_FONT))
                .addBold(Typekit.createFromAsset(this, CUSTOM_FONT));
    }

    public static void handleUserApplicationExit(Context context, Activity activity) {
        if (mIsBackPressedOnce) {
            activity.finish();
            return;
        }

        mIsBackPressedOnce = true;
        DisplayCustomToast(context, context.getResources().getString(R.string.dismiss_activity));

        new Handler().postDelayed(() -> mIsBackPressedOnce = false, 2000);
    }

    public static void DisplayCustomToast(Context context, String toastText) {
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View customToastLayout = layoutInflater.inflate(R.layout.toast_custom, null);

        TextView customTextView = (TextView) customToastLayout.findViewById(R.id.custom_tv);
        customTextView.setText(toastText);
        customTextView.setTypeface(Typekit.createFromAsset(context, CUSTOM_FONT));
        Toast customToast = new Toast(context);
        customToast.setView(customToastLayout);
        customToast.setDuration(Toast.LENGTH_SHORT);
        customToast.show();
    }

    public static Bitmap getBitmapFromVectorDrawable(Context context, int drawableId) {
        Drawable drawable = AppCompatDrawableManager.get().getDrawable(context, drawableId);
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            drawable = (DrawableCompat.wrap(drawable)).mutate();
        }

        Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(),
                drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);

        return bitmap;
    }
}