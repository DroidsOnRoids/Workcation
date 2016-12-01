package com.droidsonroids.workcation.screens.main.map;

import android.app.Activity;
import android.transition.Transition;
import android.transition.TransitionInflater;
import android.transition.TransitionSet;
import android.view.View;
import com.droidsonroids.workcation.R;
import com.droidsonroids.workcation.common.transitions.TextResizeTransition;

//TODO: Implement as builder
public class BaliDetailsLayoutTransitionFactory {
    private static final String TITLE_TEXT_VIEW_TRANSITION_NAME = "titleTextView";
    private static final String CARD_VIEW_TRANSITION_NAME = "cardView";

    public static TransitionSet prepareShowingTransitionSet(Activity activity, String transitionName, BaliDetailsLayout baliDetailsLayout, final View sharedView) {
        setupSharedViewShowingSceneTransitionNames(sharedView, transitionName);
        setupBaliDetailsLayoutShowingSceneTransitionNames(baliDetailsLayout, transitionName);
        final TransitionSet set = new TransitionSet();
        set
                .addTransition(prepareShowingSlideTransition(activity, transitionName, baliDetailsLayout))
                .addTransition(prepareShowingTextResizeTransition(transitionName))
                .addTransition(prepareShowingSharedTransition(activity, transitionName));
        return set;
    }

    public static TransitionSet prepareHidingTransitionSet(final Activity activity, final String transitionName, final BaliDetailsLayout baliDetailsLayout, final View sharedView) {
        setupSharedViewShowingSceneTransitionNames(sharedView, transitionName);
        setupBaliDetailsLayoutShowingSceneTransitionNames(baliDetailsLayout, transitionName);
        final TransitionSet set = new TransitionSet();
        set
                .addTransition(prepareShowingTextResizeTransition(transitionName))
                .addTransition(prepareShowingSharedTransition(activity, transitionName));
        return set;
    }

    private static Transition prepareShowingTextResizeTransition(final String transitionName) {
        TextResizeTransition transition = new TextResizeTransition();
        transition.addTarget(transitionName + TITLE_TEXT_VIEW_TRANSITION_NAME);
        return transition;
    }

    private static Transition prepareShowingSharedTransition(Activity activity, String transitionName) {
        Transition shared = TransitionInflater.from(activity).inflateTransition(android.R.transition.move);
        shared.addTarget(transitionName);
        shared.addTarget(transitionName + CARD_VIEW_TRANSITION_NAME);
        return shared;
    }

    private static Transition prepareShowingSlideTransition(final Activity activity, final String transitionName, final BaliDetailsLayout layout) {
        Transition slide = TransitionInflater.from(activity).inflateTransition(R.transition.bali_details_enter_transition);
        slide.excludeTarget(transitionName, true);
        slide.excludeTarget(layout.textViewTitle, true);
        slide.excludeTarget(layout.cardViewContainer, true);
        return slide;
    }

    private static void setupBaliDetailsLayoutShowingSceneTransitionNames(BaliDetailsLayout layout, String transitionName) {
        layout.textViewTitle.setTransitionName(getTitleTransitionName(transitionName));
        layout.imageViewPlaceDetails.setTransitionName(transitionName);
        layout.cardViewContainer.setTransitionName(getCardViewTransitionName(transitionName));
    }

    private static void setupSharedViewShowingSceneTransitionNames(View recyclerItemView, String transitionName) {
        recyclerItemView.findViewById(R.id.image_place_details).setTransitionName(transitionName);
        recyclerItemView.findViewById(R.id.textview_title).setTransitionName(getTitleTransitionName(transitionName));
        recyclerItemView.setTransitionName(getCardViewTransitionName(transitionName));
    }

    private static String getTitleTransitionName(String transitionName) {
        return transitionName + TITLE_TEXT_VIEW_TRANSITION_NAME;
    }

    private static String getCardViewTransitionName(String transitionName) {
        return transitionName + CARD_VIEW_TRANSITION_NAME;
    }
}
