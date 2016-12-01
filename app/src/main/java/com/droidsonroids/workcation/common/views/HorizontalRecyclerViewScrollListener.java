package com.droidsonroids.workcation.common.views;

import android.support.v7.widget.RecyclerView;

public class HorizontalRecyclerViewScrollListener extends RecyclerView.OnScrollListener {
    private static final int OFFSET_RANGE = 50;
    private static final double COVER_FACTOR = 0.7;

    private int[] mItemRanges = null;
    private final int itemsCount;
    private final OnShowScaleAnimationListener listener;

    public HorizontalRecyclerViewScrollListener(final int itemsCount, final OnShowScaleAnimationListener listener) {
        this.itemsCount = itemsCount;
        this.listener = listener;
    }

    @Override
    public void onScrolled(final RecyclerView recyclerView, final int dx, final int dy) {
        super.onScrolled(recyclerView, dx, dy);
        if (mItemRanges == null) fillItemRanges(itemsCount, recyclerView);
        for (int i = 0; i < mItemRanges.length; i++) {
            if (isInChildItemsRange(recyclerView.computeHorizontalScrollOffset(), mItemRanges[i], OFFSET_RANGE)) listener.onShowAnimation(i);
        }
    }

    private void fillItemRanges(final int placesCount, final RecyclerView recyclerView) {
        mItemRanges = new int[placesCount];
        int childWidth = (recyclerView.computeHorizontalScrollRange() - recyclerView.computeHorizontalScrollExtent()) / placesCount;
        for (int i = 0; i < placesCount; i++) {
            mItemRanges[i] = (int) (((childWidth * i + childWidth * (i + 1)) / 2) * COVER_FACTOR);
        }
    }

    private boolean isInChildItemsRange(final int offset, final int itemBound, final int range) {
        int rangeMin = itemBound - range;
        int rangeMax = itemBound + range;
        return (Math.min(rangeMin, rangeMax) <= offset) && (Math.max(rangeMin, rangeMax) >= offset);
    }

    public interface OnShowScaleAnimationListener {
        void onShowAnimation(final int position);
    }
}
