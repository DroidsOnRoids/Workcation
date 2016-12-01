package com.droidsonroids.workcation.common.mvp;

import java.lang.ref.SoftReference;

public class MvpPresenterImpl<V extends MvpView> implements MvpPresenter<V> {

    private SoftReference<V> viewReference;

    @Override
    public void attachView(V view) {
        viewReference = new SoftReference<>(view);
        onViewAttached();
    }

    public V getView() {
        return viewReference == null ? null : viewReference.get();
    }

    @Override
    public boolean isViewAttached() {
        return viewReference != null && viewReference.get() != null;
    }

    @Override
    public void detachView() {
        if (viewReference != null) {
            viewReference.clear();
            viewReference = null;
        }
    }

    public void onViewAttached() {

    }
}
