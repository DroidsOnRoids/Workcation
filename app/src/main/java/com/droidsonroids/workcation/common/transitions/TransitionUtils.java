package com.droidsonroids.workcation.common.transitions;

public class TransitionUtils {
    private static final String DEFAULT_TRANSITION_NAME = "transition";

    public static int getItemPositionFromTransition(final String transitionName) {
        return Integer.parseInt(transitionName.substring(transitionName.length() - 1));
    }

    public static String getRecyclerViewTransitionName(final int position) {
        return DEFAULT_TRANSITION_NAME + position;
    }
}
