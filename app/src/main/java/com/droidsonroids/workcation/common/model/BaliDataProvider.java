package com.droidsonroids.workcation.common.model;

import android.content.res.AssetManager;
import com.droidsonroids.workcation.common.WorkcationApp;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.gson.Gson;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.List;

public class BaliDataProvider {
    private final static String JSON_PATH = "bali.json";

    private static BaliDataProvider sInstance;

    private BaliData mBaliData;

    private BaliDataProvider() {}

    public static BaliDataProvider instance() {
        if(sInstance == null) {
            sInstance = new BaliDataProvider();
            return sInstance;
        }
        return sInstance;
    }

    public void initialize() {
        try {
            AssetManager assetManager = WorkcationApp.getInstance().getAssets();
            InputStream inputStream;

            inputStream = assetManager.open(JSON_PATH);

            Gson gson = new Gson();
            Reader reader = new InputStreamReader(inputStream);

            mBaliData = gson.fromJson(reader, BaliData.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public LatLngBounds provideLatLngBoundsForAllPlaces() {
        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        for(Place place : mBaliData.getPlacesList()) {
            builder.include(new LatLng(place.getLat(), place.getLng()));
        }
        return builder.build();
    }

    public List<Place> providePlacesList() {
        return mBaliData.getPlacesList();
    }

    public double getLatByPosition(final int position) {
        return mBaliData.getPlacesList().get(position).getLat();
    }

    public double getLngByPosition(final int position) {
        return mBaliData.getPlacesList().get(position).getLng();
    }
}
