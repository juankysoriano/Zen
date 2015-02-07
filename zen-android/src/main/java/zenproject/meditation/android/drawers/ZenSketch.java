package zenproject.meditation.android.drawers;

import android.view.ViewGroup;

import com.juankysoriano.rainbow.core.Rainbow;
import com.juankysoriano.rainbow.core.drawing.RainbowDrawer;
import com.juankysoriano.rainbow.core.event.RainbowEvent;
import com.juankysoriano.rainbow.core.event.RainbowInputController;

public class ZenSketch extends Rainbow implements RainbowInputController.RainbowInteractionListener {

    private StepDrawer inkDrawer;
    private StepDrawer eraserDrawer;
    private final RainbowInputController rainbowInputController;
    private final RainbowDrawer rainbowDrawer;
    private SketchTouchesListener sketchTouchesListener;

    public static ZenSketch newInstance(ViewGroup viewGroup) {
        RainbowDrawer rainbowDrawer = new RainbowDrawer();
        RainbowInputController rainbowInputController = new RainbowInputController();
        return new ZenSketch(viewGroup, rainbowDrawer, rainbowInputController);
    }

    ZenSketch(ViewGroup viewGroup,
              RainbowDrawer rainbowDrawer,
              RainbowInputController rainbowInputController) {
        super(viewGroup, rainbowDrawer, rainbowInputController);
        this.rainbowDrawer = rainbowDrawer;
        this.rainbowInputController = rainbowInputController;
    }

    @Override
    public void onSketchSetup() {
        this.inkDrawer = InkDrawer.newInstance(rainbowDrawer, rainbowInputController);
        this.eraserDrawer = EraserDrawer.newInstance(rainbowDrawer, rainbowInputController);
        rainbowInputController.setRainbowInteractionListener(this);
    }

    @Override
    public void onDrawingStart() {
        super.onDrawingStart();
    }

    @Override
    public void onDrawingStep() {
        inkDrawer.paintStep();
        eraserDrawer.paintStep();
    }

    @Override
    public void onDrawingStop() {
        super.onDrawingStop();
    }

    @Override
    public void onSketchDestroy() {
    }

    @Override
    public void onSketchTouched(RainbowEvent event, RainbowDrawer rainbowDrawer) {
        if (hasListener()) {
            sketchTouchesListener.onSketchTouched(event, rainbowDrawer);
        }
        inkDrawer.initDrawingAt(event.getX(), event.getY());
    }

    @Override
    public void onSketchReleased(RainbowEvent event, RainbowDrawer rainbowDrawer) {
        if (hasListener()) {
            sketchTouchesListener.onSketchReleased(event, rainbowDrawer);
        }
    }

    @Override
    public void onFingerDragged(RainbowEvent event, RainbowDrawer rainbowDrawer) {
        inkDrawer.paintStep();
        eraserDrawer.paintStep();
    }

    @Override
    public void onMotionEvent(RainbowEvent event, RainbowDrawer rainbowDrawer) {
        //no-op
    }

    public void disableErasing() {
        eraserDrawer.disable();
    }

    public void enablePainting() {
        inkDrawer.enable();
    }

    public void enableErasing() {
        eraserDrawer.enable();
    }

    public void disablePainting() {
        inkDrawer.disable();
    }

    public void setSketchTouchesListener(SketchTouchesListener sketchTouchesListener) {
        this.sketchTouchesListener = sketchTouchesListener;
    }

    private boolean hasListener() {
        return sketchTouchesListener != null;
    }

    public interface SketchTouchesListener {
        void onSketchTouched(RainbowEvent event, RainbowDrawer rainbowDrawer);

        void onSketchReleased(RainbowEvent event, RainbowDrawer rainbowDrawer);
    }
}
