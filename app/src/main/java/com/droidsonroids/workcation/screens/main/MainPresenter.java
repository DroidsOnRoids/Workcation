package com.droidsonroids.workcation.screens.main;

import android.graphics.Bitmap;
import com.droidsonroids.workcation.common.mvp.MvpPresenter;

public interface MainPresenter extends MvpPresenter<MainView> {
    void saveBitmap(Bitmap googleMap);

    void provideMapLatLngBounds();
}
