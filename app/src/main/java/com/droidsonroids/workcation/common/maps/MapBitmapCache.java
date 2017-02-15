package com.droidsonroids.workcation.common.maps;

import android.graphics.Bitmap;
import android.util.LruCache;

public class MapBitmapCache extends LruCache<String, Bitmap> {
    private static final int DEFAULT_CACHE_SIZE = (int) (Runtime.getRuntime().maxMemory() / 1024) / 8;
    public static final String KEY = "MAP_BITMAP_KEY";

    private static MapBitmapCache sInstance;
    /**
     * @param maxSize for caches that do not override {@link #sizeOf}, this is
     * the maximum number of entries in the cache. For all other caches,
     * this is the maximum sum of the sizes of the entries in this cache.
     */
    private MapBitmapCache(final int maxSize) {
        super(maxSize);
    }

    public static MapBitmapCache instance() {
        if(sInstance == null) {
            sInstance = new MapBitmapCache(DEFAULT_CACHE_SIZE);
            return sInstance;
        }
        return sInstance;
    }

    public Bitmap getBitmap() {
        return get(KEY);
    }

    public void putBitmap(Bitmap bitmap) {
        put(KEY, bitmap);
    }

    @Override
    protected int sizeOf(String key, Bitmap value) {
        return value == null ? 0 : value.getRowBytes() * value.getHeight() / 1024;
    }
}
