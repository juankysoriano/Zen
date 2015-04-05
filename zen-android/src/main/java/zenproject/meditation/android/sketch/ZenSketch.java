package zenproject.meditation.android.sketch;

import android.view.MotionEvent;

import com.juankysoriano.rainbow.core.Rainbow;
import com.juankysoriano.rainbow.core.drawing.RainbowDrawer;
import com.juankysoriano.rainbow.core.event.RainbowInputController;

import zenproject.meditation.android.analytics.AnalyticsTracker;
import zenproject.meditation.android.ContextRetriever;
import zenproject.meditation.android.R;
import zenproject.meditation.android.sketch.painting.ink.InkDropSizeLimiter;
import zenproject.meditation.android.sketch.painting.flowers.Flower;
import zenproject.meditation.android.sketch.actions.StepPerformer;
import zenproject.meditation.android.sketch.painting.flowers.branch.BranchPerformer;
import zenproject.meditation.android.sketch.painting.flowers.branch.BranchesList;
import zenproject.meditation.android.sketch.painting.ink.InkPerformer;
import zenproject.meditation.android.sketch.music.MusicPerformer;
import zenproject.meditation.android.ui.menu.dialogs.flower.FlowerSelectedListener;

public class ZenSketch extends Rainbow implements RainbowInputController.RainbowInteractionListener, FlowerSelectedListener {

    private static final int DEFAULT_COLOR = ContextRetriever.INSTANCE.getResources().getColor(R.color.colorSketch);
    private final RainbowInputController rainbowInputController;
    private final RainbowDrawer rainbowDrawer;
    private final BranchesList branchesList;
    private final InkDropSizeLimiter inkDropSizeLimiter;
    private OnPaintingListener onPaintingListener;
    private StepPerformer inkDrawer;
    private StepPerformer branchDrawer;
    private StepPerformer musicPerformer;

    protected ZenSketch(
            MusicPerformer musicPerformer, BranchesList branches, InkDropSizeLimiter inkDropSizeLimiter, RainbowDrawer rainbowDrawer,
            RainbowInputController rainbowInputController) {
        super(rainbowDrawer, rainbowInputController);
        this.musicPerformer = musicPerformer;
        this.branchesList = branches;
        this.inkDropSizeLimiter = inkDropSizeLimiter;
        this.rainbowDrawer = rainbowDrawer;
        this.rainbowInputController = rainbowInputController;
    }

    public static ZenSketch newInstance() {
        RainbowDrawer rainbowDrawer = new RainbowDrawer();
        RainbowInputController rainbowInputController = new RainbowInputController();

        return new ZenSketch(MusicPerformer.newInstance(rainbowInputController),
                BranchesList.newInstance(),
                InkDropSizeLimiter.newInstance(),
                rainbowDrawer,
                rainbowInputController);
    }

    @Override
    public void onSketchSetup() {
        this.inkDrawer = InkPerformer.newInstance(branchesList, inkDropSizeLimiter, rainbowDrawer, rainbowInputController);
        this.branchDrawer = BranchPerformer.newInstance(branchesList, rainbowDrawer);
        this.rainbowInputController.setRainbowInteractionListener(this);
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
    }

    @Override
    public void onMotionEvent(MotionEvent event, RainbowDrawer rainbowDrawer) {
        //no-op
    }

    public void clear() {
        rainbowDrawer.background(DEFAULT_COLOR);
        branchesList.clear();
    }

    public void setOnPaintingListener(OnPaintingListener onPaintingListener) {
        this.onPaintingListener = onPaintingListener;
    }

    @Override
    public void onFlowerSelected(Flower flower) {
        ((BranchPerformer) branchDrawer).onFlowerSelected(flower);
    }

    public interface OnPaintingListener {
        void onPaintingStart();

        void onPaintingEnd();
    }
}
