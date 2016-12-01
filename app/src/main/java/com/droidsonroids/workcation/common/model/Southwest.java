package com.droidsonroids.workcation.common.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by droids on 28.11.2016.
 */

public class Southwest {

    @SerializedName("lat") String lat;
    @SerializedName("lng") String lng;

    public String getLat() {
        return lat;
    }

    public String getLng() {
        return lng;
    }

    public Double getLatD() {
        return Double.parseDouble(lat);
    }

    public void setLat(final String lat) {
        this.lat = lat;
    }
}
