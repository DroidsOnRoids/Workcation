package com.droidsonroids.workcation.common.maps;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import com.droidsonroids.workcation.R;
import com.droidsonroids.workcation.common.maps.MarkerView;
import com.droidsonroids.workcation.common.views.GuiUtils;
import com.google.android.gms.maps.model.LatLng;

public class PulseMarkerView extends MarkerView {

    private final Context context;
    private float mSize;
    private Animation mScaleAnimation;
    private Paint mStrokeBackgroundPaint;
    private Paint mBackgroundPaint;
    private String mText;
    private Paint mTextPaint;
    private AnimatorSet showAnimatorSet, hideAnimatorSet, pulseAnimatorSet;

    public PulseMarkerView(final Context context, final LatLng latLng, final Point point) {
        super(context, latLng, point);
        this.context = context;
        setVisibility(View.INVISIBLE);
        setupSizes(context);
        setupScaleAnimation(context);
        setupBackgroundPaint(context);
        setupStrokeBackgroundPaint(context);
        setupTextPaint(context);
        setupShowAnimatorSet();
        setupHideAnimatorSet();
        setupPulseAnimator();
    }

    public PulseMarkerView(final Context context, final LatLng latLng, final Point point, final int position) {
        this(context, latLng, point);
        mText = String.valueOf(position);
    }

    private void setupPulseAnimator() {
        ObjectAnimator animatorScaleX = ObjectAnimator.ofFloat(this, View.SCALE_X, 1.0f, 1.5f);
        animatorScaleX.setRepeatMode(ValueAnimator.REVERSE);
        ObjectAnimator animatorScaleY = ObjectAnimator.ofFloat(this, View.SCALE_Y, 1.0f, 1.5f);
        animatorScaleY.setRepeatMode(ValueAnimator.REVERSE);

        pulseAnimatorSet = new AnimatorSet();
        pulseAnimatorSet.setDuration(100);
        pulseAnimatorSet.playTogether(animatorScaleX, animatorScaleY);
    }

    private void setupHideAnimatorSet() {
        Animator animatorScaleX = ObjectAnimator.ofFloat(this, View.SCALE_X, 1.0f, 0.f);
        Animator animatorScaleY = ObjectAnimator.ofFloat(this, View.SCALE_Y, 1.0f, 0.f);
        Animator animator = ObjectAnimator.ofFloat(this, View.ALPHA, 1.f, 0.f).setDuration(300);
        animator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationStart(final Animator animation) {
                super.onAnimationStart(animation);
                setVisibility(View.INVISIBLE);
                invalidate();
            }
        });
        hideAnimatorSet = new AnimatorSet();
        hideAnimatorSet.playTogether(animator, animatorScaleX, animatorScaleY);
    }

    private void setupSizes(final Context context) {
        mSize = GuiUtils.dpToPx(context, 32) / 2;
    }

    private void setupShowAnimatorSet() {
        Animator animatorScaleX = ObjectAnimator.ofFloat(this, View.SCALE_X, 1.5f, 1.f);
        Animator animatorScaleY = ObjectAnimator.ofFloat(this, View.SCALE_Y, 1.5f, 1.f);
        Animator animator = ObjectAnimator.ofFloat(this, View.ALPHA, 0.f, 1.f).setDuration(300);
        animator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationStart(final Animator animation) {
                super.onAnimationStart(animation);
                setVisibility(View.VISIBLE);
                invalidate();
            }
        });
        showAnimatorSet = new AnimatorSet();
        showAnimatorSet.playTogether(animator, animatorScaleX, animatorScaleY);
    }

    private void setupScaleAnimation(final Context context) {
        mScaleAnimation = AnimationUtils.loadAnimation(context, R.anim.pulse);
        mScaleAnimation.setDuration(100);
    }

    private void setupTextPaint(final Context context) {
        mTextPaint = new Paint();
        mTextPaint.setColor(ContextCompat.getColor(context, R.color.white));
        mTextPaint.setTextAlign(Paint.Align.CENTER);
        mTextPaint.setTextSize(context.getResources().getDimensionPixelSize(R.dimen.textsize_medium));
    }

    private void setupStrokeBackgroundPaint(final Context context) {
        mStrokeBackgroundPaint = new Paint();
        mStrokeBackgroundPaint.setColor(ContextCompat.getColor(context, android.R.color.white));
        mStrokeBackgroundPaint.setStyle(Paint.Style.STROKE);
        mStrokeBackgroundPaint.setAntiAlias(true);
        mStrokeBackgroundPaint.setStrokeWidth(GuiUtils.dpToPx(context, 2));
    }

    private void setupBackgroundPaint(final Context context) {
        mBackgroundPaint = new Paint();
        mBackgroundPaint.setColor(ContextCompat.getColor(context, android.R.color.holo_red_dark));
        mBackgroundPaint.setAntiAlias(true);
    }

    @Override
    public void setLayoutParams(final ViewGroup.LayoutParams params) {
        FrameLayout.LayoutParams frameParams = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT);
        frameParams.width = (int)GuiUtils.dpToPx(context, 44);
        frameParams.height = (int)GuiUtils.dpToPx(context, 44);
        frameParams.leftMargin = point.x - frameParams.width / 2;
        frameParams.topMargin = point.y - frameParams.height / 2;
        super.setLayoutParams(frameParams);
    }

    public void pulse() {
        startAnimation(mScaleAnimation);
    }

    @Override
    protected void onDraw(final Canvas canvas) {
        drawBackground(canvas);
        drawStrokeBackground(canvas);
        drawText(canvas);
        super.onDraw(canvas);
    }

    private void drawText(final Canvas canvas) {
        if(mText != null && !TextUtils.isEmpty(mText))
            canvas.drawText(mText, mSize, (mSize - ((mTextPaint.descent() + mTextPaint.ascent()) / 2)), mTextPaint);
    }

    private void drawStrokeBackground(final Canvas canvas) {
        canvas.drawCircle(mSize, mSize, GuiUtils.dpToPx(context, 28) / 2, mStrokeBackgroundPaint);
    }

    private void drawBackground(final Canvas canvas) {
        canvas.drawCircle(mSize, mSize, mSize, mBackgroundPaint);
    }

    public void setText(String text) {
        mText = text;
        invalidate();
    }

    @Override
    public void hide() {
        hideAnimatorSet.start();
    }

    @Override
    public void refresh(final Point point) {
        this.point = point;
        updatePulseViewLayoutParams(point);
    }

    @Override
    public void show() {
        showAnimatorSet.start();
    }

    public void showWithDelay(final int delay) {
        showAnimatorSet.setStartDelay(delay);
        showAnimatorSet.start();
    }

    public void updatePulseViewLayoutParams(final Point point) {
        this.point = point;
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT);
        params.width = (int)GuiUtils.dpToPx(context, 44);
        params.height = (int)GuiUtils.dpToPx(context, 44);
        params.leftMargin = point.x - params.width / 2;
        params.topMargin = point.y - params.height / 2;
        super.setLayoutParams(params);
        invalidate();
    }
}
