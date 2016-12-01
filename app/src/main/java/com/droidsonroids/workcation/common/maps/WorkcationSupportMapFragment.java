package com.droidsonroids.workcation.common.maps;

import android.graphics.Color;
import android.graphics.Point;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import com.droidsonroids.workcation.common.model.Place;
import com.droidsonroids.workcation.common.views.PulseView;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import java.util.ArrayList;
import java.util.List;

public class WorkcationSupportMapFragment extends SupportMapFragment {
    private static final int NO_POSITION = -1;

    public View originalContentView;
    public FrameLayout mapWrapperLayout;
    private GoogleMap googleMap;
    private List<LatLng> polylines;
    private Polyline currentPolyline;
    private PulseView startPulseView;
    private PulseView finishPulseView;
    private List<PulseView> pulseViewList = new ArrayList<>();
    private int scaleAnimationDelay = 100;

    @Override
    public View onCreateView(final LayoutInflater layoutInflater, final ViewGroup viewGroup, final Bundle bundle) {
        originalContentView = super.onCreateView(layoutInflater, viewGroup, bundle);
        mapWrapperLayout = new FrameLayout(getActivity());
        mapWrapperLayout.addView(originalContentView);
        return mapWrapperLayout;
    }

    @Override
    public void onViewCreated(final View view, @Nullable final Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Nullable
    @Override
    public View getView() {
        return originalContentView;
    }

    public void updateMapZoomAndRegion(final LatLng northeastLatLng, final LatLng southwestLatLng) {
        int width = mapWrapperLayout.getWidth();
        int height = mapWrapperLayout.getHeight();
        int padding = MapsUtil.DEFAULT_MAP_PADDING;
        getActivity().runOnUiThread(() -> {
            googleMap.animateCamera(CameraUpdateFactory.newLatLngBounds(new LatLngBounds(southwestLatLng, northeastLatLng), width, height, padding));
            googleMap.setOnCameraIdleListener(() -> drawStartAndFinishMarker(polylines.get(0), polylines.get(polylines.size() - 1)));
        });
    }

    public void drawPolylines(final List<LatLng> polylines) {
        this.polylines = polylines;
        PolylineOptions options = new PolylineOptions();
        for(int i = 1; i < polylines.size(); i++) {
            options.add(polylines.get(i -1), polylines.get(i)).width(10).color(Color.RED).geodesic(true);
        }
        currentPolyline = googleMap.addPolyline(options);
    }

    private void drawStartAndFinishMarker(final LatLng start, final LatLng finish) {
        getActivity().runOnUiThread(() -> {
            startPulseView = addMarkerToMapWithoutAnimation(start);
            finishPulseView = addMarkerToMapWithoutAnimation(finish);
        });
        googleMap.setOnCameraIdleListener(null);
    }

    private void addMarkerToMapWithAnimation(final int position, final Place place, final GoogleMap googleMap) {
        MarkerOptions markerOptions = new MarkerOptions().alpha(0).position(new LatLng(place.getLat(), place.getLng()));
        Marker marker = googleMap.addMarker(markerOptions);
        marker.setVisible(false);

        Point point = googleMap.getProjection().toScreenLocation(marker.getPosition());
        PulseView pulseView = createPulseViewAndAddToMap(position, point);
        pulseView.showWithDelay(scaleAnimationDelay);
        scaleAnimationDelay += 70;

    }

    private PulseView addMarkerToMapWithoutAnimation(final LatLng latLng) {
        MarkerOptions markerOptions = new MarkerOptions().alpha(0).position(new LatLng(latLng.latitude, latLng.longitude));
        Marker marker = googleMap.addMarker(markerOptions);
        marker.setVisible(false);

        Point point = googleMap.getProjection().toScreenLocation(marker.getPosition());
        PulseView pulseView = createPulseViewAndAddToMap(NO_POSITION, point);
        pulseView.show();
        scaleAnimationDelay += 70;
        return pulseView;
    }

    @NonNull
    private PulseView createPulseViewAndAddToMap(final int position, final Point point) {
        PulseView pulseView = position < 0 ? new PulseView(getActivity()) : newPulseView(position);
        pulseView.setPulseViewLayoutParams(point);
        mapWrapperLayout.addView(pulseView);
        pulseView.setVisibility(View.GONE);
        pulseViewList.add(pulseView);
        return pulseView;
    }

    private PulseView newPulseView(final int position) {
        PulseView pulseView = new PulseView(getActivity());
        pulseView.setText(Integer.toString(position));
        return pulseView;
    }

    public void moveMapAndAddMarkers(final LatLngBounds latLngBounds, final List<Place> mBaliPlaces) {
        googleMap.moveCamera(CameraUpdateFactory.newLatLngBounds(latLngBounds, 150));
        final GoogleMap.OnCameraIdleListener onCameraIdleListener = () -> {
            for (int i = 0; i < mBaliPlaces.size(); i++) {
                addMarkerToMapWithAnimation(i, mBaliPlaces.get(i), googleMap);
            }
        };
        googleMap.setOnCameraIdleListener(onCameraIdleListener);
    }

    public void showPulseView(final int position) {
        PulseView pulseView = pulseViewList.get(position);
        pulseView.pulse();
    }

    public void onBackPressedWithScene(final LatLngBounds latLngBounds) {
        googleMap.moveCamera(CameraUpdateFactory.newLatLngBounds(latLngBounds, MapsUtil.DEFAULT_ZOOM));
        removeStartAndFinishMarkers();
        for (int i = 0; i < pulseViewList.size(); i++) {
            pulseViewList.get(i).show();
        }
        if(currentPolyline != null ) currentPolyline.remove();
    }

    private void removeStartAndFinishMarkers() {
        mapWrapperLayout.removeView(startPulseView);
        mapWrapperLayout.removeView(finishPulseView);
    }

    public LatLng getCurrentMapLatLng() {
        return new LatLng(googleMap.getCameraPosition().target.latitude, googleMap.getCameraPosition().target.longitude);
    }

    public void fadeOutPulseViews() {
        for (int i = 0; i < pulseViewList.size(); i++) {
            pulseViewList.get(i).hide();
        }
    }

    public void setCameraListenerToNull() {
        googleMap.setOnCameraIdleListener(null);
    }

    public void setupMap(final GoogleMap googleMap) {
        this.googleMap = googleMap;
    }
}
