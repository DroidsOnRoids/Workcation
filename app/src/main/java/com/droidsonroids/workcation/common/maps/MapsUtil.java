package com.droidsonroids.workcation.common.maps;

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
}
