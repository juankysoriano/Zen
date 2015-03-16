package zenproject.meditation.android.drawers;

import com.juankysoriano.rainbow.core.drawing.RainbowDrawer;
import com.juankysoriano.rainbow.core.graphics.RainbowGraphics;
import com.juankysoriano.rainbow.core.graphics.RainbowImage;
import com.juankysoriano.rainbow.utils.RainbowMath;

import java.util.ArrayList;
import java.util.List;

import zenproject.meditation.android.R;
import zenproject.meditation.android.preferences.BrushOptionsPreferences;

public class BranchesPerformer implements StepPerformer, RainbowImage.LoadPictureListener {
    private static final RainbowImage NO_IMAGE = null;
    private static final int ALPHA = 225;
    private static final int WHITE = 255;
    private static final float MAX_THRESHOLD = 100;
    private static final float SPROUD_THRESHOLD = .9f * MAX_THRESHOLD;
    private static final float FLOWER_THRESHOLD = .5f * MAX_THRESHOLD;
    private static final int FRAMES_TO_SKIP = 0;
    private static final int MIN_FLOWER_SIZE = 15;
    private static final int MAX_FLOWER_SIZE = 80;
    private final RainbowDrawer rainbowDrawer;
    private final BranchesList branchesList;
    private final PaintStepSkipper paintStepSkipper;
    private final BrushOptionsPreferences brushOptionsPreferences;
    private boolean enabled = true;
    private RainbowImage image;

    protected BranchesPerformer(BranchesList branchesList,
                                RainbowDrawer rainbowDrawer,
                                PaintStepSkipper paintStepSkipper,
                                BrushOptionsPreferences brushOptionsPreferences) {
        this.branchesList = branchesList;
        this.rainbowDrawer = rainbowDrawer;
        this.paintStepSkipper = paintStepSkipper;
        this.brushOptionsPreferences = brushOptionsPreferences;
    }

    public static BranchesPerformer newInstance(BranchesList branchesList, RainbowDrawer rainbowDrawer) {
        BranchesPerformer branchesDrawer = new BranchesPerformer(branchesList,
                rainbowDrawer,
                new PaintStepSkipper(FRAMES_TO_SKIP),
                BrushOptionsPreferences.newInstance());
        configureDrawer(rainbowDrawer);
        rainbowDrawer.loadImage(R.drawable.flower, RainbowImage.LOAD_ORIGINAL_SIZE, branchesDrawer);

        return branchesDrawer;
    }

    private static void configureDrawer(RainbowDrawer rainbowDrawer) {
        rainbowDrawer.noStroke();
        rainbowDrawer.smooth();
    }

    @Override
    public void onLoadSucceed(RainbowImage imageLoaded) {
        image = imageLoaded;
    }

    @Override
    public void onLoadFail() {
        image = NO_IMAGE;
    }

    @Override
    public void doStep() {
        if (enabled) {
            if (!paintStepSkipper.hasToSkipStep()) {
                paintAndUpdateBranches();
            }
            paintStepSkipper.recordStep();
        }

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
        float flowerSize = RainbowMath.random(MIN_FLOWER_SIZE, MAX_FLOWER_SIZE);
        rainbowDrawer.tint(WHITE, ALPHA);
        rainbowDrawer.imageMode(RainbowGraphics.CENTER);
        rainbowDrawer.pushMatrix();
        rainbowDrawer.translate(branch.getX(), branch.getY());
        rainbowDrawer.rotate(RainbowMath.random(RainbowMath.TWO_PI));
        rainbowDrawer.image(image, 0, 0, flowerSize, flowerSize);
        rainbowDrawer.popMatrix();
    }

    private void performBranchPainting(Branch branch) {
        rainbowDrawer.stroke(brushOptionsPreferences.getBrushColor(), ALPHA);
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

}
