package com.droidsonroids.workcation.screens.main;

import com.droidsonroids.workcation.common.maps.MapBitmapCache;
import com.droidsonroids.workcation.common.maps.MapsUtil;
import com.droidsonroids.workcation.common.mvp.MvpPresenterImpl;
import com.google.android.gms.maps.GoogleMap;

public class MainPresenterImpl extends MvpPresenterImpl<MainView> implements MainPresenter {
    @Override
    public void preloadMap(final GoogleMap googleMap) {
        googleMap.snapshot(bitmap -> MapBitmapCache.instance().put(MapsUtil.MAP_BITMAP_KEY, bitmap));
    }
}
