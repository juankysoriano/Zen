package zenproject.meditation.android.drawers;

import android.view.MotionEvent;

import com.juankysoriano.rainbow.core.Rainbow;
import com.juankysoriano.rainbow.core.drawing.RainbowDrawer;
import com.juankysoriano.rainbow.core.event.RainbowInputController;

import zenproject.meditation.android.ContextRetriever;
import zenproject.meditation.android.R;
import zenproject.meditation.android.model.InkDropSizeLimiter;

public class ZenSketch extends Rainbow implements RainbowInputController.RainbowInteractionListener {

    private static final int DEFAULT_COLOR = ContextRetriever.INSTANCE.getCurrentContext().getResources().getColor(R.color.colorSketch);
    private final RainbowInputController rainbowInputController;
    private final RainbowDrawer rainbowDrawer;
    private final BranchesList branchesList;
    private final InkDropSizeLimiter inkDropSizeLimiter;
    private OnPaintingListener onPaintingListener;
    private StepDrawer inkDrawer;
    private StepDrawer eraserDrawer;
    private StepDrawer branchDrawer;

    ZenSketch(
            RainbowDrawer rainbowDrawer,
            RainbowInputController rainbowInputController, BranchesList branches, InkDropSizeLimiter inkDropSizeLimiter) {
        super(rainbowDrawer, rainbowInputController);
        this.rainbowDrawer = rainbowDrawer;
        this.rainbowInputController = rainbowInputController;
        this.branchesList = branches;
        this.inkDropSizeLimiter = inkDropSizeLimiter;
    }

    public static ZenSketch newInstance() {
        RainbowDrawer rainbowDrawer = new RainbowDrawer();
        RainbowInputController rainbowInputController = new RainbowInputController();
        return new ZenSketch(rainbowDrawer, rainbowInputController, BranchesList.newInstance(), new InkDropSizeLimiter());
    }

    @Override
    public void onSketchSetup() {
        this.inkDrawer = InkDrawer.newInstance(branchesList, inkDropSizeLimiter, rainbowDrawer, rainbowInputController);
        this.eraserDrawer = EraserDrawer.newInstance(rainbowDrawer, rainbowInputController);
        this.branchDrawer = BranchesDrawer.newInstance(branchesList, rainbowDrawer);
        rainbowInputController.setRainbowInteractionListener(this);
    }

    @Override
    public void onDrawingStep() {
        branchDrawer.paintStep();
    }

    @Override
    public void onSketchTouched(MotionEvent event, RainbowDrawer rainbowDrawer) {
        if (hasOnPaintingListener()) {
            onPaintingListener.onPaintingStart();
        }
        inkDrawer.reset();
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
        inkDrawer.paintStep();
        eraserDrawer.paintStep();
    }

    @Override
    public void onMotionEvent(MotionEvent event, RainbowDrawer rainbowDrawer) {
        //no-op
    }

    public void selectPainting() {
        disableErasing();
        enablePainting();
    }

    private void disableErasing() {
        eraserDrawer.disable();
    }

    private void enablePainting() {
        inkDrawer.enable();
    }

    public void selectErasing() {
        disablePainting();
        enableErasing();
    }

    private void enableErasing() {
        eraserDrawer.enable();
    }

    private void disablePainting() {
        inkDrawer.disable();
    }

    public void clear() {
        rainbowDrawer.background(DEFAULT_COLOR);
    }

    public void setOnPaintingListener(OnPaintingListener onPaintingListener) {
        this.onPaintingListener = onPaintingListener;
    }

    public interface OnPaintingListener {
        void onPaintingStart();

        void onPaintingEnd();
    }
}
