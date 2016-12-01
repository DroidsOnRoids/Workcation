package com.droidsonroids.workcation.common;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.ColorRes;
import android.support.annotation.StringRes;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class ViewUtils {

    public static void setText(TextView textView, String text) {
        new Handler(Looper.getMainLooper()).post(() -> textView.setText(text));
    }

    public static void showView(View view) {
        new Handler(Looper.getMainLooper()).post(() -> view.setVisibility(View.VISIBLE));
    }

    public static void showViews(View... views) {
        new Handler(Looper.getMainLooper()).post(() -> {
            for(View view : views) {
                view.setVisibility(View.VISIBLE);
            }
        });
    }

    public static void goneViews(View... views) {
        new Handler(Looper.getMainLooper()).post(() -> {
            for(View view : views) {
                view.setVisibility(View.GONE);
            }
        });
    }

    public static void invisibleViews(View... views) {
        new Handler(Looper.getMainLooper()).post(() -> {
            for(View view : views) {
                view.setVisibility(View.INVISIBLE);
            }
        });
    }

    public static void goneView(View view) {
        new Handler(Looper.getMainLooper()).post(() -> view.setVisibility(View.GONE));
    }

    public static void invisbleView(View view) {
        new Handler(Looper.getMainLooper()).post(() -> view.setVisibility(View.INVISIBLE));
    }

    public static void setError(EditText editText, String error) {
        new Handler(Looper.getMainLooper()).post(() -> editText.setError(error));
    }

    public static void showKeyboard(Context ctx) {
        InputMethodManager imm = (InputMethodManager) ctx.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
    }

    public static void hideKeyboard(Context ctx) {
        InputMethodManager imm = (InputMethodManager) ctx.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
    }

    public static float dpToPx(float dp, Context context){
        Resources resources = context.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        float px = dp * (metrics.densityDpi / 160f);
        return px;
    }

    public static float pxToDp(float px, Context context){
        Resources resources = context.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        float dp = px / (metrics.densityDpi / 160f);
        return dp;
    }

    public static boolean isVisible(View view) {
        return view.getVisibility() == View.VISIBLE;
    }

    public static void setDrawableColor(ImageView imageView, @ColorRes int colorRes) {
        new Handler(Looper.getMainLooper()).post(() -> {
            Drawable wrappedDrawable = DrawableCompat.wrap(imageView.getDrawable());
            DrawableCompat.setTint(wrappedDrawable, ContextCompat.getColor(imageView.getContext(), colorRes));
            imageView.setImageDrawable(wrappedDrawable);
        });
    }

    public static void removeError(EditText editText) {
        new Handler(Looper.getMainLooper()).post(() -> editText.setError(null));
    }
}
