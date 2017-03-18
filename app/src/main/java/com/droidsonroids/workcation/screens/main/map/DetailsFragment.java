package com.droidsonroids.workcation.screens.main.map;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.transition.Scene;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import butterknife.BindView;
import com.droidsonroids.workcation.R;
import com.droidsonroids.workcation.common.maps.MapBitmapCache;
import com.droidsonroids.workcation.common.maps.PulseOverlayLayout;
import com.droidsonroids.workcation.common.model.Place;
import com.droidsonroids.workcation.common.mvp.MvpFragment;
import com.droidsonroids.workcation.common.transitions.ScaleDownImageTransition;
import com.droidsonroids.workcation.common.transitions.TransitionUtils;
import com.droidsonroids.workcation.common.views.HorizontalRecyclerViewScrollListener;
import com.droidsonroids.workcation.common.views.TranslateItemAnimator;
import com.droidsonroids.workcation.screens.main.MainActivity;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import java.util.ArrayList;
import java.util.List;

public class DetailsFragment extends MvpFragment<DetailsFragmentView, DetailsFragmentPresenter>
        implements DetailsFragmentView, OnMapReadyCallback, BaliPlacesAdapter.OnPlaceClickListener, HorizontalRecyclerViewScrollListener.OnItemCoverListener {
    public static final String TAG = DetailsFragment.class.getSimpleName();

    @BindView(R.id.recyclerview) RecyclerView recyclerView;
    @BindView(R.id.container) FrameLayout containerLayout;
    @BindView(R.id.mapPlaceholder) ImageView mapPlaceholder;
    @BindView(R.id.mapOverlayLayout) PulseOverlayLayout mapOverlayLayout;

    private List<Place> baliPlaces;
    private BaliPlacesAdapter baliAdapter;
    private String currentTransitionName;
    private Scene detailsScene;

    public static Fragment newInstance(final Context ctx) {
        DetailsFragment fragment = new DetailsFragment();
        ScaleDownImageTransition transition = new ScaleDownImageTransition(ctx, MapBitmapCache.instance().getBitmap());
        transition.addTarget(ctx.getString(R.string.mapPlaceholderTransition));
        transition.setDuration(600);
        fragment.setEnterTransition(transition);
        return fragment;
    }

    @Override
    protected DetailsFragmentPresenter createPresenter() {
        return new DetailsFragmentPresenterImpl();
    }

    @Override
    public void onBackPressed() {
        if (detailsScene != null) {
            presenter.onBackPressedWithScene();
        } else {
            ((MainActivity) getActivity()).superOnBackPressed();
        }
    }

    private View getSharedViewByPosition(final int childPosition) {
        for (int i = 0; i < recyclerView.getChildCount(); i++) {
            if (childPosition == recyclerView.getChildAdapterPosition(recyclerView.getChildAt(i))) {
                return recyclerView.getChildAt(i);
            }
        }
        return null;
    }

    @Override
    public int getLayout() {
        return R.layout.fragment_details;
    }

    @Override
    public void onViewCreated(final View view, @Nullable final Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setupBaliData();
        setupMapFragment();
        setupRecyclerView();
    }

    private void setupBaliData() {
        presenter.provideBaliData();
    }

    private void setupMapFragment() {
        ((SupportMapFragment)getChildFragmentManager().findFragmentById(R.id.mapFragment)).getMapAsync(this);
    }

    private void setupRecyclerView() {
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        baliAdapter = new BaliPlacesAdapter(this, getActivity());
    }

    @Override
    public void onMapReady(final GoogleMap googleMap) {
        mapOverlayLayout.setupMap(googleMap);
        setupGoogleMap();
        addDataToRecyclerView();
    }

    private void setupGoogleMap() {
        presenter.moveMapAndAddMarker();
    }

    private void addDataToRecyclerView() {
        recyclerView.setItemAnimator(new TranslateItemAnimator());
        recyclerView.setAdapter(baliAdapter);
        baliAdapter.setPlacesList(baliPlaces);
        recyclerView.addOnScrollListener(new HorizontalRecyclerViewScrollListener(this));
    }

    @Override
    public void onPlaceClicked(final View sharedImageView, final String transitionName, final int position) {
        currentTransitionName = transitionName;
        detailsScene = BaliDetailsLayout.showScene(getActivity(), containerLayout, sharedImageView, currentTransitionName, baliPlaces.get(position));
        getRoutePointsAndAnimateMap(position);
        animateMap();
    }

    private void getRoutePointsAndAnimateMap(final int position) {
        presenter.getRoutePoints(mapOverlayLayout.getCurrentLatLng(), position);
    }

    private void animateMap() {
        mapOverlayLayout.setOnCameraIdleListener(null);
        mapOverlayLayout.hideAllMarkers();
    }

    @Override
    public void drawPolylinesOnMap(final ArrayList<LatLng> polylines) {
        getActivity().runOnUiThread(() -> mapOverlayLayout.addPolyline(polylines));
    }

    @Override
    public void provideBaliData(final List<Place> places) {
        baliPlaces = places;
    }

    @Override
    public void onBackPressedWithScene(final LatLngBounds latLngBounds) {
        int childPosition = TransitionUtils.getItemPositionFromTransition(currentTransitionName);
        BaliDetailsLayout.hideScene(getActivity(), containerLayout, getSharedViewByPosition(childPosition), currentTransitionName);
        notifyLayoutAfterBackPress(childPosition);
        mapOverlayLayout.onBackPressed(latLngBounds);
        detailsScene = null;
    }

    private void notifyLayoutAfterBackPress(final int childPosition) {
        containerLayout.removeAllViews();
        containerLayout.addView(recyclerView);
        recyclerView.requestLayout();
        baliAdapter.notifyItemChanged(childPosition);
    }

    @Override
    public void moveMapAndAddMaker(final LatLngBounds latLngBounds) {
        mapOverlayLayout.moveCamera(latLngBounds);
        mapOverlayLayout.setOnCameraIdleListener(() -> {
            for (int i = 0; i < baliPlaces.size(); i++) {
                mapOverlayLayout.createAndShowMarker(i, baliPlaces.get(i).getLatLng());
            }
            mapOverlayLayout.setOnCameraIdleListener(null);
        });
        mapOverlayLayout.setOnCameraMoveListener(mapOverlayLayout::refresh);
    }

    @Override
    public void updateMapZoomAndRegion(final LatLng northeastLatLng, final LatLng southwestLatLng) {
        getActivity().runOnUiThread(() -> {
            mapOverlayLayout.animateCamera(new LatLngBounds(southwestLatLng, northeastLatLng));
            mapOverlayLayout.setOnCameraIdleListener(() -> mapOverlayLayout.drawStartAndFinishMarker());
        });
    }

    @Override
    public void onItemCover(final int position) {
        mapOverlayLayout.showMarker(position);
    }
}
