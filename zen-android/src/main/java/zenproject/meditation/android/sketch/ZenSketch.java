package zenproject.meditation.android.sketch;

import com.juankysoriano.rainbow.core.Rainbow;
import com.juankysoriano.rainbow.core.drawing.RainbowDrawer;
import com.juankysoriano.rainbow.core.event.RainbowInputController;
import com.novoda.notils.caster.Classes;

import zenproject.meditation.android.ContextRetriever;
import zenproject.meditation.android.R;
import zenproject.meditation.android.SketchRetriever;
import zenproject.meditation.android.sketch.actions.StepPerformer;
import zenproject.meditation.android.sketch.music.MusicPerformer;
import zenproject.meditation.android.sketch.painting.SketchInteractionListener;
import zenproject.meditation.android.sketch.painting.flowers.Flower;
import zenproject.meditation.android.sketch.painting.flowers.branch.BranchPerformer;
import zenproject.meditation.android.sketch.painting.ink.InkDrop;
import zenproject.meditation.android.sketch.painting.ink.InkPerformer;
import zenproject.meditation.android.ui.menu.dialogs.flower.FlowerSelectedListener;

/**
 * Orchestration class for all the performers responsible for specific artistic (lol) operations.
 */
public class ZenSketch extends Rainbow implements FlowerSelectedListener {

    private static final int DEFAULT_COLOR = ContextRetriever.INSTANCE.getResources().getColor(R.color.colorSketch);
    private final RainbowDrawer rainbowDrawer;
    private final StepPerformer inkPerformer;
    private final StepPerformer branchPerformer;
    private final StepPerformer musicPerformer;
    private final SketchInteractionListener sketchInteractionListener;

    protected ZenSketch(
            MusicPerformer musicPerformer,
            InkPerformer inkPerformer,
            BranchPerformer branchPerformer,
            RainbowDrawer rainbowDrawer,
            RainbowInputController rainbowInputController,
            SketchInteractionListener sketchInteractionListener) {
        super(rainbowDrawer, rainbowInputController);
        this.musicPerformer = musicPerformer;
        this.inkPerformer = inkPerformer;
        this.branchPerformer = branchPerformer;
        this.rainbowDrawer = rainbowDrawer;
        this.sketchInteractionListener = sketchInteractionListener;
    }

    public static ZenSketch newInstance() {
        RainbowDrawer rainbowDrawer = new RainbowDrawer();
        RainbowInputController rainbowInputController = RainbowInputController.newInstance();
        InkDrop inkDrop = InkDrop.newInstance(rainbowInputController);
        InkPerformer inkPerformer = InkPerformer.newInstance(inkDrop, rainbowInputController.getRainbowDrawer(), rainbowInputController);
        BranchPerformer branchPerformer = BranchPerformer.newInstance(inkDrop, rainbowDrawer, rainbowInputController);
        SketchInteractionListener sketchInteractionListener = SketchInteractionListener.newInstance(inkPerformer);
        rainbowInputController.setRainbowInteractionListener(sketchInteractionListener);

        ZenSketch zenSketch = new ZenSketch(MusicPerformer.newInstance(rainbowInputController),
                inkPerformer,
                branchPerformer,
                rainbowDrawer,
                rainbowInputController, sketchInteractionListener
        );

        SketchRetriever.INSTANCE.inject(zenSketch);

        return zenSketch;
    }

    @Override
    public void onSketchSetup() {
        inkPerformer.init();
        branchPerformer.init();
        musicPerformer.init();
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
        branchPerformer.reset();
        rainbowDrawer.background(DEFAULT_COLOR);
    }

    public void setOnPaintingListener(PaintListener paintListener) {
        sketchInteractionListener.setPaintListener(paintListener);
    }

    @Override
    public void onFlowerSelected(Flower flower) {
        BranchPerformer performer = Classes.from(branchPerformer);
        performer.onFlowerSelected(flower);
    }

    public interface PaintListener {
        void onPaintingStart();

        void onPaintingEnd();
    }
}
