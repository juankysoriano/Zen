package zenproject.meditation.android.drawers;

import com.juankysoriano.rainbow.core.drawing.RainbowDrawer;
import com.juankysoriano.rainbow.core.event.RainbowInputController;
import com.juankysoriano.rainbow.core.graphics.RainbowGraphics;
import com.juankysoriano.rainbow.core.graphics.RainbowImage;
import com.juankysoriano.rainbow.utils.RainbowMath;

import zenproject.meditation.android.R;
import zenproject.meditation.android.model.InkDrop;

public class InkDrawer implements StepDrawer, RainbowImage.LoadPictureListener {
    private static final RainbowImage NO_IMAGE = null;
    private static final int INK_ISSUE_THRESHOLD = 97;
    private static final int ALPHA = 180;
    private static final int BLACK = 0;
    private static final int WHITE = 255;
    private static final float INK_DROP_IMAGE_SCALE = 0.5f;
    private final RainbowDrawer rainbowDrawer;
    private final RainbowInputController rainbowInputController;
    private final InkDrop inkDrop;
    private RainbowImage image;
    private boolean enabled = true;

    public static InkDrawer newInstance(RainbowDrawer rainbowDrawer, RainbowInputController rainbowInputController) {
        InkDrawer inkDrawer = new InkDrawer(new InkDrop(), rainbowDrawer, rainbowInputController);
        configureDrawer(rainbowDrawer);
        rainbowDrawer.loadImage(R.drawable.brush_ink, RainbowImage.LOAD_ORIGINAL_SIZE, inkDrawer);
        return inkDrawer;
    }

    private static void configureDrawer(RainbowDrawer rainbowDrawer) {
        rainbowDrawer.noStroke();
        rainbowDrawer.smooth();
    }

    private InkDrawer(InkDrop inkDrop, RainbowDrawer rainbowDrawer, RainbowInputController rainbowInputController) {
        this.inkDrop = inkDrop;
        this.rainbowDrawer = rainbowDrawer;
        this.rainbowInputController = rainbowInputController;
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
        inkDrop.moveTo(rainbowInputController.getX(), rainbowInputController.getY());
        rainbowDrawer.exploreLine(inkDrop.getX(), inkDrop.getY(), inkDrop.getOldX(), inkDrop.getOldY(), new RainbowDrawer.PointDetectedListener() {

            @Override
            public void onPointDetected(float x, float y, RainbowDrawer rainbowDrawer) {
                inkDrop.updateInkRadius(rainbowInputController.isScreenTouched());
                if (inkDrop.isMoving()) {
                    drawInk(x, y);
                }
            }
        });
    }

    @Override
    public void initDrawingAt(float x, float y) {
        inkDrop.resetTo(x, y);
        inkDrop.resetRadius();
    }

    private void drawInk(float x, float y) {
        if (hasToPaintDropImage()) {
            paintDropWithImage(x, y);
        } else {
            paintDropWithoutImage(x, y);
        }
    }

    private void paintDropWithoutImage(float x, float y) {
        rainbowDrawer.fill(BLACK, ALPHA);
        rainbowDrawer.ellipseMode(RainbowGraphics.CENTER);
        rainbowDrawer.ellipse(x, y, inkDrop.getRadius() * INK_DROP_IMAGE_SCALE, inkDrop.getRadius() * INK_DROP_IMAGE_SCALE);
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

    private boolean hasToPaintDropImage() {
        return RainbowMath.random(100) > INK_ISSUE_THRESHOLD && hasImage();
    }

    private boolean hasImage() {
        return image != NO_IMAGE;
    }

    public void enable() {
        enabled = true;
    }

    public void disable() {
        enabled = false;
    }
}
