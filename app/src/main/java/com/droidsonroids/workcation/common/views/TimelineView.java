package com.droidsonroids.workcation.common.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.View;
import com.droidsonroids.workcation.R;

public class TimelineView extends View {

    private Paint backgroundPaint;
    private boolean sizeChanged = false;
    private int width;
    private int height;
    private float margin;
    private int lineThickness;
    private int radius;
    private int outerRadius;

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
            margin = array.getDimensionPixelSize(R.styleable.TimelineView_tv_margin, (int)GuiUtils.dpToPx(context, 16));
            lineThickness = array.getDimensionPixelSize(R.styleable.TimelineView_tv_lineThickness, (int)GuiUtils.dpToPx(context, 2));
            radius = array.getDimensionPixelSize(R.styleable.TimelineView_tv_radius, (int)GuiUtils.dpToPx(context, 4));
            outerRadius = array.getDimensionPixelSize(R.styleable.TimelineView_tv_outerRadius, (int)GuiUtils.dpToPx(context, 2));
            color = array.getColor(R.styleable.TimelineView_tv_color, ContextCompat.getColor(context, R.color.white));
        } finally {
            array.recycle();
        }
        backgroundPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        backgroundPaint.setColor(color);
        backgroundPaint.setStrokeWidth(lineThickness);
    }

    @Override
    protected void onSizeChanged(final int w, final int h, final int oldw, final int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        if(!sizeChanged) {
            width = w;
            height = h;
            sizeChanged = true;
        }
    }

    @Override
    protected void onDraw(final Canvas canvas) {
        super.onDraw(canvas);
        float startX = outerRadius + radius - lineThickness / 2;
        canvas.drawLine(startX, 0, startX, height / 2 - radius - outerRadius, backgroundPaint);
        canvas.drawCircle(startX, height / 2, radius, backgroundPaint);
        canvas.drawLine(startX, height / 2 + radius + outerRadius, startX, height, backgroundPaint);
    }
}
