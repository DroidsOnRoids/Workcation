package com.droidsonroids.workcation.common.maps;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.droidsonroids.workcation.common.model.Place;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import java.util.ArrayList;
import java.util.List;

public class MapFragment extends SupportMapFragment {

    public PulseWrapperLayout mapWrapperLayout;

    @Override
    public View onCreateView(final LayoutInflater layoutInflater, final ViewGroup viewGroup, final Bundle bundle) {
        View originalContentView = super.onCreateView(layoutInflater, viewGroup, bundle);
        mapWrapperLayout = new PulseWrapperLayout(getActivity());
        mapWrapperLayout.addView(originalContentView);
        return mapWrapperLayout;
    }

    public void updateZoomAndRegion(final LatLng northEast, final LatLng southWest) {
        mapWrapperLayout.animateCamera(new LatLngBounds(southWest, northEast));
        mapWrapperLayout.setOnCameraIdleListener(() -> mapWrapperLayout.drawStartAndFinishMarker());
    }

    public void drawPolylines(final ArrayList<LatLng> polylines) {
        getActivity().runOnUiThread(() -> mapWrapperLayout.addPolyline(polylines));
    }

    public void moveMap(final LatLngBounds latLngBounds) {
        mapWrapperLayout.moveCamera(latLngBounds);
    }

    public void addMarkers(final List<Place> baliPlaces) {
        mapWrapperLayout.setOnCameraIdleListener(() -> {
            for (int i = 0; i < baliPlaces.size(); i++) {
                mapWrapperLayout.createMarker(i, baliPlaces.get(i).getLatLng());
            }
            mapWrapperLayout.setOnCameraIdleListener(null);
        });
        mapWrapperLayout.setOnCameraMoveListener(mapWrapperLayout::refresh);
    }

    public void showMarker(final int position) {
        mapWrapperLayout.showMarker(position);
    }

    public void onBackPressedWithScene(final LatLngBounds latLngBounds) {
        mapWrapperLayout.moveCamera(latLngBounds);
        removeStartAndFinishMarkers();
        mapWrapperLayout.removeCurrentPolyline();
        mapWrapperLayout.showAllMarkers();
        mapWrapperLayout.refresh();
    }

    private void removeStartAndFinishMarkers() {
        mapWrapperLayout.removeStartMarker();
        mapWrapperLayout.removeFinishMarker();
    }

    public LatLng getCurrentMapLatLng() {
        return mapWrapperLayout.getCurrentLatLng();
    }

    public void setCameraListenerToNull() {
        mapWrapperLayout.setOnCameraIdleListener(null);
    }

    public void setupMap(final GoogleMap googleMap) {
        mapWrapperLayout.setupMap(googleMap);
    }

    public void hideMarkers() {
        mapWrapperLayout.hideAllMarkers();
    }
}
