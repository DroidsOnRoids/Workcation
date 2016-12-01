package com.droidsonroids.workcation.common.mvp;

import android.os.Bundle;
import android.support.annotation.LayoutRes;
import butterknife.ButterKnife;
import com.droidsonroids.workcation.common.SuperActivity;

public abstract class MvpActivity<V extends MvpView, P extends MvpPresenter> extends SuperActivity implements MvpView {

    protected P presenter;

    @SuppressWarnings("unchecked")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayout());
        ButterKnife.bind(this);
        if (presenter == null) {
            presenter = createPresenter();
        }
        presenter.attachView(this);
    }

    @Override
    protected void onDestroy() {
        presenter.detachView();
        super.onDestroy();
    }

    protected abstract P createPresenter();

    @LayoutRes
    protected abstract int getLayout();
}
