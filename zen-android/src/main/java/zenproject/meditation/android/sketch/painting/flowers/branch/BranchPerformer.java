package zenproject.meditation.android.sketch.painting.flowers.branch;

import android.os.AsyncTask;

import com.juankysoriano.rainbow.core.drawing.RainbowDrawer;
import com.juankysoriano.rainbow.core.event.RainbowInputController;
import com.juankysoriano.rainbow.utils.RainbowMath;
import com.novoda.notils.exception.DeveloperError;

import java.util.ArrayList;
import java.util.List;

import zenproject.meditation.android.ContextRetriever;
import zenproject.meditation.android.R;
import zenproject.meditation.android.preferences.BrushOptionsPreferences;
import zenproject.meditation.android.preferences.FlowerOptionPreferences;
import zenproject.meditation.android.sketch.actions.StepPerformer;
import zenproject.meditation.android.sketch.painting.PaintStepSkipper;
import zenproject.meditation.android.sketch.painting.flowers.Flower;
import zenproject.meditation.android.sketch.painting.flowers.FlowerDrawer;
import zenproject.meditation.android.sketch.painting.ink.BrushColor;
import zenproject.meditation.android.sketch.painting.ink.InkDrop;
import zenproject.meditation.android.ui.menu.dialogs.flower.FlowerSelectedListener;

public class BranchPerformer implements StepPerformer, FlowerSelectedListener {
    private static final int ALPHA = 128;
    private static final int MAX_THRESHOLD = 100;
    private static final int BLOOM_THRESHOLD = 90;
    private static final float INK_VELOCITY_THRESHOLD = ContextRetriever.INSTANCE.getResources().getDimension(R.dimen.ink_velocity_threshold);
    private static final int BRANCH_THRESHOLD_FAST = 73;
    private static final int BRANCH_THRESHOLD_SLOW = 93;
    private static final int FLOWER_THRESHOLD = 70;
    private static final float LEAF_SIZE = ContextRetriever.INSTANCE.getResources().getDimension(R.dimen.branch_default_radius) * 2;

    private final RainbowDrawer rainbowDrawer;
    private final BranchesList branchesList;
    private final PaintStepSkipper paintStepSkipper;
    private final BrushOptionsPreferences brushOptionsPreferences;
    private final InkDrop inkDrop;
    private final RainbowInputController rainbowInputController;
    private FlowerDrawer flowerDrawer;
    private boolean enabled = true;
    private boolean initialised;

    protected BranchPerformer(InkDrop inkDrop,
                              BranchesList branchesList,
                              FlowerDrawer flowerDrawer,
                              PaintStepSkipper paintStepSkipper,
                              BrushOptionsPreferences brushOptionsPreferences,
                              RainbowDrawer rainbowDrawer,
                              RainbowInputController rainbowInputController) {
        this.inkDrop = inkDrop;
        this.branchesList = branchesList;
        this.flowerDrawer = flowerDrawer;
        this.rainbowDrawer = rainbowDrawer;
        this.paintStepSkipper = paintStepSkipper;
        this.brushOptionsPreferences = brushOptionsPreferences;
        this.rainbowInputController = rainbowInputController;
    }

    public static BranchPerformer newInstance(InkDrop inkDrop,
                                              RainbowDrawer rainbowDrawer,
                                              RainbowInputController rainbowInputController) {
        BrushOptionsPreferences brushOptionsPreferences = BrushOptionsPreferences.newInstance();
        BranchesList branchesList = BranchesList.newInstance();
        FlowerOptionPreferences flowerOptionPreferences = FlowerOptionPreferences.newInstance();
        Flower flower = flowerOptionPreferences.getFlower();
        FlowerDrawer flowerDrawer = FlowerDrawer.newInstance(flower, rainbowDrawer);
        return new BranchPerformer(inkDrop,
                branchesList,
                flowerDrawer,
                new PaintStepSkipper(), brushOptionsPreferences, rainbowDrawer,
                rainbowInputController);
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
            attemptToBloomBranch();
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

    private void bloomFlowerIfLuck(Branch branch) {
        if (RainbowMath.random(MAX_THRESHOLD) > FLOWER_THRESHOLD) {
            paintFlowerFor(branch);
        } else {
            paintLeafFor(branch);
        }
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

    private void paintFlowerFor(Branch branch) {
        flowerDrawer.paintFlowerFor(branch);
    }

    private void paintLeafFor(Branch branch) {
        rainbowDrawer.strokeWeight(LEAF_SIZE);
        rainbowDrawer.stroke(brushOptionsPreferences.getLeafColor(), ALPHA);
        rainbowDrawer.point(branch.getX(), branch.getY());
        rainbowDrawer.strokeWeight(1);
    }

    private void attemptToBloomBranch() {
        float bloomBranchThreshold = rainbowInputController.getFingerVelocity() > INK_VELOCITY_THRESHOLD
                ? BRANCH_THRESHOLD_FAST
                : BRANCH_THRESHOLD_SLOW;
        if (!isErasing() && rainbowInputController.isScreenTouched() && RainbowMath.random(MAX_THRESHOLD) > bloomBranchThreshold) {
            createBranchAt(rainbowInputController.getSmoothX(), rainbowInputController.getSmoothY());
        }
    }

    private void createBranchAt(float x, float y) {
        RainbowInputController.MovementDirection verticalMovement = rainbowInputController.getVerticalDirection();
        RainbowInputController.MovementDirection horizontalMovement = rainbowInputController.getHorizontalDirection();
        float radius = inkDrop.getRadius() / 4;
        float verticalOffset = verticalMovement == RainbowInputController.MovementDirection.DOWN ? radius : -radius;
        float horizontalOffset = horizontalMovement == RainbowInputController.MovementDirection.RIGHT ? radius : -radius;

        branchesList.bloomFrom(Branch.createAt(x + horizontalOffset, y + verticalOffset));
    }

    @Override
    public void enable() {
        enabled = true;
    }

    @Override
    public void disable() {
        enabled = false;
    }

    @Override
    public void reset() {
        branchesList.clear();
    }

    private boolean isErasing() {
        return BrushColor.ERASE == brushOptionsPreferences.getBrushColor();
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
