package zenproject.meditation.android.sketch.painting;

import android.view.MotionEvent;

import com.juankysoriano.rainbow.core.drawing.RainbowDrawer;
import com.juankysoriano.rainbow.core.event.RainbowInputController;

import zenproject.meditation.android.sketch.ZenSketch;
import zenproject.meditation.android.sketch.actions.StepPerformer;

public class SketchInteractionListener implements RainbowInputController.RainbowInteractionListener {
    private ZenSketch.PaintListener paintListener;
    private final StepPerformer inkPerformer;

    protected SketchInteractionListener(StepPerformer inkPerformer) {
        this.inkPerformer = inkPerformer;
    }

    public static SketchInteractionListener newInstance(StepPerformer inkPerformer) {
        return new SketchInteractionListener(inkPerformer);
    }

    @Override
    public void onSketchTouched(MotionEvent event, RainbowDrawer rainbowDrawer) {
        if (hasPaintListener()) {
            paintListener.onPaintingStart();
        }
        inkPerformer.reset();
    }

    @Override
    public void onSketchReleased(MotionEvent event, RainbowDrawer rainbowDrawer) {
        if (hasPaintListener()) {
            paintListener.onPaintingEnd();
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

    public void setPaintListener(ZenSketch.PaintListener paintListener) {
        this.paintListener = paintListener;
    }

    private boolean hasPaintListener() {
        return paintListener != null;
    }
}
