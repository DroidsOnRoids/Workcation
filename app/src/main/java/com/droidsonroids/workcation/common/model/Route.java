package com.droidsonroids.workcation.common.model;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class Route {

    @SerializedName("legs") List<Leg> legs;
    @SerializedName("overview_polyline") Polyline polyline;
    @SerializedName("bounds") com.droidsonroids.workcation.common.model.Bounds bounds;

    public List<Leg> getLegs() {
        return legs;
    }

    public Bounds getBounds() {
        return bounds;
    }

    public Polyline getOverviewPolyline() {
        return polyline;
    }
}
