package zenproject.meditation.android.sketch.painting.ink;

import com.juankysoriano.rainbow.core.drawing.Modes;
import com.juankysoriano.rainbow.core.drawing.RainbowDrawer;
import com.juankysoriano.rainbow.core.event.RainbowInputController;
import com.juankysoriano.rainbow.core.graphics.RainbowImage;
import com.juankysoriano.rainbow.utils.RainbowMath;
import com.novoda.notils.exception.DeveloperError;

import zenproject.meditation.android.R;
import zenproject.meditation.android.sketch.actions.StepPerformer;

public class InkPerformer implements StepPerformer, RainbowImage.LoadPictureListener {
    private static final RainbowImage NO_IMAGE = null;
    private static final int MAX_THRESHOLD = 100;
    private static final int INK_ISSUE_THRESHOLD = 98;
    private static final float INK_DROP_IMAGE_SCALE = 0.5f;
    private static final int OPAQUE = 255;
    private final RainbowDrawer rainbowDrawer;
    private final RainbowInputController rainbowInputController;
    private final InkDrop inkDrop;
    private RainbowImage image;
    private boolean enabled = true;
    private boolean initialised;

    protected InkPerformer(InkDrop inkDrop,
                           RainbowDrawer rainbowDrawer,
                           RainbowInputController rainbowInputController) {
        this.inkDrop = inkDrop;
        this.rainbowDrawer = rainbowDrawer;
        this.rainbowInputController = rainbowInputController;
    }

    public static InkPerformer newInstance(InkDrop inkDrop,
                                           RainbowDrawer rainbowDrawer,
                                           RainbowInputController rainbowInputController) {
        return new InkPerformer(
                inkDrop,
                rainbowDrawer,
                rainbowInputController
        );
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
        rainbowDrawer.loadImage(R.drawable.brush_ink, Modes.LoadMode.LOAD_ORIGINAL_SIZE, this);
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
            moveAndPaintInkDrop();
        }
    }

    private void moveAndPaintInkDrop() {
        drawInk(
                rainbowInputController.getPreviousSmoothX(),
                rainbowInputController.getPreviousSmoothY(),
                rainbowInputController.getSmoothX(),
                rainbowInputController.getSmoothY()
        );
        inkDrop.updateInkRadius();
    }

    private void drawInk(float px, float py, float x, float y) {
        paintDropWithoutImage(px, py, x, y);
        if (hasToPaintDropImage()) {
            paintDropWithImage(x, y);
        }
    }

    private void paintDropWithoutImage(float px, float py, float x, float y) {
        rainbowDrawer.stroke(inkDrop.getBrushColor().toAndroidColor(), OPAQUE);
        rainbowDrawer.strokeWeight(isErasing() ? inkDrop.getMaxRadius() * INK_DROP_IMAGE_SCALE : inkDrop.getRadius() * INK_DROP_IMAGE_SCALE);
        rainbowDrawer.line(px, py, x, y);
    }

    private boolean hasToPaintDropImage() {
        return !isErasing() && RainbowMath.random(MAX_THRESHOLD) > INK_ISSUE_THRESHOLD && hasImage();
    }

    private void paintDropWithImage(float x, float y) {
        rainbowDrawer.tint(inkDrop.getBrushColor().toAndroidColor());
        rainbowDrawer.imageMode(Modes.Draw.CENTER);

        rainbowDrawer.pushMatrix();
        rainbowDrawer.translate(x, y);
        rainbowDrawer.rotate(RainbowMath.random(RainbowMath.TWO_PI));
        rainbowDrawer.image(image, 0, 0, inkDrop.getRadius(), inkDrop.getRadius());
        rainbowDrawer.popMatrix();
    }

    private boolean isErasing() {
        return BrushColor.ERASE == inkDrop.getBrushColor();
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
