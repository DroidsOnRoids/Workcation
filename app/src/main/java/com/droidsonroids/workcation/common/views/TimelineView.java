package com.droidsonroids.workcation.common.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.View;
import com.droidsonroids.workcation.R;
import com.droidsonroids.workcation.common.ViewUtils;

public class TimelineView extends View {

    private Paint mBackgroundPaint;
    private boolean sizeChanged = false;
    private int mWidth;
    private int mHeight;
    private float mMargin;
    private int mLineThickness;
    private int mRadius;
    private int mOuterRadius;

    public TimelineView(final Context context) {
        this(context, null);
    }

    public TimelineView(final Context context, final AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    private void init(final Context context, final AttributeSet attrs) {
        int color;
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.TimelineView);
        try {
            mMargin = array.getDimensionPixelSize(R.styleable.TimelineView_tv_margin, (int)GuiUtils.dpToPx(context, 16));
            mLineThickness = array.getDimensionPixelSize(R.styleable.TimelineView_tv_lineThickness, (int)GuiUtils.dpToPx(context, 2));
            mRadius = array.getDimensionPixelSize(R.styleable.TimelineView_tv_radius, (int)GuiUtils.dpToPx(context, 4));
            mOuterRadius = array.getDimensionPixelSize(R.styleable.TimelineView_tv_outerRadius, (int)GuiUtils.dpToPx(context, 2));
            color = array.getColor(R.styleable.TimelineView_tv_color, ContextCompat.getColor(context, R.color.white));
        } finally {
            array.recycle();
        }
        mBackgroundPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mBackgroundPaint.setColor(color);
        mBackgroundPaint.setStrokeWidth(mLineThickness);
    }

    @Override
    protected void onSizeChanged(final int w, final int h, final int oldw, final int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        if(!sizeChanged) {
            mWidth = w;
            mHeight = h;
            sizeChanged = true;
        }
    }

    @Override
    protected void onDraw(final Canvas canvas) {
        super.onDraw(canvas);
        float startX = mOuterRadius + mRadius - mLineThickness / 2;
        canvas.drawLine(startX, 0, startX, mHeight / 2 - mRadius - mOuterRadius,mBackgroundPaint);
        canvas.drawCircle(startX, mHeight / 2,  mRadius, mBackgroundPaint);
        canvas.drawLine(startX, mHeight / 2 + mRadius + mOuterRadius, startX, mHeight, mBackgroundPaint);
    }
}
