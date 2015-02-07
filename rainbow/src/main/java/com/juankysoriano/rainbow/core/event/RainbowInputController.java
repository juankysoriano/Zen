package com.juankysoriano.rainbow.core.event;

import android.view.MotionEvent;

import com.juankysoriano.rainbow.core.drawing.RainbowDrawer;

public class RainbowInputController {
    private final EventDispatcher eventDispatcher;
    private final FingerPositionPredictor fingerPositionPredictor;
    private RainbowInteractionListener rainbowInteractionListener;
    private float x, y;
    private float px, py;
    private boolean screenTouched;

    public RainbowInputController() {
        fingerPositionPredictor = new FingerPositionPredictor();
        eventDispatcher = new EventDispatcher();
        x = y = px = py = -1;
    }

    public void postEvent(final RainbowEvent motionEvent, final RainbowDrawer rainbowDrawer, final boolean looping) {
        eventDispatcher.setEvent(motionEvent);
        if (!looping) {
            rainbowDrawer.beginDraw();
            dequeueEvents(rainbowDrawer);
            rainbowDrawer.endDraw();
        }
    }

    public void dequeueEvents(final RainbowDrawer rainbowDrawer) {
        if (eventDispatcher.hasEvent()) {
            final RainbowEvent motionEvent = eventDispatcher.getEvent();
            preHandleEvent(motionEvent);
            handleSketchEvent(motionEvent, rainbowDrawer);
            postHandleEvent(motionEvent);
        }
    }

    private void handleSketchEvent(final RainbowEvent event, final RainbowDrawer rainbowDrawer) {

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                screenTouched = true;
                fingerPositionPredictor.resetTo(event.getX(), event.getY());
                if (hasInteractionListener()) {
                    rainbowInteractionListener.onSketchTouched(event, rainbowDrawer);
                }
                break;
            case MotionEvent.ACTION_UP:
                screenTouched = false;
                if (hasInteractionListener()) {
                    rainbowInteractionListener.onSketchReleased(event, rainbowDrawer);
                }
                break;
            case MotionEvent.ACTION_MOVE:
                screenTouched = true;
                fingerPositionPredictor.moveTo(event.getX(), event.getY());
                if (hasInteractionListener()) {
                    rainbowInteractionListener.onFingerDragged(event, rainbowDrawer);
                }
                break;
        }

        if (hasInteractionListener()) {
            rainbowInteractionListener.onMotionEvent(event, rainbowDrawer);
        }
    }

    private synchronized void preHandleEvent(RainbowEvent event) {
        if ((event.getAction() == MotionEvent.ACTION_DOWN)
                || (event.getAction() == MotionEvent.ACTION_UP)
                || event.getAction() == MotionEvent.ACTION_MOVE) {
            px = x;
            py = y;
            x = event.getX();
            y = event.getY();
        }
    }

    private synchronized void postHandleEvent(RainbowEvent event) {
        if ((event.getAction() == MotionEvent.ACTION_DOWN)
                || (event.getAction() == MotionEvent.ACTION_UP)
                || event.getAction() == MotionEvent.ACTION_MOVE) {
            px = x;
            py = y;
        }
    }

    /**
     * Used to set a RainbowInteractionListener which will listen for different interaction events
     * @param rainbowInteractionListener
     */
    public void setRainbowInteractionListener(RainbowInteractionListener rainbowInteractionListener) {
        this.rainbowInteractionListener = rainbowInteractionListener;
    }

    /**
     * Used to remove the attached RainbowInteractionListener
     */
    public void removeSketchInteractionListener() {
        this.rainbowInteractionListener = null;
    }

    private boolean hasInteractionListener() {
        return rainbowInteractionListener != null;
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public float getPreviousX() {
        if (px == -1) {
            return x;
        } else {
            return px;
        }
    }

    public float getPreviousY() {
        if (py == -1) {
            return y;
        } else {
            return py;
        }
    }

    public float getSmoothX() {
        return fingerPositionPredictor.getX();
    }

    public float getSmoothY() {
        return fingerPositionPredictor.getY();
    }

    public float getPreviousSmoothX() {
        return fingerPositionPredictor.getOldX();
    }

    public float getPreviousSmoothY() {
        return fingerPositionPredictor.getOldY();
    }

    public boolean isScreenTouched() {
        return screenTouched;
    }

    public static interface RainbowInteractionListener {
        void onSketchTouched(final RainbowEvent event, final RainbowDrawer rainbowDrawer);

        void onSketchReleased(final RainbowEvent event, final RainbowDrawer rainbowDrawer);

        void onFingerDragged(final RainbowEvent event, final RainbowDrawer rainbowDrawer);

        void onMotionEvent(final RainbowEvent event, final RainbowDrawer rainbowDrawer);
    }
}
