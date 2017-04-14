package com.droidsonroids.workcation.screens.main.map;

import com.droidsonroids.workcation.common.mvp.MvpPresenter;
import com.google.android.gms.maps.model.LatLng;

public interface DetailsFragmentPresenter extends MvpPresenter<DetailsFragmentView> {

    void drawRoute(LatLng first, final int position);

    void provideBaliData();

    void onBackPressedWithScene();

    void moveMapAndAddMarker();
}
