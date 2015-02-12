package zenproject.meditation.android.drawers;

import android.view.ViewGroup;

import com.juankysoriano.rainbow.core.Rainbow;
import com.juankysoriano.rainbow.core.drawing.RainbowDrawer;
import com.juankysoriano.rainbow.core.event.RainbowEvent;
import com.juankysoriano.rainbow.core.event.RainbowInputController;

import zenproject.meditation.android.ContextRetriever;
import zenproject.meditation.android.R;
import zenproject.meditation.android.model.InkDropSizeLimiter;

public class ZenSketch extends Rainbow implements RainbowInputController.RainbowInteractionListener {

    private static final int DEFAULT_COLOR = ContextRetriever.INSTANCE.getContext().getResources().getColor(R.color.defaultBackground);
    private final RainbowInputController rainbowInputController;
    private final RainbowDrawer rainbowDrawer;
    private final BranchesList branchesList;
    private final InkDropSizeLimiter inkDropSizeLimiter;
    private OnPaintingListener onPaintingListener;
    private StepDrawer inkDrawer;
    private StepDrawer eraserDrawer;
    private StepDrawer branchDrawer;

    ZenSketch(ViewGroup viewGroup,
              RainbowDrawer rainbowDrawer,
              RainbowInputController rainbowInputController, BranchesList branches, InkDropSizeLimiter inkDropSizeLimiter) {
        super(viewGroup, rainbowDrawer, rainbowInputController);
        this.rainbowDrawer = rainbowDrawer;
        this.rainbowInputController = rainbowInputController;
        this.branchesList = branches;
        this.inkDropSizeLimiter = inkDropSizeLimiter;
    }

    public static ZenSketch newInstance(ViewGroup viewGroup) {
        RainbowDrawer rainbowDrawer = new RainbowDrawer();
        RainbowInputController rainbowInputController = new RainbowInputController();
        return new ZenSketch(viewGroup, rainbowDrawer, rainbowInputController, BranchesList.newInstance(), new InkDropSizeLimiter());
    }

    @Override
    public void onSketchSetup() {
        this.inkDrawer = InkDrawer.newInstance(branchesList, inkDropSizeLimiter, rainbowDrawer, rainbowInputController);
        this.eraserDrawer = EraserDrawer.newInstance(rainbowDrawer, rainbowInputController);
        this.branchDrawer = BranchesDrawer.newInstance(branchesList, rainbowDrawer);
        rainbowInputController.setRainbowInteractionListener(this);
    }

    @Override
    public void onDrawingStart() {
        super.onDrawingStart();
    }

    @Override
    public void onDrawingStep() {
        branchDrawer.paintStep();
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
        if (hasOnPaintingListener()) {
            onPaintingListener.onPaintingStart();
        }
        inkDrawer.reset();
    }

    private boolean hasOnPaintingListener() {
        return this.onPaintingListener != null;
    }

    @Override
    public void onSketchReleased(RainbowEvent event, RainbowDrawer rainbowDrawer) {
        if (hasOnPaintingListener()) {
            onPaintingListener.onPaintingEnd();
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
        branchDrawer.enable();
    }

    public void enableErasing() {
        eraserDrawer.enable();
    }

    public void disablePainting() {
        inkDrawer.disable();
        branchDrawer.disable();
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
