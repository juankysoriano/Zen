package zenproject.meditation.android.sketch;

import android.view.MotionEvent;

import com.juankysoriano.rainbow.core.Rainbow;
import com.juankysoriano.rainbow.core.drawing.RainbowDrawer;
import com.juankysoriano.rainbow.core.event.RainbowInputController;

import zenproject.meditation.android.AnalyticsTracker;
import zenproject.meditation.android.ContextRetriever;
import zenproject.meditation.android.R;
import zenproject.meditation.android.model.InkDropSizeLimiter;
import zenproject.meditation.android.preferences.Flower;
import zenproject.meditation.android.sketch.performers.StepPerformer;
import zenproject.meditation.android.sketch.performers.flowers.BranchesList;
import zenproject.meditation.android.sketch.performers.flowers.BranchesPerformer;
import zenproject.meditation.android.sketch.performers.ink.InkPerformer;
import zenproject.meditation.android.sketch.performers.music.MusicPerformer;
import zenproject.meditation.android.views.dialogs.flower.FlowerSelectedListener;

public class ZenSketch extends Rainbow implements RainbowInputController.RainbowInteractionListener, FlowerSelectedListener {

    private static final int DEFAULT_COLOR = ContextRetriever.INSTANCE.getCurrentResources().getColor(R.color.colorSketch);
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
        this.branchDrawer = BranchesPerformer.newInstance(branchesList, rainbowDrawer);
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
            musicPerformer.reset();
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
        AnalyticsTracker.INSTANCE.trackClearSketch();
        rainbowDrawer.background(DEFAULT_COLOR);
        branchesList.clear();
    }

    public void setOnPaintingListener(OnPaintingListener onPaintingListener) {
        this.onPaintingListener = onPaintingListener;
    }

    @Override
    public void onFlowerSelected(Flower flower) {
        ((BranchesPerformer) branchDrawer).onFlowerSelected(flower);
    }

    public interface OnPaintingListener {
        void onPaintingStart();

        void onPaintingEnd();
    }
}
