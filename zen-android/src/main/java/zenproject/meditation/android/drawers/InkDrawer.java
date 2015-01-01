package zenproject.meditation.android.drawers;

import com.juankysoriano.rainbow.core.drawing.RainbowDrawer;
import com.juankysoriano.rainbow.core.event.RainbowInputController;
import com.juankysoriano.rainbow.core.graphics.RainbowGraphics;
import com.juankysoriano.rainbow.core.graphics.RainbowImage;
import com.juankysoriano.rainbow.utils.RainbowMath;

import zenproject.meditation.android.R;
import zenproject.meditation.android.model.InkDrop;
import zenproject.meditation.android.model.InkDropSizeLimiter;

import static com.juankysoriano.rainbow.core.event.RainbowInputController.MovementDirection;

public class InkDrawer implements StepDrawer, RainbowImage.LoadPictureListener {
    private static final RainbowImage NO_IMAGE = null;
    private static final int INK_ISSUE_THRESHOLD = 99;
    private static final int ALPHA = 180;
    private static final int BLACK = 0;
    private static final int WHITE = 255;
    private static final float INK_DROP_IMAGE_SCALE = 0.5f;
    private final RainbowDrawer rainbowDrawer;
    private final RainbowInputController rainbowInputController;
    private final InkDrop inkDrop;
    private final BranchesList branches;
    private RainbowImage image;
    private boolean enabled = true;

    private InkDrawer(InkDrop inkDrop, BranchesList branches, RainbowDrawer rainbowDrawer, RainbowInputController rainbowInputController) {
        this.inkDrop = inkDrop;
        this.branches = branches;
        this.rainbowDrawer = rainbowDrawer;
        this.rainbowInputController = rainbowInputController;
    }

    public static InkDrawer newInstance(BranchesList branches, InkDropSizeLimiter inkDropSizeLimiter, RainbowDrawer rainbowDrawer, RainbowInputController rainbowInputController) {
        InkDrawer inkDrawer = new InkDrawer(new InkDrop(inkDropSizeLimiter), branches, rainbowDrawer, rainbowInputController);
        configureDrawer(rainbowDrawer);
        rainbowDrawer.loadImage(R.drawable.brush_ink, RainbowImage.LOAD_ORIGINAL_SIZE, inkDrawer);
        return inkDrawer;
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
    public void paintStep() {
        if (enabled) {
            moveAndPaintInkDrop(rainbowInputController);
        }
    }

    private void moveAndPaintInkDrop(final RainbowInputController rainbowInputController) {
        rainbowDrawer.exploreLine(rainbowInputController.getSmoothX(),
                rainbowInputController.getSmoothY(),
                rainbowInputController.getPreviousSmoothX(),
                rainbowInputController.getPreviousSmoothY(),
                new RainbowDrawer.PointDetectedListener() {

                    @Override
                    public void onPointDetected(float x, float y, RainbowDrawer rainbowDrawer) {
                        inkDrop.updateInkRadiusFor(rainbowInputController);
                        drawInk(x, y);
                        attempToCreateBranchAt(x, y);
                    }
                });
    }

    private void attempToCreateBranchAt(float x, float y) {
        if (RainbowMath.random(100) > 95 && !hasToPaintDropImage()) {
            createBranchAt(x, y);
        }
    }

    private void createBranchAt(float x, float y) {
        MovementDirection verticalMovement = rainbowInputController.getVerticalDirection();
        MovementDirection horizontalMovement = rainbowInputController.getHorizontalDirection();
        float radius = inkDrop.getRadius() / 4;
        float verticalOffset = verticalMovement == MovementDirection.DOWN ? radius : -radius;
        float horizontalOffset = horizontalMovement == MovementDirection.RIGHT ? radius : -radius;

        branches.sproudFrom(Branch.createAt(x + horizontalOffset, y + verticalOffset));
    }

    private void drawInk(float x, float y) {
        if (hasToPaintDropImage()) {
            paintDropWithImage(x, y);
        } else {
            paintDropWithoutImage(x, y);
        }
    }

    private boolean hasToPaintDropImage() {
        return RainbowMath.random(100) > INK_ISSUE_THRESHOLD && hasImage();
    }

    private void paintDropWithImage(float x, float y) {
        rainbowDrawer.pushMatrix();
        rainbowDrawer.tint(WHITE, ALPHA);
        rainbowDrawer.translate(x, y);
        rainbowDrawer.rotate(RainbowMath.random(RainbowMath.TWO_PI));
        rainbowDrawer.imageMode(RainbowGraphics.CENTER);
        rainbowDrawer.image(image, 0, 0, inkDrop.getRadius(), inkDrop.getRadius());
        rainbowDrawer.popMatrix();
    }

    private void paintDropWithoutImage(float x, float y) {
        rainbowDrawer.noStroke();
        rainbowDrawer.fill(BLACK, ALPHA);
        rainbowDrawer.ellipseMode(RainbowGraphics.CENTER);
        rainbowDrawer.ellipse(x, y, inkDrop.getRadius() * INK_DROP_IMAGE_SCALE, inkDrop.getRadius() * INK_DROP_IMAGE_SCALE);
    }

    private boolean hasImage() {
        return image != NO_IMAGE;
    }

    @Override
    public void reset() {
        inkDrop.resetRadius();
    }

    public void enable() {
        enabled = true;
    }

    public void disable() {
        enabled = false;
    }
}
