package zenproject.meditation.android.model;

import com.juankysoriano.rainbow.utils.RainbowMath;

import zenproject.meditation.android.ContextRetriever;
import zenproject.meditation.android.R;

public class InkDropSizeLimiter {

    private static final float MINIMUM_RADIUS = ContextRetriever.INSTANCE.getCurrentContext().getResources().getDimension(R.dimen.ink_drop_min_radius);
    private static final float MAXIMUM_RADIUS = ContextRetriever.INSTANCE.getCurrentContext().getResources().getDimension(R.dimen.ink_drop_max_radius);
    private static final float SCALE_FACTOR = 3;
    private float percentage = 10;

    public float getMinimumRadius() {
        return RainbowMath.max(MINIMUM_RADIUS, getRadius() / calculateRadiusFactorForMinimum());
    }

    public float getRadius() {
        return RainbowMath.map(percentage, 0, 100, MINIMUM_RADIUS, MAXIMUM_RADIUS);
    }

    private float calculateRadiusFactorForMinimum() {
        return SCALE_FACTOR;
    }

    public float getMaxRadius() {
        return Math.min(MAXIMUM_RADIUS, getRadius() * calculateRadiusFactorForMaximum());
    }

    private float calculateRadiusFactorForMaximum() {
        return RainbowMath.map(MAXIMUM_RADIUS + MINIMUM_RADIUS - getRadius(), MINIMUM_RADIUS, MAXIMUM_RADIUS, 1, SCALE_FACTOR);
    }

}
