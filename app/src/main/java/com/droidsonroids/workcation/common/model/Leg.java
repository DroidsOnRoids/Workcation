package com.droidsonroids.workcation.common.model;

import com.google.gson.annotations.SerializedName;
import java.util.List;

/**
 * Created by droids on 21.11.2016.
 */
public class Leg {
    @SerializedName("steps") List<Step> steps;
}
