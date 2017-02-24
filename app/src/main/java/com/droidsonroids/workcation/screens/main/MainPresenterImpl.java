package com.droidsonroids.workcation.screens.main;

import android.graphics.Bitmap;
import com.droidsonroids.workcation.common.maps.MapBitmapCache;
import com.droidsonroids.workcation.common.model.BaliDataProvider;
import com.droidsonroids.workcation.common.mvp.MvpPresenterImpl;

public class MainPresenterImpl extends MvpPresenterImpl<MainView> implements MainPresenter {
    @Override
    public void saveBitmap(final Bitmap bitmap) {
        MapBitmapCache.instance().putBitmap(bitmap);
    }

    @Override
    public void provideMapLatLngBounds() {
        getView().setMapLatLngBounds(BaliDataProvider.instance().provideLatLngBoundsForAllPlaces());
    }
}
