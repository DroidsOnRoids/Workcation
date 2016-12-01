package com.droidsonroids.workcation.common.views;

import android.content.Context;

public class GuiUtils {

    public static float dpToPx(Context ctx, int dp) {
        float density = ctx.getResources().getDisplayMetrics().density;
        return (dp * density);
    }

    public static float pxToDp(Context ctx, int px) {
        float density = ctx.getResources().getDisplayMetrics().density;
        return (px / density);
    }
}
