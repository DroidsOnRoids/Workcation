package com.droidsonroids.workcation.common.transitions;

import android.transition.PathMotion;
import android.transition.Transition;
import android.transition.TransitionPropagation;
import android.util.Pair;
import android.view.View;

public class TransitionBuilder {

    private Transition transition;

    public TransitionBuilder(final Transition transition) {
        this.transition = transition;
    }

    public TransitionBuilder duration(long duration) {
        transition.setDuration(duration);
        return this;
    }

    public TransitionBuilder target(View view) {
        transition.addTarget(view);
        return this;
    }

    public TransitionBuilder target(Class clazz) {
        transition.addTarget(clazz);
        return this;
    }

    public TransitionBuilder target(String target) {
        transition.addTarget(target);
        return this;
    }

    public TransitionBuilder target(int targetId) {
        transition.addTarget(targetId);
        return this;
    }

    public TransitionBuilder delay(long delay) {
        transition.setStartDelay(delay);
        return this;
    }

    public TransitionBuilder pathMotion(PathMotion motion) {
        transition.setPathMotion(motion);
        return this;
    }

    public TransitionBuilder propagation(TransitionPropagation propagation) {
        transition.setPropagation(propagation);
        return this;
    }

    public TransitionBuilder pair(Pair<View, String> pair) {
        pair.first.setTransitionName(pair.second);
        transition.addTarget(pair.second);
        return this;
    }

    public TransitionBuilder excludeTarget(final View view, final boolean exclude){
        transition.excludeTarget(view, exclude);
        return this;
    }

    public TransitionBuilder excludeTarget(final String targetName, final boolean exclude) {
        transition.excludeTarget(targetName, exclude);
        return this;
    }

    public TransitionBuilder link(final View from, final View to, final String transitionName) {
        from.setTransitionName(transitionName);
        to.setTransitionName(transitionName);
        transition.addTarget(transitionName);
        return this;
    }

    public Transition build() {
        return transition;
    }
}
