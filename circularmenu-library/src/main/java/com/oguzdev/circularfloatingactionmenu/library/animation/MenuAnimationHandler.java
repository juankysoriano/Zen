/*
 *   Copyright 2014 Oguz Bilgener
 */
package com.oguzdev.circularfloatingactionmenu.library.animation;

import android.animation.Animator;
import android.graphics.Point;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;

import com.oguzdev.circularfloatingactionmenu.library.CircularMenu;

/**
 * An abstract class that is a prototype for the actual animation handlers
 */
public abstract class MenuAnimationHandler {

    private CircularMenu.MenuStateChangeListener stateChangeListener;

    // There are only two distinct animations at the moment.
    protected enum ActionType {
        OPENING, CLOSING
    }

    protected CircularMenu menu;

    public MenuAnimationHandler() {
    }

    public void setMenu(CircularMenu menu) {
        this.menu = menu;
    }

    protected CircularMenu getMenu() {
        return this.menu;
    }

    /**
     * Starts the opening animation
     * Should be overriden by children
     *
     * @param center
     */
    public void animateMenuOpening(Point center) {
        if (menu == null) {
            throw new NullPointerException("MenuAnimationHandler cannot animate without a valid FloatingActionMenu.");
        }

    }

    /**
     * Ends the opening animation
     * Should be overriden by children
     *
     * @param center
     */
    public void animateMenuClosing(Point center) {
        if (menu == null) {
            throw new NullPointerException("MenuAnimationHandler cannot animate without a valid FloatingActionMenu.");
        }
    }

    /**
     * Sets a listener which will notify when animation is end.
     */
    public void setStateChangeListener(CircularMenu.MenuStateChangeListener stateChangeListener) {
        this.stateChangeListener = stateChangeListener;
    }

    /**
     * @return listener notifying for animation ends.
     */
    protected CircularMenu.MenuStateChangeListener getStateChangeListener() {
        return stateChangeListener;
    }

    /**
     * Restores the specified sub action view to its final state, according to the current actionType
     * Should be called after an animation finishes.
     *
     * @param subActionItem
     * @param actionType
     */
    protected void restoreSubActionViewAfterAnimation(CircularMenu.Item subActionItem, ActionType actionType) {
        ViewGroup.LayoutParams params = subActionItem.view.getLayoutParams();
        subActionItem.view.setTranslationX(0);
        subActionItem.view.setTranslationY(0);
        subActionItem.view.setRotation(0);
        subActionItem.view.setScaleX(1);
        subActionItem.view.setScaleY(1);
        subActionItem.view.setAlpha(1);
        if (actionType == ActionType.OPENING) {
            FrameLayout.LayoutParams lp = (FrameLayout.LayoutParams) params;
            if (menu.isSystemOverlay()) {
                WindowManager.LayoutParams overlayParams = (WindowManager.LayoutParams) menu.getOverlayContainer().getLayoutParams();
                lp.setMargins(subActionItem.x - overlayParams.x, subActionItem.y - overlayParams.y, 0, 0);
            } else {
                lp.setMargins(subActionItem.x, subActionItem.y, 0, 0);
            }
            subActionItem.view.setLayoutParams(lp);
        } else if (actionType == ActionType.CLOSING) {
            Point center = menu.getActionViewCenter();
            FrameLayout.LayoutParams lp = (FrameLayout.LayoutParams) params;
            if (menu.isSystemOverlay()) {
                WindowManager.LayoutParams overlayParams = (WindowManager.LayoutParams) menu.getOverlayContainer().getLayoutParams();
                lp.setMargins(center.x - overlayParams.x - subActionItem.width / 2, center.y - overlayParams.y - subActionItem.height / 2, 0, 0);
            } else {
                lp.setMargins(center.x - subActionItem.width / 2, center.y - subActionItem.height / 2, 0, 0);
            }
            subActionItem.view.setLayoutParams(lp);
            menu.removeViewFromCurrentContainer(subActionItem.view);

            if (menu.isSystemOverlay()) {
                // When all the views are removed from the overlay container,
                // we also need to detach it
                if (menu.getOverlayContainer().getChildCount() == 0) {
                    menu.detachOverlayContainer();
                }
            }
        }
    }

    /**
     * A special animation listener that is intended to listen the last of the sequential animations.
     * Changes the animating property of children.
     */
    public class LastAnimationListener implements Animator.AnimatorListener {

        private final CircularMenu.MenuStateChangeListener stateChangeListener;
        private final ActionType actionType;
        private final CircularMenu circularMenu;

        public LastAnimationListener(CircularMenu.MenuStateChangeListener stateChangeListener, ActionType actionType, CircularMenu circularMenu) {
            this.stateChangeListener = stateChangeListener;
            this.actionType = actionType;
            this.circularMenu = circularMenu;
        }

        @Override
        public void onAnimationStart(Animator animation) {
            setAnimating(true);
        }

        @Override
        public void onAnimationEnd(Animator animation) {
            setAnimating(false);
            notifyAnimationEnds();
        }

        @Override
        public void onAnimationCancel(Animator animation) {
            setAnimating(false);
            notifyAnimationEnds();
        }

        @Override
        public void onAnimationRepeat(Animator animation) {
            setAnimating(true);
        }

        private void notifyAnimationEnds() {
            if (isAnimationOpening()) {
                notifyAnimationOpeningEnds();
            } else if (isAnimationClosing()) {
                notifyAnimationClosingEnds();
            }
        }

        private void notifyAnimationOpeningEnds() {
            if (stateChangeListener != null) {
                stateChangeListener.onMenuOpened(circularMenu);
            }
        }

        private void notifyAnimationClosingEnds() {
            if (stateChangeListener != null) {
                stateChangeListener.onMenuClosed(circularMenu);
            }
        }

        private boolean isAnimationOpening() {
            return actionType == ActionType.OPENING;
        }

        private boolean isAnimationClosing() {
            return actionType == ActionType.CLOSING;
        }
    }

    public abstract boolean isAnimating();

    protected abstract void setAnimating(boolean animating);
}
