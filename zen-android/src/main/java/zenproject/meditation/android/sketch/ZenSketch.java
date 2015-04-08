package zenproject.meditation.android.sketch;

import android.view.MotionEvent;

import com.juankysoriano.rainbow.core.Rainbow;
import com.juankysoriano.rainbow.core.drawing.RainbowDrawer;
import com.juankysoriano.rainbow.core.event.RainbowInputController;
import com.novoda.notils.caster.Classes;

import zenproject.meditation.android.ContextRetriever;
import zenproject.meditation.android.R;
import zenproject.meditation.android.sketch.actions.StepPerformer;
import zenproject.meditation.android.sketch.music.MusicPerformer;
import zenproject.meditation.android.sketch.painting.flowers.Flower;
import zenproject.meditation.android.sketch.painting.flowers.branch.BranchPerformer;
import zenproject.meditation.android.sketch.painting.flowers.branch.BranchesList;
import zenproject.meditation.android.sketch.painting.ink.InkPerformer;
import zenproject.meditation.android.ui.menu.dialogs.flower.FlowerSelectedListener;

/**
 * Orchestration class for all the performers responsible for specific artistic (lol) operations.
 */
public class ZenSketch extends Rainbow implements RainbowInputController.RainbowInteractionListener, FlowerSelectedListener {

    private static final int DEFAULT_COLOR = ContextRetriever.INSTANCE.getResources().getColor(R.color.colorSketch);
    private final RainbowInputController rainbowInputController;
    private final RainbowDrawer rainbowDrawer;
    private final BranchesList branchesList;
    private OnPaintingListener onPaintingListener;
    private StepPerformer inkPerformer;
    private StepPerformer branchPerformer;
    private StepPerformer musicPerformer;

    protected ZenSketch(
            MusicPerformer musicPerformer, InkPerformer inkPerformer, BranchPerformer branchPerformer, BranchesList branches, RainbowDrawer rainbowDrawer,
            RainbowInputController rainbowInputController) {
        super(rainbowDrawer, rainbowInputController);
        this.musicPerformer = musicPerformer;
        this.inkPerformer = inkPerformer;
        this.branchPerformer = branchPerformer;
        this.branchesList = branches;
        this.rainbowDrawer = rainbowDrawer;
        this.rainbowInputController = rainbowInputController;
    }

    public static ZenSketch newInstance() {
        RainbowDrawer rainbowDrawer = new RainbowDrawer();
        RainbowInputController rainbowInputController = new RainbowInputController();
        BranchesList branchesList = BranchesList.newInstance();

        return new ZenSketch(MusicPerformer.newInstance(rainbowInputController),
                InkPerformer.newInstance(branchesList, rainbowDrawer, rainbowInputController),
                BranchPerformer.newInstance(branchesList, rainbowDrawer),
                BranchesList.newInstance(),
                rainbowDrawer,
                rainbowInputController);
    }

    @Override
    public void onSketchSetup() {
        this.inkPerformer.init();
        this.branchPerformer.init();
        this.musicPerformer.init();
        this.rainbowInputController.setRainbowInteractionListener(this);
    }

    @Override
    public void onDrawingStep() {
        branchPerformer.doStep();
        musicPerformer.doStep();
    }

    @Override
    public void onDrawingPause() {
        musicPerformer.disable();
    }

    @Override
    public void onDrawingResume() {
        musicPerformer.enable();
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

    public void clear() {
        rainbowDrawer.background(DEFAULT_COLOR);
        branchesList.clear();
    }

    public void setOnPaintingListener(OnPaintingListener onPaintingListener) {
        this.onPaintingListener = onPaintingListener;
    }

    @Override
    public void onFlowerSelected(Flower flower) {
        BranchPerformer branchPerformer = Classes.from(this.branchPerformer);
        branchPerformer.onFlowerSelected(flower);
    }

    public interface OnPaintingListener {
        void onPaintingStart();

        void onPaintingEnd();
    }
}
