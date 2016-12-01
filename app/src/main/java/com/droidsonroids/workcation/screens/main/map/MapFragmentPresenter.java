package com.droidsonroids.workcation.screens.main.map;

import com.droidsonroids.workcation.common.model.Bounds;
import com.droidsonroids.workcation.common.mvp.MvpPresenter;
import com.google.android.gms.maps.model.LatLng;

public interface MapFragmentPresenter extends MvpPresenter<MapFragmentView> {

    void getRoutePoints(LatLng first, final int position);

    void provideBaliData();

    void onBackPressedWithScene();

    void moveMapAndAddMarker();
}
