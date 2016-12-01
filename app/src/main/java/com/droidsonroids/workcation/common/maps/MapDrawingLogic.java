package com.droidsonroids.workcation.common.maps;

import com.google.android.gms.maps.GoogleMap;

/**
 * Created by droids on 01.12.2016.
 */

public class MapDrawingLogic {

    private final GoogleMap googleMap;

    public MapDrawingLogic(GoogleMap googleMap) {
        this.googleMap = googleMap;
        googleMap.getUiSettings().setScrollGesturesEnabled(false);
    }
}
