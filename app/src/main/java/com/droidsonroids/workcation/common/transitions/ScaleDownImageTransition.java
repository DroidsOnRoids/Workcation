package com.droidsonroids.workcation.common.transitions;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.transition.ChangeImageTransform;
import android.transition.Transition;
import android.transition.TransitionValues;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import com.droidsonroids.workcation.R;
import com.droidsonroids.workcation.common.maps.MapBitmapCache;
import com.droidsonroids.workcation.common.maps.MapsUtil;

/**
 * Created by droids on 29.11.2016.
 */

public class ScaleDownImageTransition extends Transition {
    private static final int DEFAULT_SCALE_DOWN_FACTOR = 3;
    private static final String PROPNAME_SCALE_X= "transitions:scale_down:scale_x";
    private static final String PROPNAME_SCALE_Y= "transitions:scale_down:scale_y";

    private int scaleDownFactor;

    public ScaleDownImageTransition() {
        super();
    }

    public ScaleDownImageTransition(final Context context, final AttributeSet attrs) {
        super(context, attrs);
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.ScaleDownImageTransition);
        try {
            scaleDownFactor = array.getInteger(R.styleable.ScaleDownImageTransition_factor, DEFAULT_SCALE_DOWN_FACTOR);
        } finally {
            array.recycle();
        }
    }

    @Override
    public Animator createAnimator(final ViewGroup sceneRoot, final TransitionValues startValues, final TransitionValues endValues) {
        if (null == endValues) {
            return null;
        }
        final View view = endValues.view;
        if(view instanceof ImageView) {
            (view).setBackgroundColor(Color.BLUE);
            float scaleX = (float)endValues.values.get(PROPNAME_SCALE_X);
            float scaleY = (float)endValues.values.get(PROPNAME_SCALE_Y);

            ObjectAnimator scaleXAnimator = ObjectAnimator.ofFloat(view, View.SCALE_X, scaleX, scaleX*scaleDownFactor);
            ObjectAnimator scaleYAnimator = ObjectAnimator.ofFloat(view, View.SCALE_Y, scaleY, scaleY*scaleDownFactor);
            AnimatorSet set = new AnimatorSet();
            set.playTogether(scaleXAnimator, scaleYAnimator);
            return set;
        }
        return null;
    }

    @Override
    public void captureStartValues(TransitionValues transitionValues) {
        captureValues(transitionValues);
    }

    @Override
    public void captureEndValues(TransitionValues transitionValues) {
        captureValues(transitionValues);
    }

    private void captureValues(TransitionValues values) {
        // Capture the property values of views for later use
        values.values.put(PROPNAME_SCALE_X, values.view.getScaleX());
        values.values.put(PROPNAME_SCALE_Y, values.view.getScaleY());
    }
}
