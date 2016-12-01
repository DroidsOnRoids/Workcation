package com.droidsonroids.workcation.common.views;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.drawable.TransitionDrawable;
import android.support.v4.content.ContextCompat;
import android.support.v4.text.TextUtilsCompat;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.TextView;
import com.droidsonroids.workcation.R;

public class PulseView extends View {

    private float mSize;
    private Animation mScaleAnimation;
    private int mPadding;
    private final Context mContext;
    private Paint mStrokeBackgroundPaint;
    private Paint mBackgroundPaint;
    private String mText;
    private Paint mTextPaint;
    private Point mPoint;
    private AnimatorSet showAnimatorSet;

    public PulseView(final Context context) {
        this(context, null);
    }

    public PulseView(final Context context, final AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        setVisibility(View.INVISIBLE);
        setupSizes(context);
        setupScaleAnimation(context);
        setupBackgroundPaint(context);
        setupStrokeBackgroundPaint(context);
        setupTextPaint(context);
        setupShowAnimation();
    }

    private void setupSizes(final Context context) {
        mPadding = context.getResources().getDimensionPixelSize(R.dimen.default_layout_margin_halved);
        mSize = GuiUtils.dpToPx(mContext, 32) / 2;
    }

    private void setupShowAnimation() {
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
        params.height = (params.height + (int)GuiUtils.dpToPx(mContext, 44)) / 2;
        params.width =  (params.width + (int)GuiUtils.dpToPx(mContext, 44)) / 2;
        ((FrameLayout.LayoutParams)params).leftMargin = mPoint.x - params.width / 2;
        ((FrameLayout.LayoutParams)params).topMargin = mPoint.y - params.height / 2;
        super.setLayoutParams(params);
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
            canvas.drawText(mText, getWidth() / 2, ((getHeight() / 2) - ((mTextPaint.descent() + mTextPaint.ascent()) / 2)), mTextPaint);
    }

    private void drawStrokeBackground(final Canvas canvas) {
        canvas.drawCircle(mSize, mSize, GuiUtils.dpToPx(mContext, 28) / 2, mStrokeBackgroundPaint);
    }

    private void drawBackground(final Canvas canvas) {
        canvas.drawCircle(mSize, mSize, mSize, mBackgroundPaint);
    }

    public void setText(String text) {
        mText = text;
        invalidate();
    }

    public void hide() {
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
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playTogether(animator, animatorScaleX, animatorScaleY);
        animatorSet.start();
    }

    public void showWithDelay(final int delay) {
        showAnimatorSet.setStartDelay(delay);
        showAnimatorSet.start();
    }

    public void show() {
        showAnimatorSet.start();
    }

    public void setPulseViewLayoutParams(final Point point) {
        mPoint = point;
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT);
        params.leftMargin = point.x - getWidth() / 2;
        params.topMargin = point.y - getHeight() / 2;
        setLayoutParams(params);
        invalidate();
    }
}
