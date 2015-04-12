package zenproject.meditation.android.sketch.painting.flowers.branch;

import android.os.AsyncTask;

import com.juankysoriano.rainbow.core.drawing.RainbowDrawer;
import com.juankysoriano.rainbow.utils.RainbowMath;
import com.novoda.notils.exception.DeveloperError;

import java.util.ArrayList;
import java.util.List;

import zenproject.meditation.android.ContextRetriever;
import zenproject.meditation.android.R;
import zenproject.meditation.android.persistence.BrushOptionsPreferences;
import zenproject.meditation.android.persistence.FlowerOptionPreferences;
import zenproject.meditation.android.sketch.actions.StepPerformer;
import zenproject.meditation.android.sketch.painting.PaintStepSkipper;
import zenproject.meditation.android.sketch.painting.flowers.Flower;
import zenproject.meditation.android.sketch.painting.flowers.FlowerDrawer;
import zenproject.meditation.android.ui.menu.dialogs.flower.FlowerSelectedListener;

public class BranchPerformer implements StepPerformer, FlowerSelectedListener {
    private static final int ALPHA = 128;
    private static final float MAX_THRESHOLD = 100;
    private static final float BLOOM_THRESHOLD = .90f * MAX_THRESHOLD;
    private static final float FLOWER_THRESHOLD = .80f * MAX_THRESHOLD;
    private static final int LEAF_COLOR = ContextRetriever.INSTANCE.getResources().getColor(R.color.colorPrimaryDark);
    private static final float LEAF_SIZE = ContextRetriever.INSTANCE.getResources().getDimension(R.dimen.branch_default_radius) * 2;

    private final RainbowDrawer rainbowDrawer;
    private final BranchesList branchesList;
    private final PaintStepSkipper paintStepSkipper;
    private final BrushOptionsPreferences brushOptionsPreferences;
    private FlowerDrawer flowerDrawer;
    private boolean enabled = true;
    private boolean initialised;

    protected BranchPerformer(BranchesList branchesList,
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

    public static BranchPerformer newInstance(BranchesList branchesList, RainbowDrawer rainbowDrawer) {
        return new BranchPerformer(branchesList,
                FlowerDrawer.newInstance(FlowerOptionPreferences.newInstance().getFlower(), rainbowDrawer),
                rainbowDrawer,
                new PaintStepSkipper(),
                BrushOptionsPreferences.newInstance());
    }

    @Override
    public void init() {
        synchronized (this) {
            if (initialised) {
                throw new DeveloperError("You don't really want init this if it was already initialised");
            }
            initialised = true;
            configureDrawer();
            flowerDrawer.init();
        }
    }

    private void configureDrawer() {
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
        return flowerDrawer != null;
    }

    private void paintAndUpdateBranches() {
        List<Branch> branchList = new ArrayList<>(branchesList.asList());
        for (Branch branch : branchList) {
            paintAndUpdateBranch(branch);
        }
    }

    private void paintAndUpdateBranch(Branch branch) {
        if (branch.isDead()) {
            branchesList.prune(branch);
            bloomFlowerIfLuck(branch);
        } else {
            performBranchPainting(branch);
            branch.update();
            bloomBranchIfLuck(branch);
        }
    }

    private void paintFlowerFor(Branch branch) {
        flowerDrawer.paintFlowerFor(branch);
    }

    private void performBranchPainting(Branch branch) {
        rainbowDrawer.stroke(brushOptionsPreferences.getBranchColor(), ALPHA);
        rainbowDrawer.line(branch.getX(), branch.getY(), branch.getOldX(), branch.getOldY());
    }

    private void bloomBranchIfLuck(Branch branch) {
        if (RainbowMath.random(MAX_THRESHOLD) > BLOOM_THRESHOLD) {
            branchesList.bloomFrom(branch);
        }
    }

    private void bloomFlowerIfLuck(Branch branch) {
        if (RainbowMath.random(MAX_THRESHOLD) > FLOWER_THRESHOLD) {
            paintFlowerFor(branch);
        } else {
            paintLeafFor(branch);
        }
    }

    private void paintLeafFor(Branch branch) {
        rainbowDrawer.strokeWeight(LEAF_SIZE);
        rainbowDrawer.stroke(LEAF_COLOR, ALPHA);
        rainbowDrawer.point(branch.getX(), branch.getY());
        rainbowDrawer.strokeWeight(1);
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
    public void onFlowerSelected(final Flower flower) {
        new AsyncTask<Flower, Void, Void>() {
            @Override
            protected Void doInBackground(Flower... flowers) {
                flowerDrawer = FlowerDrawer.newInstance(flowers[0], rainbowDrawer);
                flowerDrawer.init();
                return null;
            }
        }.execute(flower);
    }
}
