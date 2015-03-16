package zenproject.meditation.android.model;

import com.juankysoriano.rainbow.utils.RainbowMath;

import zenproject.meditation.android.ContextRetriever;
import zenproject.meditation.android.R;
import zenproject.meditation.android.preferences.BrushOptionsPreferences;

public class InkDropSizeLimiter {

    private static final float MINIMUM_RADIUS = ContextRetriever.INSTANCE.getCurrentResources().getDimension(R.dimen.ink_drop_min_radius);
    private static final float MAXIMUM_RADIUS = ContextRetriever.INSTANCE.getCurrentResources().getDimension(R.dimen.ink_drop_max_radius);
    private static final float PERCENTAGE_MIN = 0;
    private static final float PERCENTAGE_MAX = 100;
    private static final float SCALE_FACTOR = 3;
    private int percentage;

    public static InkDropSizeLimiter newInstance() {
        return new InkDropSizeLimiter(BrushOptionsPreferences.newInstance().getBrushSize());
    }

    InkDropSizeLimiter(int percentage) {
        this.percentage = percentage;
    }

    public float getMinimumRadius() {
        return RainbowMath.max(MINIMUM_RADIUS, getRadius() / calculateRadiusFactorForMinimum());
    }

    public float getRadius() {
        return RainbowMath.map(percentage, PERCENTAGE_MIN, PERCENTAGE_MAX, MINIMUM_RADIUS, MAXIMUM_RADIUS);
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

    public void setScaleFactor(int percentage) {
        this.percentage = percentage;
    }

}
