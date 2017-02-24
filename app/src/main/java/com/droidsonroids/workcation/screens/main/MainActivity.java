package com.droidsonroids.workcation.screens.main;

import android.os.Bundle;
import butterknife.OnClick;
import com.droidsonroids.workcation.R;
import com.droidsonroids.workcation.common.maps.MapsUtil;
import com.droidsonroids.workcation.common.mvp.MvpActivity;
import com.droidsonroids.workcation.common.mvp.MvpFragment;
import com.droidsonroids.workcation.screens.main.home.HomeFragment;
import com.droidsonroids.workcation.screens.main.map.DetailsFragment;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLngBounds;

public class MainActivity extends MvpActivity<MainView, MainPresenter> implements MainView, OnMapReadyCallback {

    SupportMapFragment mapFragment;
    private LatLngBounds mapLatLngBounds;

    @Override
    protected MainPresenter createPresenter() {
        return new MainPresenterImpl();
    }

    @Override
    protected int getLayout() {
        return R.layout.activity_main;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        presenter.provideMapLatLngBounds();
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.container, HomeFragment.newInstance(), HomeFragment.TAG)
                .addToBackStack(HomeFragment.TAG)
                .commit();
        mapFragment = (SupportMapFragment)getSupportFragmentManager().findFragmentById(R.id.mapFragment);
        mapFragment.getMapAsync(this);
    }

    @OnClick(R.id.explore)
    public void onItemExploreClicked() {
        if(getSupportFragmentManager().findFragmentByTag(DetailsFragment.TAG) == null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right)
                    .replace(R.id.container, DetailsFragment.newInstance(this), DetailsFragment.TAG)
                    .addToBackStack(DetailsFragment.TAG)
                    .commit();
        }
    }

    @Override
    public void onBackPressed() {
        if(getSupportFragmentManager().getBackStackEntryCount() > 1) {
            triggerFragmentBackPress(getSupportFragmentManager().getBackStackEntryCount());
        } else {
            finish();
        }
    }

    @Override
    public void setMapLatLngBounds(final LatLngBounds latLngBounds) {
        mapLatLngBounds = latLngBounds;
    }

    @Override
    public void onMapReady(final GoogleMap googleMap) {
        googleMap.moveCamera(CameraUpdateFactory.newLatLngBounds(
                mapLatLngBounds,
                MapsUtil.calculateWidth(getWindowManager()),
                MapsUtil.calculateHeight(getWindowManager(), getResources().getDimensionPixelSize(R.dimen.map_margin_bottom)), 150));
        googleMap.setOnMapLoadedCallback(() -> googleMap.snapshot(presenter::saveBitmap));
    }

    private void triggerFragmentBackPress(final int count) {
        ((MvpFragment)getSupportFragmentManager().findFragmentByTag(getSupportFragmentManager().getBackStackEntryAt(count - 1).getName())).onBackPressed();
    }

    public void superOnBackPressed() {
        super.onBackPressed();
    }
}
