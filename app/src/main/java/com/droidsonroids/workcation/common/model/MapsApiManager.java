package com.droidsonroids.workcation.common.model;

import android.util.Log;
import com.google.android.gms.maps.model.LatLng;
import com.google.gson.Gson;
import java.io.IOException;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;

public class MapsApiManager {

    private static MapsApiManager sInstance;
    private OkHttpClient mClient;

    public static MapsApiManager instance() {
        if(sInstance == null) {
            sInstance = new MapsApiManager();
            return sInstance;
        } else {
            return sInstance;
        }
    }

    private MapsApiManager(){}

    public void initialize() {
        mClient = new OkHttpClient.Builder().addInterceptor(new HttpLoggingInterceptor()).build();
    }

    public void getRoute(final LatLng start, final LatLng end, final Callback callback) {
        HttpUrl.Builder urlBuilder = HttpUrl.parse(RestConstants.BASE_URL).newBuilder();
        urlBuilder.addQueryParameter("origin", start.latitude + "," + start.longitude);
        urlBuilder.addQueryParameter("destination", end.latitude + "," + end.longitude);

        String url = urlBuilder.build().toString();

        Request request = new Request.Builder().url(url).build();

        mClient.newCall(request).enqueue(callback);
    }
}
