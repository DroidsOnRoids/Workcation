package com.droidsonroids.workcation.screens.main;

import com.droidsonroids.workcation.common.mvp.MvpPresenter;
import com.google.android.gms.maps.GoogleMap;

public interface MainPresenter extends MvpPresenter<MainView> {
    void preloadMap(GoogleMap googleMap);
}
