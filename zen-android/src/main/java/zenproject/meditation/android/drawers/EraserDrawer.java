package zenproject.meditation.android.drawers;

import com.juankysoriano.rainbow.core.drawing.RainbowDrawer;
import com.juankysoriano.rainbow.core.event.RainbowInputController;
import com.juankysoriano.rainbow.core.graphics.RainbowGraphics;

public class EraserDrawer implements StepDrawer {
    private final RainbowDrawer rainbowDrawer;
    private final RainbowInputController rainbowInputController;
    private boolean enabled;

    public static EraserDrawer newInstance(RainbowDrawer rainbowDrawer, RainbowInputController rainbowInputController) {
        EraserDrawer eraserDrawer = new EraserDrawer(rainbowDrawer, rainbowInputController);
        configureDrawer(rainbowDrawer);
        return eraserDrawer;
    }

    private static void configureDrawer(RainbowDrawer rainbowDrawer) {
        rainbowDrawer.noStroke();
        rainbowDrawer.smooth();
    }

    private EraserDrawer(RainbowDrawer rainbowDrawer, RainbowInputController rainbowInputController) {
        this.rainbowDrawer = rainbowDrawer;
        this.rainbowInputController = rainbowInputController;
    }

    @Override
    public void paintStep() {
        final float x = rainbowInputController.getSmoothX();
        final float y = rainbowInputController.getSmoothY();
        final float px = rainbowInputController.getPreviousSmoothX();
        final float py = rainbowInputController.getPreviousSmoothY();

        rainbowDrawer.exploreLine(x, y, px, py, new RainbowDrawer.PointDetectedListener() {

            @Override
            public void onPointDetected(float x, float y, RainbowDrawer rainbowDrawer) {
                drawErase(x, y);
            }
        });
    }

    private void drawErase(float x, float y) {
        if (enabled) {
            rainbowDrawer.noStroke();
            rainbowDrawer.fill(RainbowGraphics.CLEAR, 30);
            rainbowDrawer.ellipseMode(RainbowGraphics.CENTER);
            rainbowDrawer.ellipse(x, y, 60, 60);
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
