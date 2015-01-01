package zenproject.meditation.android.drawers;

import com.juankysoriano.rainbow.core.drawing.RainbowDrawer;
import com.juankysoriano.rainbow.utils.RainbowMath;

import java.util.ArrayList;
import java.util.List;

public class BranchesDrawer implements StepDrawer {
    private static final int FRAMES_TO_SKIP = 0;
    private final RainbowDrawer rainbowDrawer;
    private final BranchesList branchesList;
    private final PaintStepSkipper paintStepSkipper;
    private boolean enabled = true;

    private BranchesDrawer(BranchesList branchesList, RainbowDrawer rainbowDrawer, PaintStepSkipper paintStepSkipper) {
        this.branchesList = branchesList;
        this.rainbowDrawer = rainbowDrawer;
        this.paintStepSkipper = paintStepSkipper;
    }

    public static BranchesDrawer newInstance(BranchesList branchesList, RainbowDrawer rainbowDrawer) {
        BranchesDrawer branchesDrawer = new BranchesDrawer(branchesList, rainbowDrawer, new PaintStepSkipper(FRAMES_TO_SKIP));
        configureDrawer(rainbowDrawer);
        return branchesDrawer;
    }

    private static void configureDrawer(RainbowDrawer rainbowDrawer) {
        rainbowDrawer.noStroke();
        rainbowDrawer.smooth();
    }

    @Override
    public void paintStep() {
        if (enabled) {
            if(!paintStepSkipper.hasToSkipStep()) {
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
        if (!branch.isDead()) {
            performBranchPainting(branch);
            branch.update();
            sproudBranchIfLuck(branch);
        } else {
            branchesList.prune(branch);
        }
    }

    private void performBranchPainting(Branch branch) {
        rainbowDrawer.stroke(0, 200);
        rainbowDrawer.line(branch.getX(), branch.getY(), branch.getOldX(), branch.getOldY());
    }

    private void sproudBranchIfLuck(Branch branch) {
        if (RainbowMath.random(100) > 94) {
            branchesList.sproudFrom(branch);
        }
    }

    @Override
    public void reset() {
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
