package zenproject.meditation.android.sketch.performers.flowers;

import android.os.AsyncTask;

import com.juankysoriano.rainbow.core.drawing.RainbowDrawer;
import com.juankysoriano.rainbow.utils.RainbowMath;

import java.util.ArrayList;
import java.util.List;

import zenproject.meditation.android.preferences.BrushOptionsPreferences;
import zenproject.meditation.android.preferences.FlowerOptionPreferences;
import zenproject.meditation.android.sketch.performers.StepPerformer;
import zenproject.meditation.android.views.dialogs.flower.FlowerSelectedListener;

public class BranchesPerformer implements StepPerformer, FlowerSelectedListener {
    private static final int ALPHA = 225;
    private static final float MAX_THRESHOLD = 100;
    private static final float SPROUD_THRESHOLD = .9f * MAX_THRESHOLD;
    private static final float FLOWER_THRESHOLD = .5f * MAX_THRESHOLD;
    private static final int FRAMES_TO_SKIP = 0;
    private final RainbowDrawer rainbowDrawer;
    private final BranchesList branchesList;
    private final PaintStepSkipper paintStepSkipper;
    private final BrushOptionsPreferences brushOptionsPreferences;
    private FlowerDrawer flowerDrawer;
    private boolean enabled = true;

    protected BranchesPerformer(BranchesList branchesList,
                                FlowerDrawer flowerDrawer,
                                RainbowDrawer rainbowDrawer,
                                PaintStepSkipper paintStepSkipper,
                                BrushOptionsPreferences brushOptionsPreferences) {
        this.branchesList = branchesList;
        this.flowerDrawer = flowerDrawer;
        this.rainbowDrawer = rainbowDrawer;
        this.paintStepSkipper = paintStepSkipper;
        this.brushOptionsPreferences = brushOptionsPreferences;
    }

    public static BranchesPerformer newInstance(BranchesList branchesList, RainbowDrawer rainbowDrawer) {
        BranchesPerformer branchesDrawer = new BranchesPerformer(branchesList,
                FlowerDrawer.from(FlowerOptionPreferences.newInstance().getFlower(), rainbowDrawer),
                rainbowDrawer,
                new PaintStepSkipper(FRAMES_TO_SKIP),
                BrushOptionsPreferences.newInstance());
        configureDrawer(rainbowDrawer);
        return branchesDrawer;
    }

    private static void configureDrawer(RainbowDrawer rainbowDrawer) {
        rainbowDrawer.noStroke();
        rainbowDrawer.smooth();
    }

    @Override
    public void doStep() {
        if (enabled) {
            if (!paintStepSkipper.hasToSkipStep() && hasFlower()) {
                paintAndUpdateBranches();
            }
            paintStepSkipper.recordStep();
        }

    }

    private boolean hasFlower() {
        return !(flowerDrawer instanceof NullFlowerDrawer) && flowerDrawer != null;
    }

    private void paintAndUpdateBranches() {
        List<Branch> branchList = new ArrayList<>(branchesList.getBranchesList());
        for (Branch branch : branchList) {
            paintAndUpdateBranch(branch);
        }
    }

    private void paintAndUpdateBranch(Branch branch) {
        if (branch.isDead()) {
            branchesList.prune(branch);
            sproudFlowerIfLuck(branch);
        } else {
            performBranchPainting(branch);
            branch.update();
            sproudBranchIfLuck(branch);
        }
    }

    private void paintFlowerFor(Branch branch) {
        flowerDrawer.paintFlowerFor(branch);
    }

    private void performBranchPainting(Branch branch) {
        rainbowDrawer.stroke(brushOptionsPreferences.getBranchColor(), ALPHA);
        rainbowDrawer.line(branch.getX(), branch.getY(), branch.getOldX(), branch.getOldY());
    }

    private void sproudBranchIfLuck(Branch branch) {
        if (RainbowMath.random(MAX_THRESHOLD) > SPROUD_THRESHOLD) {
            branchesList.sproudFrom(branch);
        }
    }

    private void sproudFlowerIfLuck(Branch branch) {
        if (RainbowMath.random(MAX_THRESHOLD) > FLOWER_THRESHOLD) {
            paintFlowerFor(branch);
        }
    }

    @Override
    public void reset() {
        //no-op
    }

    @Override
    public void disable() {
        enabled = false;
    }

    @Override
    public void enable() {
        enabled = true;
    }

    @Override
    public void onFlowerSelected(final FlowerDrawer.Flower flower) {
        new AsyncTask<FlowerDrawer.Flower, Void, Void>() {
            @Override
            protected Void doInBackground(FlowerDrawer.Flower... flowers) {
                flowerDrawer = FlowerDrawer.from(flowers[0], rainbowDrawer);
                return null;
            }
        }.execute(flower);
    }
}
