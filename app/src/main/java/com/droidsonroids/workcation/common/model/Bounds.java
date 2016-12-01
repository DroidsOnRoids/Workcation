package com.droidsonroids.workcation.common.model;

import com.google.android.gms.maps.model.LatLng;
import com.google.gson.annotations.SerializedName;

public class Bounds {

    @SerializedName("northeast") Northeast northeast;
    @SerializedName("southwest") Southwest southwest;

    public LatLng getSouthwestLatLng() {
        return new LatLng(Double.parseDouble(southwest.getLat()), Double.parseDouble(southwest.getLng()));
    }

    public LatLng getNortheastLatLng() {
        return new LatLng(Double.parseDouble(northeast.getLat()), Double.parseDouble(northeast.getLng()));
    }

    public Southwest getSouthwest() {
        return southwest;
    }

    public Northeast getNortheast() {
        return northeast;
    }

}
