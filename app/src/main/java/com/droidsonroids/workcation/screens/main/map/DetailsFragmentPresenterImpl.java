package com.droidsonroids.workcation.screens.main.map;

import com.droidsonroids.workcation.common.maps.MapsUtil;
import com.droidsonroids.workcation.common.model.BaliDataProvider;
import com.droidsonroids.workcation.common.model.Bounds;
import com.droidsonroids.workcation.common.model.DirectionsResponse;
import com.droidsonroids.workcation.common.model.MapsApiManager;
import com.droidsonroids.workcation.common.model.Route;
import com.droidsonroids.workcation.common.mvp.MvpPresenterImpl;
import com.google.android.gms.maps.model.LatLng;
import com.google.gson.Gson;
import com.google.maps.android.PolyUtil;
import java.io.IOException;
import java.util.ArrayList;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class DetailsFragmentPresenterImpl extends MvpPresenterImpl<DetailsFragmentView> implements DetailsFragmentPresenter {

    private MapsApiManager mapsApiManager = MapsApiManager.instance();
    private BaliDataProvider baliDataProvider = BaliDataProvider.instance();

    @Override
    public void drawRoute(final LatLng first, final int position) {
        final LatLng second = new LatLng(baliDataProvider.getLatByPosition(position), baliDataProvider.getLngByPosition(position));
        mapsApiManager.getRoute(first, second, new Callback() {
            @Override
            public void onFailure(final Call call, final IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(final Call call, final Response response) throws IOException {
                Route route = new Gson().fromJson(response.body().charStream(), DirectionsResponse.class).getRoutes().get(0);
                providePolylineToDraw(route.getOverviewPolyline().getPoints());
                updateMapZoomAndRegion(route.getBounds());
            }
        });
    }

    @Override
    public void provideBaliData() {
        getView().provideBaliData(baliDataProvider.providePlacesList());
    }

    @Override
    public void onBackPressedWithScene() {
        getView().onBackPressedWithScene(baliDataProvider.provideLatLngBoundsForAllPlaces());
    }

    @Override
    public void moveMapAndAddMarker() {
        getView().moveMapAndAddMaker(baliDataProvider.provideLatLngBoundsForAllPlaces());
    }

    private void updateMapZoomAndRegion(final Bounds bounds) {
        bounds.getSouthwest().setLat(MapsUtil.increaseLatitude(bounds));
        getView().updateMapZoomAndRegion(bounds.getNortheastLatLng(), bounds.getSouthwestLatLng());
    }

    private void providePolylineToDraw(final String points) {
        getView().drawPolylinesOnMap(new ArrayList<>(PolyUtil.decode(points)));
    }
}
