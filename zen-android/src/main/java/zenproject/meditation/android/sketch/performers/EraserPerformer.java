package zenproject.meditation.android.sketch.performers;

import com.juankysoriano.rainbow.core.drawing.LineExplorer;
import com.juankysoriano.rainbow.core.drawing.RainbowDrawer;
import com.juankysoriano.rainbow.core.event.RainbowInputController;
import com.juankysoriano.rainbow.core.graphics.RainbowGraphics;

public class EraserPerformer implements StepPerformer {
    private static final int ALPHA = 100;
    private static final int RADIUS = 60;

    private final RainbowDrawer rainbowDrawer;
    private final RainbowInputController rainbowInputController;
    private boolean enabled;

    protected EraserPerformer(RainbowDrawer rainbowDrawer, RainbowInputController rainbowInputController) {
        this.rainbowDrawer = rainbowDrawer;
        this.rainbowInputController = rainbowInputController;
    }

    public static EraserPerformer newInstance(RainbowDrawer rainbowDrawer, RainbowInputController rainbowInputController) {
        EraserPerformer eraserDrawer = new EraserPerformer(rainbowDrawer, rainbowInputController);
        configureDrawer(rainbowDrawer);
        return eraserDrawer;
    }

    private static void configureDrawer(RainbowDrawer rainbowDrawer) {
        rainbowDrawer.noStroke();
        rainbowDrawer.smooth();
    }

    @Override
    public void doStep() {
        final float x = rainbowInputController.getSmoothX();
        final float y = rainbowInputController.getSmoothY();
        final float px = rainbowInputController.getPreviousSmoothX();
        final float py = rainbowInputController.getPreviousSmoothY();

        rainbowDrawer.exploreLine(x, y, px, py, LineExplorer.Precision.LOW, new RainbowDrawer.PointDetectedListener() {

            @Override
            public void onPointDetected(float px, float py, float x, float y, RainbowDrawer rainbowDrawer) {
                drawErase(x, y);
            }
        });
    }

    private void drawErase(float x, float y) {
        if (enabled) {
            rainbowDrawer.noStroke();
            rainbowDrawer.fill(RainbowGraphics.CLEAR, ALPHA);
            rainbowDrawer.ellipseMode(RainbowGraphics.CENTER);
            rainbowDrawer.ellipse(x, y, RADIUS, RADIUS);
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
