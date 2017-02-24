package com.droidsonroids.workcation.screens.main;

import com.droidsonroids.workcation.common.mvp.MvpView;
import com.google.android.gms.maps.model.LatLngBounds;

public interface MainView extends MvpView {
    void setMapLatLngBounds(final LatLngBounds latLngBounds);
}
