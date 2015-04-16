package zenproject.meditation.android.sketch.painting.ink;

import com.juankysoriano.rainbow.core.drawing.LineExplorer;
import com.juankysoriano.rainbow.core.drawing.RainbowDrawer;
import com.juankysoriano.rainbow.core.event.RainbowInputController;
import com.juankysoriano.rainbow.core.graphics.RainbowGraphics;
import com.juankysoriano.rainbow.core.graphics.RainbowImage;
import com.juankysoriano.rainbow.utils.RainbowMath;
import com.novoda.notils.exception.DeveloperError;

import zenproject.meditation.android.ContextRetriever;
import zenproject.meditation.android.R;
import zenproject.meditation.android.sketch.actions.StepPerformer;
import zenproject.meditation.android.sketch.painting.flowers.branch.Branch;
import zenproject.meditation.android.sketch.painting.flowers.branch.BranchesList;

import static com.juankysoriano.rainbow.core.event.RainbowInputController.MovementDirection;

public class InkPerformer implements StepPerformer, RainbowImage.LoadPictureListener {
    private static final RainbowImage NO_IMAGE = null;
    private static final int MAX_THRESHOLD = 100;
    private static final int INK_ISSUE_THRESHOLD = 98;
    private static final float INK_VELOCITY_THRESHOLD = ContextRetriever.INSTANCE.getResources().getDimension(R.dimen.ink_velocity_threshold);
    private static final int BRANCH_THRESHOLD_FAST = 73;
    private static final int BRANCH_THRESHOLD_SLOW = 93;
    private static final float INK_DROP_IMAGE_SCALE = 0.5f;
    private static final int OPAQUE = 255;
    private final RainbowDrawer rainbowDrawer;
    private final RainbowInputController rainbowInputController;
    private final InkDrop inkDrop;
    private final BranchesList branches;
    private RainbowImage image;
    private boolean enabled = true;
    private boolean initialised;

    protected InkPerformer(InkDrop inkDrop,
                           BranchesList branches,
                           RainbowDrawer rainbowDrawer,
                           RainbowInputController rainbowInputController) {
        this.inkDrop = inkDrop;
        this.branches = branches;
        this.rainbowDrawer = rainbowDrawer;
        this.rainbowInputController = rainbowInputController;
    }

    public static InkPerformer newInstance(BranchesList branches,
                                           RainbowDrawer rainbowDrawer,
                                           RainbowInputController rainbowInputController) {
        return new InkPerformer(InkDrop.newInstance(rainbowInputController),
                branches,
                rainbowDrawer,
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
        }
    }

    private void configureDrawer() {
        rainbowDrawer.noStroke();
        rainbowDrawer.smooth();
        rainbowDrawer.loadImage(R.drawable.brush_ink, RainbowImage.LOAD_ORIGINAL_SIZE, this);
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
            moveAndPaintInkDrop(rainbowInputController);
        }
    }

    private void moveAndPaintInkDrop(final RainbowInputController rainbowInputController) {
        rainbowDrawer.exploreLine(rainbowInputController.getPreviouowersSmoothX(),
                rainbowInputController.getPreviousSmoothY(),
                rainbowInputController.getSmoothX(),
                rainbowInputController.getSmoothY(),
                LineExplorer.Precision.LOW,
                new RainbowDrawer.PointDetectedListener() {
                    @Override
                    public void onPointDetected(float px, float py, float x, float y, RainbowDrawer rainbowDrawer) {
                        inkDrop.updateInkRadius();
                        drawInk(px, py, x, y);
                        attemptToBloomBranchAt(x, y);
                    }
                }
    }

    private void attemptToBloomBranchAt(float x, float y) {
        if (!isErasing()) {
            float bloomBranchThreshold = rainbowInputController.getFingerVelocity() > INK_VELOCITY_THRESHOLD
                    ? BRANCH_THRESHOLD_FAST
                    : BRANCH_THRESHOLD_SLOW;
            if (RainbowMath.random(MAX_THRESHOLD) > bloomBranchThreshold && !hasToPaintDropImage()) {
                createBranchAt(x, y);
            }
        }
    }

    private boolean isErasing() {
        return BrushColor.ERASE == inkDrop.getBrushColor();
    }

    private void createBranchAt(float x, float y) {
        MovementDirection verticalMovement = rainbowInputController.getVerticalDirection();
        MovementDirection horizontalMovement = rainbowInputController.getHorizontalDirection();
        float radius = inkDrop.getRadius() / 4;
        float verticalOffset = verticalMovement == MovementDirection.DOWN ? radius : -radius;
        float horizontalOffset = horizontalMovement == MovementDirection.RIGHT ? radius : -radius;

        branches.bloomFrom(Branch.createAt(x + horizontalOffset, y + verticalOffset));
    }

    private void drawInk(float px, float py, float x, float y) {
        paintDropWithoutImage(px, py, x, y);
        if (hasToPaintDropImage()) {
            paintDropWithImage(x, y);
        }
    }

    private boolean hasToPaintDropImage() {
        return !isErasing() && RainbowMath.random(MAX_THRESHOLD) > INK_ISSUE_THRESHOLD && hasImage();
    }

    private void paintDropWithImage(float x, float y) {
        rainbowDrawer.tint(inkDrop.getBrushColor().toAndroidColor());
        rainbowDrawer.imageMode(RainbowGraphics.CENTER);

        rainbowDrawer.pushMatrix();
        rainbowDrawer.translate(x, y);
        rainbowDrawer.rotate(RainbowMath.random(RainbowMath.TWO_PI));
        rainbowDrawer.image(image, 0, 0, inkDrop.getRadius(), inkDrop.getRadius());
        rainbowDrawer.popMatrix();
    }

    private void paintDropWithoutImage(float px, float py, float x, float y) {
        rainbowDrawer.stroke(inkDrop.getBrushColor().toAndroidColor(), OPAQUE);
        rainbowDrawer.strokeWeight(isErasing() ? inkDrop.getMaxRadius() * INK_DROP_IMAGE_SCALE : inkDrop.getRadius() * INK_DROP_IMAGE_SCALE);
        rainbowDrawer.line(px, py, x, y);
        rainbowDrawer.strokeWeight(1);
    }

    // TODO revisit this once RainbowImage has support for EmptyRainbowImage
    private boolean hasImage() {
        return image != NO_IMAGE;
    }

    @Override
    public void reset() {
        inkDrop.resetRadius();
    }

    @Override
    public void enable() {
        enabled = true;
    }

    @Override
    public void disable() {
        enabled = false;
    }
}
