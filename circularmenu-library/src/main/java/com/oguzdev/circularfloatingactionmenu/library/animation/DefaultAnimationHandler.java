/*
 *   Copyright 2014 Oguz Bilgener
 */
package com.oguzdev.circularfloatingactionmenu.library.animation;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.graphics.Point;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;

import com.oguzdev.circularfloatingactionmenu.library.CircularMenu;

/**
 * An example animation handler
 * Animates translation, rotation, scale and alpha at the same time using Property Animation APIs.
 */
public class DefaultAnimationHandler extends MenuAnimationHandler {

    /**
     * duration of animations, in milliseconds
     */
    private static final int DURATION = 400;

    private static final int FACE_DOWN = 180;
    private static final int FACE_UP = 360;
    private static final int OPAQUE = 1;

    /**
     * holds the current state of animation
     */
    private boolean animating;

    public DefaultAnimationHandler() {
        setAnimating(false);
    }

    @Override
    public void animateMenuOpening(Point center) {
        super.animateMenuOpening(center);

        setAnimating(true);

        for (int i = 0; i < menu.getSubActionItems().size(); i++) {
            animateOpeningSubActionItem(center, menu.getSubActionItems().get(i), i == 0);
        }

        if (menu.hasBackgroundView()) {
            animateRevealBackground(center);
        }

    }

    private void animateOpeningSubActionItem(Point center, CircularMenu.Item item, boolean isFirst) {
        item.view.setAlpha(OPAQUE);
        item.view.setRotation(FACE_DOWN);

        PropertyValuesHolder pvhX = PropertyValuesHolder.ofFloat(View.TRANSLATION_X, item.x - center.x + item.width / 2);
        PropertyValuesHolder pvhY = PropertyValuesHolder.ofFloat(View.TRANSLATION_Y, item.y - center.y + item.height / 2);
        PropertyValuesHolder pvhR = PropertyValuesHolder.ofFloat(View.ROTATION, FACE_UP);

        ObjectAnimator animation = ObjectAnimator.ofPropertyValuesHolder(item.view, pvhX, pvhY, pvhR);
        animation.setDuration(DURATION);
        animation.setInterpolator(new AccelerateDecelerateInterpolator());
        animation.addListener(new SubActionItemAnimationListener(item, ActionType.OPENING));

        if (isFirst) {
            animation.addListener(new LastAnimationListener(getStateChangeListener(), ActionType.OPENING, getMenu()));
        }
        animation.start();
    }

    private void animateRevealBackground(Point center) {
        View menuBackgroundView = menu.getBackgroundView();
        menuBackgroundView.setAlpha(1);
        menuBackgroundView.setX(center.x - menuBackgroundView.getWidth() / 2);
        menuBackgroundView.setY(center.y - menuBackgroundView.getHeight() / 2);
        menuBackgroundView.setScaleX(0);
        menuBackgroundView.setScaleY(0);

        PropertyValuesHolder pvhX = PropertyValuesHolder.ofFloat(View.SCALE_X, menu.getRadius() * 3.5f / menuBackgroundView.getWidth());
        PropertyValuesHolder pvhY = PropertyValuesHolder.ofFloat(View.SCALE_Y, menu.getRadius() * 3.5f / menuBackgroundView.getHeight());

        final ObjectAnimator animation = ObjectAnimator.ofPropertyValuesHolder(menuBackgroundView, pvhX, pvhY);
        animation.setDuration(DURATION);
        animation.setInterpolator(new AccelerateDecelerateInterpolator());

        animation.start();
    }

    @Override
    public void animateMenuClosing(Point center) {
        super.animateMenuOpening(center);

        setAnimating(true);

        for (int i = 0; i < menu.getSubActionItems().size(); i++) {
            animateClosingSubActionItem(center, menu.getSubActionItems().get(i), i == 0);
        }

        if (menu.hasBackgroundView()) {
            animateCollapseBackground();
        }

    }

    private void animateClosingSubActionItem(Point center, CircularMenu.Item item, boolean isFirst) {
        PropertyValuesHolder pvhX = PropertyValuesHolder.ofFloat(View.TRANSLATION_X, -(item.x - center.x + item.width / 2));
        PropertyValuesHolder pvhY = PropertyValuesHolder.ofFloat(View.TRANSLATION_Y, -(item.y - center.y + item.height / 2));
        PropertyValuesHolder pvhR = PropertyValuesHolder.ofFloat(View.ROTATION, FACE_DOWN);

        final ObjectAnimator animation = ObjectAnimator.ofPropertyValuesHolder(item.view, pvhX, pvhY, pvhR);
        animation.setDuration(DURATION);
        animation.setInterpolator(new AccelerateDecelerateInterpolator());
        animation.addListener(new SubActionItemAnimationListener(item, ActionType.CLOSING));

        if (isFirst) {
            animation.addListener(new LastAnimationListener(getStateChangeListener(), ActionType.CLOSING, getMenu()));
        }

        animation.start();
    }

    private void animateCollapseBackground() {
        PropertyValuesHolder pvhX = PropertyValuesHolder.ofFloat(View.SCALE_X, 0);
        PropertyValuesHolder pvhY = PropertyValuesHolder.ofFloat(View.SCALE_Y, 0);

        final ObjectAnimator animation = ObjectAnimator.ofPropertyValuesHolder(menu.getBackgroundView(), pvhX, pvhY);
        animation.setDuration((long) (DURATION * 1.2));
        animation.setInterpolator(new AccelerateDecelerateInterpolator());

        animation.start();
    }

    @Override
    public boolean isAnimating() {
        return animating;
    }

    @Override
    protected void setAnimating(boolean animating) {
        this.animating = animating;
    }

    protected class SubActionItemAnimationListener implements Animator.AnimatorListener {

        private CircularMenu.Item subActionItem;
        private ActionType actionType;

        public SubActionItemAnimationListener(CircularMenu.Item subActionItem, ActionType actionType) {
            this.subActionItem = subActionItem;
            this.actionType = actionType;
        }

        @Override
        public void onAnimationStart(Animator animation) {

        }

        @Override
        public void onAnimationEnd(Animator animation) {
            restoreSubActionViewAfterAnimation(subActionItem, actionType);
        }

        @Override
        public void onAnimationCancel(Animator animation) {
            restoreSubActionViewAfterAnimation(subActionItem, actionType);
        }

        @Override
        public void onAnimationRepeat(Animator animation) {
        }
    }
}
