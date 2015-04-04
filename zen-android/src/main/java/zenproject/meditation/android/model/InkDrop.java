package zenproject.meditation.android.model;

import com.juankysoriano.rainbow.core.event.RainbowInputController;
import com.juankysoriano.rainbow.utils.RainbowMath;

import zenproject.meditation.android.ContextRetriever;
import zenproject.meditation.android.R;

public class InkDrop {

    private static final float RADIUS_STEP = ContextRetriever.INSTANCE.getCurrentResources().getDimension(R.dimen.ink_drop_step);
    private static final float INK_VELOCITY_THRESHOLD = ContextRetriever.INSTANCE.getCurrentResources().getDimension(R.dimen.ink_velocity_threshold);
    private static final float ABSOLUTE_MIN_RADIUS = ContextRetriever.INSTANCE.getCurrentResources().getDimension(R.dimen.ink_drop_min_radius);

    private final InkDropSizeLimiter inkDropSizeLimiter;
    private float radius;
    private boolean reachedRadiusAfterReset;

    public InkDrop(InkDropSizeLimiter inkDropSizeLimiter) {
        this.inkDropSizeLimiter = inkDropSizeLimiter;
    }

    public float getRadius() {
        return radius;
    }

    public float getMaxRadius() {
        return inkDropSizeLimiter.getMaxRadius();
    }

    public void updateInkRadiusFor(RainbowInputController rainbowInputController) {
        if (radiusShouldIncrease(rainbowInputController)) {
            radius += RADIUS_STEP;
        } else if (radiusShouldDecrease(rainbowInputController)) {
            radius -= RADIUS_STEP;
        }

        constrainRadius(rainbowInputController);
    }

    private boolean radiusShouldDecrease(RainbowInputController rainbowInputController) {
        return isFingerMovingFast(rainbowInputController)
                || !rainbowInputController.isScreenTouched();
    }

    private boolean radiusShouldIncrease(RainbowInputController rainbowInputController) {
        return rainbowInputController.isScreenTouched() && isFingerMovingSlow(rainbowInputController);
    }

    private void constrainRadius(RainbowInputController rainbowInputController) {
        reachedRadiusAfterReset = reachedRadiusAfterReset || radius >= inkDropSizeLimiter.getRadius() / 2;
        radius = RainbowMath.constrain(radius, getMinimumRadius(rainbowInputController), inkDropSizeLimiter.getMaxRadius());
    }

    private float getMinimumRadius(RainbowInputController rainbowInputController) {
        return reachedRadiusAfterReset || !rainbowInputController.isScreenTouched() ? inkDropSizeLimiter.getMinimumRadius() : ABSOLUTE_MIN_RADIUS;
    }

    private boolean isFingerMovingFast(RainbowInputController rainbowInputController) {
        return rainbowInputController.getFingerVelocity() > INK_VELOCITY_THRESHOLD;
    }

    private boolean isFingerMovingSlow(RainbowInputController rainbowInputController) {
        return !isFingerMovingFast(rainbowInputController);
    }

    public void resetRadius() {
        reachedRadiusAfterReset = false;
        radius = ABSOLUTE_MIN_RADIUS;
    }
}
