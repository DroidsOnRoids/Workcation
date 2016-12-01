package com.droidsonroids.workcation.screens.main;

import android.os.Bundle;
import android.widget.FrameLayout;
import butterknife.BindView;
import butterknife.OnClick;
import com.droidsonroids.workcation.R;
import com.droidsonroids.workcation.common.maps.MapsUtil;
import com.droidsonroids.workcation.common.model.BaliDataProvider;
import com.droidsonroids.workcation.common.mvp.MvpActivity;
import com.droidsonroids.workcation.common.mvp.MvpFragment;
import com.droidsonroids.workcation.screens.main.home.HomeFragment;
import com.droidsonroids.workcation.screens.main.map.MapFragment;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;

public class MainActivity extends MvpActivity<MainView, MainPresenter> implements MainView, OnMapReadyCallback {

    @Override
    protected MainPresenter createPresenter() {
        return new MainPresenterImpl();
    }

    @Override
    protected int getLayout() {
        return R.layout.activity_main;
    }

    //Lifecycle callbacks
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.container_layout, HomeFragment.newInstance(), HomeFragment.TAG)
                .addToBackStack(HomeFragment.TAG)
                .commit();
        final SupportMapFragment supportMapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.mapview);
        supportMapFragment.getMapAsync(this);
    }

    @OnClick(R.id.item_explore)
    public void onItemExploreClicked() {
        if(getSupportFragmentManager().findFragmentByTag(MapFragment.TAG) == null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right)
                    .replace(R.id.container_layout, MapFragment.newInstance(), MapFragment.TAG)
                    .addToBackStack(MapFragment.TAG)
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
    public void onMapReady(final GoogleMap googleMap) {
        googleMap.setOnMapLoadedCallback(() -> googleMap.moveCamera(CameraUpdateFactory.newLatLngBounds(BaliDataProvider.instance().provideLatLngBoundsForAllPlaces(), MapsUtil.DEFAULT_ZOOM)));
        googleMap.setOnCameraMoveListener(() -> presenter.preloadMap(googleMap));
    }

    private void triggerFragmentBackPress(final int count) {
        ((MvpFragment)getSupportFragmentManager().findFragmentByTag(getSupportFragmentManager().getBackStackEntryAt(count - 1).getName())).onBackPressed();
    }

    public void superOnBackPressed() {
        super.onBackPressed();
    }
}
