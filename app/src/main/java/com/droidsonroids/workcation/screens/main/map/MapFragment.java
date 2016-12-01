package com.droidsonroids.workcation.screens.main.map;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.transition.Scene;
import android.view.View;
import android.widget.FrameLayout;
import butterknife.BindView;
import com.droidsonroids.workcation.R;
import com.droidsonroids.workcation.common.maps.WorkcationSupportMapFragment;
import com.droidsonroids.workcation.common.model.Place;
import com.droidsonroids.workcation.common.mvp.MvpFragment;
import com.droidsonroids.workcation.common.transitions.Crossfade;
import com.droidsonroids.workcation.common.transitions.TransitionUtils;
import com.droidsonroids.workcation.common.views.HorizontalRecyclerViewScrollListener;
import com.droidsonroids.workcation.common.views.TranslateItemAnimator;
import com.droidsonroids.workcation.screens.main.MainActivity;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import java.util.List;

public class MapFragment extends MvpFragment<MapFragmentView, MapFragmentPresenter>
        implements MapFragmentView, OnMapReadyCallback, BaliPlacesAdapter.OnPlaceClickListener, HorizontalRecyclerViewScrollListener.OnShowScaleAnimationListener {
    public static final String TAG = MapFragment.class.getSimpleName();

    @BindView(R.id.recyclerview) RecyclerView recyclerView;
    @BindView(R.id.container) FrameLayout containerLayout;

    private List<Place> baliPlaces;
    private WorkcationSupportMapFragment supportMapFragment;
    private BaliPlacesAdapter mBaliAdapter;
    private String currentTransitionName;
    private Scene mDetailsScene;

    public static Fragment newInstance() {
        MapFragment fragment = new MapFragment();
        Crossfade crossfade = new Crossfade();
        crossfade.setFadeBehavior(Crossfade.RESIZE_BEHAVIOR_SCALE);
        fragment.setEnterTransition(crossfade);
        return fragment;
    }

    @Override
    protected MapFragmentPresenter createPresenter() {
        return new MapFragmentPresenterImpl();
    }

    @Override
    public void onBackPressed() {
        if (mDetailsScene != null) {
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
        return R.layout.fragment_map;
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
        supportMapFragment = (WorkcationSupportMapFragment) getChildFragmentManager().findFragmentById(R.id.mapview);
        supportMapFragment.getMapAsync(this);
    }

    private void setupRecyclerView() {
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        mBaliAdapter = new BaliPlacesAdapter(this, getActivity());
    }

    @Override
    public void onMapReady(final GoogleMap googleMap) {
        supportMapFragment.setupMap(googleMap);
        setupGoogleMap();
        addDataToRecyclerView();
    }

    private void setupGoogleMap() {
        presenter.moveMapAndAddMarker();
    }

    private void addDataToRecyclerView() {
        recyclerView.setItemAnimator(new TranslateItemAnimator());
        recyclerView.setAdapter(mBaliAdapter);
        mBaliAdapter.setPlacesList(baliPlaces);
        recyclerView.addOnScrollListener(new HorizontalRecyclerViewScrollListener(baliPlaces.size(), this));
    }

    @Override
    public void onPlaceClicked(final View sharedImageView, final String transitionName, final int position) {
        currentTransitionName = transitionName;
        mDetailsScene = BaliDetailsLayout.showScene(getActivity(), containerLayout, sharedImageView, currentTransitionName, baliPlaces.get(position));
        getRoutePointsAndAnimateMap(position);
        animateMap();
    }

    private void getRoutePointsAndAnimateMap(final int position) {
        presenter.getRoutePoints(supportMapFragment.getCurrentMapLatLng(), position);
    }

    private void animateMap() {
        supportMapFragment.setCameraListenerToNull();
        supportMapFragment.fadeOutPulseViews();
    }

    @Override
    public void drawPolylinesOnMap(final List<LatLng> polylines) {
        getActivity().runOnUiThread(() -> supportMapFragment.drawPolylines(polylines));
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
        supportMapFragment.onBackPressedWithScene(latLngBounds);
        mDetailsScene = null;
    }

    private void notifyLayoutAfterBackPress(final int childPosition) {
        containerLayout.removeAllViews();
        containerLayout.addView(recyclerView);
        recyclerView.requestLayout();
        mBaliAdapter.notifyItemChanged(childPosition);
    }

    @Override
    public void moveMapAndAddMaker(final LatLngBounds latLngBounds) {
        supportMapFragment.moveMapAndAddMarkers(latLngBounds, baliPlaces);
    }

    @Override
    public void updateMapZoomAndRegion(final LatLng northeastLatLng, final LatLng southwestLatLng) {
        getActivity().runOnUiThread(() -> supportMapFragment.updateMapZoomAndRegion(northeastLatLng, southwestLatLng));
    }

    @Override
    public void onShowAnimation(final int position) {
        showScaleAnimation(position);
    }

    private void showScaleAnimation(final int i) {
        supportMapFragment.showPulseView(i);
    }
}
