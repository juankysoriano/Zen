package zenproject.meditation.android.sketch;

import android.view.MotionEvent;

import com.juankysoriano.rainbow.core.Rainbow;
import com.juankysoriano.rainbow.core.drawing.RainbowDrawer;
import com.juankysoriano.rainbow.core.event.RainbowInputController;

import zenproject.meditation.android.ContextRetriever;
import zenproject.meditation.android.R;
import zenproject.meditation.android.model.InkDropSizeLimiter;
import zenproject.meditation.android.sketch.performers.flowers.BranchesList;
import zenproject.meditation.android.sketch.performers.flowers.BranchesPerformer;
import zenproject.meditation.android.sketch.performers.eraser.EraserPerformer;
import zenproject.meditation.android.sketch.performers.ink.InkPerformer;
import zenproject.meditation.android.sketch.performers.music.MusicPerformer;
import zenproject.meditation.android.sketch.performers.StepPerformer;
import zenproject.meditation.android.views.dialogs.brush.ColorSelectedListener;
import zenproject.meditation.android.views.dialogs.brush.SizeChangedListener;

public class ZenSketch extends Rainbow implements RainbowInputController.RainbowInteractionListener, ColorSelectedListener, SizeChangedListener {

    private static final int DEFAULT_COLOR = ContextRetriever.INSTANCE.getCurrentResources().getColor(R.color.colorSketch);
    private final RainbowInputController rainbowInputController;
    private final RainbowDrawer rainbowDrawer;
    private final BranchesList branchesList;
    private final InkDropSizeLimiter inkDropSizeLimiter;
    private OnPaintingListener onPaintingListener;
    private StepPerformer inkDrawer;
    private StepPerformer eraserDrawer;
    private StepPerformer branchDrawer;
    private StepPerformer musicPerformer;

    protected ZenSketch(
            RainbowDrawer rainbowDrawer,
            RainbowInputController rainbowInputController,
            BranchesList branches,
            InkDropSizeLimiter inkDropSizeLimiter) {
        super(rainbowDrawer, rainbowInputController);
        this.rainbowDrawer = rainbowDrawer;
        this.rainbowInputController = rainbowInputController;
        this.branchesList = branches;
        this.inkDropSizeLimiter = inkDropSizeLimiter;
    }

    public static ZenSketch newInstance() {
        RainbowDrawer rainbowDrawer = new RainbowDrawer();
        RainbowInputController rainbowInputController = new RainbowInputController();
        return new ZenSketch(rainbowDrawer, rainbowInputController, BranchesList.newInstance(), InkDropSizeLimiter.newInstance());
    }

    @Override
    public void onSketchSetup() {
        this.inkDrawer = InkPerformer.newInstance(branchesList, inkDropSizeLimiter, rainbowDrawer, rainbowInputController);
        this.eraserDrawer = EraserPerformer.newInstance(rainbowDrawer, rainbowInputController);
        this.branchDrawer = BranchesPerformer.newInstance(branchesList, rainbowDrawer);
        this.musicPerformer = MusicPerformer.newInstance(rainbowInputController);
        rainbowInputController.setRainbowInteractionListener(this);
    }

    @Override
    public void onDrawingStep() {
        branchDrawer.doStep();
        musicPerformer.doStep();
    }

    @Override
    public void onDrawingPause() {
        if (hasMusicPerformer()) {
            musicPerformer.disable();
        }
    }

    @Override
    public void onDrawingResume() {
        if (hasMusicPerformer()) {
            musicPerformer.enable();
        }
    }

    @Override
    public void onSketchDestroy() {
        ((MusicPerformer) musicPerformer).release();
    }

    private boolean hasMusicPerformer() {
        return musicPerformer != null;
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
        inkDrawer.doStep();
        eraserDrawer.doStep();
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

    @Override
    public void onColorSelected(int color) {
        ((InkPerformer) inkDrawer).setSelectedColorTo(color);
    }

    @Override
    public void onSizeChanged(int percentage) {
        inkDropSizeLimiter.setScaleFactor(percentage);
    }

    public interface OnPaintingListener {
        void onPaintingStart();

        void onPaintingEnd();
    }
}
