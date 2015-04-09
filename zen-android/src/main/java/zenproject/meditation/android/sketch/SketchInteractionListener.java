package zenproject.meditation.android.sketch;

import android.view.MotionEvent;

import com.juankysoriano.rainbow.core.drawing.RainbowDrawer;
import com.juankysoriano.rainbow.core.event.RainbowInputController;

import zenproject.meditation.android.sketch.actions.StepPerformer;

public class SketchInteractionListener implements RainbowInputController.RainbowInteractionListener {
    private ZenSketch.OnPaintingListener onPaintingListener;
    private final StepPerformer inkPerformer;

    protected SketchInteractionListener(StepPerformer inkPerformer) {
        this.inkPerformer = inkPerformer;
    }

    public static SketchInteractionListener newInstance(StepPerformer inkPerformer) {
        return new SketchInteractionListener(inkPerformer);
    }

    @Override
    public void onSketchTouched(MotionEvent event, RainbowDrawer rainbowDrawer) {
        if (hasOnPaintingListener()) {
            onPaintingListener.onPaintingStart();
        }

        inkPerformer.reset();
    }

    private boolean hasOnPaintingListener() {
        return this.onPaintingListener != null;
    }

    @Override
    public void onSketchReleased(MotionEvent event, RainbowDrawer rainbowDrawer) {
        if (hasOnPaintingListener()) {
            onPaintingListener.onPaintingEnd();
        }
    }

    @Override
    public void onFingerDragged(MotionEvent event, RainbowDrawer rainbowDrawer) {
        inkPerformer.doStep();
    }

    @Override
    public void onMotionEvent(MotionEvent event, RainbowDrawer rainbowDrawer) {
        //no-op
    }

    public void setOnPaintingListener(ZenSketch.OnPaintingListener onPaintingListener) {
        this.onPaintingListener = onPaintingListener;
    }
}
