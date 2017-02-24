package com.droidsonroids.workcation.common.maps;

import android.util.DisplayMetrics;
import android.view.WindowManager;
import com.droidsonroids.workcation.common.model.Bounds;

public class MapsUtil {
    public static final String MAP_BITMAP_KEY = "map_bitmap_key";
    public static final int DEFAULT_ZOOM = 150;
    private static final double LATITUDE_INCREASE_FACTOR = 1.5;
    public static int DEFAULT_MAP_PADDING = 30;

    public static String increaseLatitude(final Bounds bounds) {
        double southwestLat = bounds.getSouthwest().getLatD();
        double northeastLat = bounds.getNortheast().getLatD();
        double updatedLat = LATITUDE_INCREASE_FACTOR * Math.abs(northeastLat - southwestLat);
        return String.valueOf(southwestLat - updatedLat);
    }

    public static int calculateWidth(final WindowManager windowManager) {
        DisplayMetrics metrics = new DisplayMetrics();
        windowManager.getDefaultDisplay().getMetrics(metrics);
        return metrics.widthPixels;
    }

    public static int calculateHeight(final WindowManager windowManager, final int paddingBottom) {
        DisplayMetrics metrics = new DisplayMetrics();
        windowManager.getDefaultDisplay().getMetrics(metrics);
        return metrics.heightPixels - paddingBottom;
    }


}
