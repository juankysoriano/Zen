package zenproject.meditation.android.sketch.painting.ink;

import com.juankysoriano.rainbow.utils.RainbowMath;

import zenproject.meditation.android.ContextRetriever;
import zenproject.meditation.android.R;
import zenproject.meditation.android.persistence.BrushOptionsPreferences;

public class InkDropSizeLimiter {

    private static final float MINIMUM_RADIUS = ContextRetriever.INSTANCE.getResources().getDimension(R.dimen.ink_drop_min_radius);
    private static final float MAXIMUM_RADIUS = ContextRetriever.INSTANCE.getResources().getDimension(R.dimen.ink_drop_max_radius);
    private static final float PERCENTAGE_MIN = 0;
    private static final float PERCENTAGE_MAX = 100;
    private static final float SCALE_FACTOR = 3;
    private final BrushOptionsPreferences brushOptionsPreferences;

    public static InkDropSizeLimiter newInstance() {
        return new InkDropSizeLimiter(BrushOptionsPreferences.newInstance());
    }

    InkDropSizeLimiter(BrushOptionsPreferences brushOptionsPreferences) {
        this.brushOptionsPreferences = brushOptionsPreferences;
    }

    public float getMinimumRadius() {
        return RainbowMath.max(MINIMUM_RADIUS / 2, getRadius() / calculateRadiusFactorForMinimum());
    }

    public float getRadius() {
        return RainbowMath.map(brushOptionsPreferences.getBrushSize(), PERCENTAGE_MIN, PERCENTAGE_MAX, MINIMUM_RADIUS, MAXIMUM_RADIUS);
    }

    private float calculateRadiusFactorForMinimum() {
        return SCALE_FACTOR;
    }

    public float getMaxRadius() {
        return Math.min(MAXIMUM_RADIUS, getRadius());
    }
}
