package zenproject.meditation.android.model;

import com.juankysoriano.rainbow.core.event.RainbowInputController;
import com.juankysoriano.rainbow.utils.RainbowMath;

import zenproject.meditation.android.ContextRetriever;
import zenproject.meditation.android.R;

public class InkDrop {

    private static final float RADIUS_STEP = ContextRetriever.INSTANCE.getContext().getResources().getDimension(R.dimen.ink_drop_step);
    private static final float INK_VELOCITY_THRESHOLD = ContextRetriever.INSTANCE.getContext().getResources().getDimension(R.dimen.ink_velocity_threshold);

    private final InkDropSizeLimiter inkDropSizeLimiter;
    private float radius;

    public InkDrop(InkDropSizeLimiter inkDropSizeLimiter) {
        this.inkDropSizeLimiter = inkDropSizeLimiter;
    }

    public float getRadius() {
        return radius;
    }

    public void updateInkRadiusFor(RainbowInputController rainbowInputController) {
        if (radiusShouldIncrease(rainbowInputController)) {
            radius += RADIUS_STEP;
        } else if (radiusShouldDecrease(rainbowInputController)) {
            radius -= RADIUS_STEP;
        }

        constrainRadius();
    }

    private boolean radiusShouldDecrease(RainbowInputController rainbowInputController) {
        return radius > inkDropSizeLimiter.getRadius() && !isFingerMovingSlow(rainbowInputController)
                || isFingerMovingFast(rainbowInputController)
                || !rainbowInputController.isScreenTouched();
    }

    private boolean radiusShouldIncrease(RainbowInputController rainbowInputController) {
        return radius < inkDropSizeLimiter.getRadius() && !isFingerMovingFast(rainbowInputController)
                || isFingerMovingSlow(rainbowInputController);
    }

    private void constrainRadius() {
        radius = RainbowMath.constrain(radius, inkDropSizeLimiter.getMinimumRadius(), inkDropSizeLimiter.getMaxRadius());
    }

    private boolean isFingerMovingFast(RainbowInputController rainbowInputController) {
        return rainbowInputController.getFingerVelocity() > INK_VELOCITY_THRESHOLD;
    }

    private boolean isFingerMovingSlow(RainbowInputController rainbowInputController) {
        return rainbowInputController.getFingerVelocity() < INK_VELOCITY_THRESHOLD / 2.5;
    }

    public void resetRadius() {
        radius = inkDropSizeLimiter.getRadius();
    }
}