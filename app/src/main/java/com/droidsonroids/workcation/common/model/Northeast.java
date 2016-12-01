package com.droidsonroids.workcation.common.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by droids on 28.11.2016.
 */

public class Northeast {

    @SerializedName("lat") String lat;
    @SerializedName("lng") String lng;

    public String getLat() {
        return lat;
    }

    public Double getLatD() {
        return Double.parseDouble(lat);
    }

    public String getLng() {
        return lng;
    }
}
