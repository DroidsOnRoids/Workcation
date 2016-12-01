package com.droidsonroids.workcation.common.utils;

import android.content.Context;
import android.util.DisplayMetrics;

public class DimenUtils {

    public static float dpToPx(Context ctx, float dp) {
        DisplayMetrics metrics = ctx.getResources().getDisplayMetrics();
        float px = dp * ((float)metrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT);
        return px;
    }

    public static float pxToDp(Context ctx, float px) {
        DisplayMetrics metrics = ctx.getResources().getDisplayMetrics();
        float dp = px / ((float)metrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT);
        return dp;
    }
}
