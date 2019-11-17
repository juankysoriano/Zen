package zenproject.meditation.android.sketch.painting.ink;

import com.juankysoriano.rainbow.core.event.RainbowInputController;
import com.juankysoriano.rainbow.utils.RainbowMath;

import zenproject.meditation.android.ContextRetriever;
import zenproject.meditation.android.R;
import zenproject.meditation.android.preferences.BrushOptionsPreferences;

public class InkDrop {

    private static final float RADIUS_STEP = ContextRetriever.INSTANCE.getResources().getDimension(R.dimen.ink_drop_step);
    private static final float INK_VELOCITY_THRESHOLD = ContextRetriever.INSTANCE.getResources().getDimension(R.dimen.ink_velocity_threshold);
    private static final float ABSOLUTE_MIN_RADIUS = ContextRetriever.INSTANCE.getResources().getDimension(R.dimen.ink_drop_min_radius);

    private final InkDropSizeLimiter inkDropSizeLimiter;
    private final BrushOptionsPreferences brushOptionsPreferences;
    private final RainbowInputController rainbowInputController;
    private float radius;
    private boolean reachedRadiusAfterReset;

    public static InkDrop newInstance(RainbowInputController rainbowInputController) {
        BrushOptionsPreferences brushOptionsPreferences = BrushOptionsPreferences.newInstance();
        InkDropSizeLimiter inkDropSizeLimiter = InkDropSizeLimiter.newInstance(brushOptionsPreferences);

        return new InkDrop(inkDropSizeLimiter, brushOptionsPreferences, rainbowInputController);
    }

    InkDrop(InkDropSizeLimiter inkDropSizeLimiter,
            BrushOptionsPreferences brushOptionsPreferences,
            RainbowInputController rainbowInputController) {
        this.inkDropSizeLimiter = inkDropSizeLimiter;
        this.brushOptionsPreferences = brushOptionsPreferences;
        this.rainbowInputController = rainbowInputController;
        this.radius = ABSOLUTE_MIN_RADIUS;
    }

    public float getRadius() {
        return radius;
    }

    public BrushColor getBrushColor() {
        return brushOptionsPreferences.getBrushColor();
    }

    float getMaxRadius() {
        return inkDropSizeLimiter.getMaximumRadius();
    }

    void updateInkRadius() {
        if (radiusShouldIncrease()) {
            radius += RADIUS_STEP;
        } else {
            radius -= RADIUS_STEP;
        }

        constrainRadius();
    }

    private boolean radiusShouldIncrease() {
        return isFingerMovingSlow();
    }

    private void constrainRadius() {
        reachedRadiusAfterReset = reachedRadiusAfterReset || radius >= calculateRadiusToBeReach();
        radius = RainbowMath.constrain(radius, getMinimumRadius(), inkDropSizeLimiter.getMaximumRadius());
    }

    private float calculateRadiusToBeReach() {
        return inkDropSizeLimiter.getRadius() / 2;
    }

    private float getMinimumRadius() {
        return reachedRadiusAfterReset || !rainbowInputController.isScreenTouched() ? inkDropSizeLimiter.getMinimumRadius() : ABSOLUTE_MIN_RADIUS;
    }

    private boolean isFingerMovingSlow() {
        return rainbowInputController.isFingerMoving() && rainbowInputController.getFingerVelocity() < INK_VELOCITY_THRESHOLD;
    }

    public void resetRadius() {
        reachedRadiusAfterReset = false;
        radius = ABSOLUTE_MIN_RADIUS;
    }
}
