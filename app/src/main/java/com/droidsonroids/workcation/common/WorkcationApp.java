package com.droidsonroids.workcation.common;

import android.app.Application;
import com.droidsonroids.workcation.R;
import com.droidsonroids.workcation.common.maps.MapBitmapCache;
import com.droidsonroids.workcation.common.model.BaliDataProvider;
import com.droidsonroids.workcation.common.model.MapsApiManager;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;

public class WorkcationApp extends Application {

    private static WorkcationApp sInstance;

    @Override
    public void onCreate() {
        super.onCreate();
        sInstance = this;
        MapsApiManager.instance().initialize();
        BaliDataProvider.instance().initialize();
        initCalligraphy();
    }

    private void initCalligraphy() {
        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/Roboto-RobotoRegular.ttf")
                .setFontAttrId(R.attr.fontPath)
                .build()
        );
    }

    public static WorkcationApp getInstance() {
        return sInstance;
    }
}
