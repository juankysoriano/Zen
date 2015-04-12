package zenproject.meditation.android.sketch;

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
public class ZenSketch extends Rainbow implements FlowerSelectedListener {

    private static final int DEFAULT_COLOR = ContextRetriever.INSTANCE.getResources().getColor(R.color.colorSketch);
    private final RainbowInputController rainbowInputController;
    private final RainbowDrawer rainbowDrawer;
    private final BranchesList branchesList;
    private final StepPerformer inkPerformer;
    private final StepPerformer branchPerformer;
    private final StepPerformer musicPerformer;
    private final SketchInteractionListener sketchInteractionListener;

    protected ZenSketch(
            MusicPerformer musicPerformer,
            InkPerformer inkPerformer,
            BranchPerformer branchPerformer,
            BranchesList branches,
            RainbowDrawer rainbowDrawer,
            SketchInteractionListener sketchInteractionListener,
            RainbowInputController rainbowInputController) {
        super(rainbowDrawer, rainbowInputController);
        this.musicPerformer = musicPerformer;
        this.inkPerformer = inkPerformer;
        this.branchPerformer = branchPerformer;
        this.branchesList = branches;
        this.rainbowDrawer = rainbowDrawer;
        this.sketchInteractionListener = sketchInteractionListener;
        this.rainbowInputController = rainbowInputController;
    }

    public static ZenSketch newInstance() {
        RainbowDrawer rainbowDrawer = new RainbowDrawer();
        RainbowInputController rainbowInputController = new RainbowInputController();
        BranchesList branchesList = BranchesList.newInstance();
        InkPerformer inkPerformer = InkPerformer.newInstance(branchesList, rainbowDrawer, rainbowInputController);
        return new ZenSketch(MusicPerformer.newInstance(rainbowInputController),
                inkPerformer,
                BranchPerformer.newInstance(branchesList, rainbowDrawer),
                branchesList,
                rainbowDrawer,
                SketchInteractionListener.newInstance(inkPerformer),
                rainbowInputController);
    }

    @Override
    public void onSketchSetup() {
        this.inkPerformer.init();
        this.branchPerformer.init();
        this.musicPerformer.init();
        this.rainbowInputController.setRainbowInteractionListener(sketchInteractionListener);
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

    public void clear() {
        rainbowDrawer.background(DEFAULT_COLOR);
        branchesList.clear();
    }

    public void setOnPaintingListener(OnPaintingListener onPaintingListener) {
        sketchInteractionListener.setOnPaintingListener(onPaintingListener);
    }

    @Override
    public void onFlowerSelected(Flower flower) {
        BranchPerformer performer = Classes.from(this.branchPerformer);
        performer.onFlowerSelected(flower);
    }

    public interface OnPaintingListener {
        void onPaintingStart();

        void onPaintingEnd();
    }
}
