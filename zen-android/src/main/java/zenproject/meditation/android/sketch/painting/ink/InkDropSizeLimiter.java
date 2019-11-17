package zenproject.meditation.android.sketch.painting.ink;

import com.juankysoriano.rainbow.utils.RainbowMath;

import zenproject.meditation.android.ContextRetriever;
import zenproject.meditation.android.R;
import zenproject.meditation.android.preferences.BrushOptionsPreferences;

public class InkDropSizeLimiter {

    private static final float RADIUS_MIN = ContextRetriever.INSTANCE.getResources().getDimension(R.dimen.ink_drop_min_radius);
    private static final float RADIUS_MAX = ContextRetriever.INSTANCE.getResources().getDimension(R.dimen.ink_drop_max_radius);
    private static final float PERCENTAGE_MIN = 0;
    private static final float PERCENTAGE_MAX = 100;
    private static final float SCALE_FACTOR = 3;
    private final BrushOptionsPreferences brushOptionsPreferences;

    public static InkDropSizeLimiter newInstance(BrushOptionsPreferences brushOptionsPreferences) {
        return new InkDropSizeLimiter(brushOptionsPreferences);
    }

    InkDropSizeLimiter(BrushOptionsPreferences brushOptionsPreferences) {
        this.brushOptionsPreferences = brushOptionsPreferences;
    }

    float getMinimumRadius() {
        return RainbowMath.max(RADIUS_MIN, getRadius() / calculateRadiusFactorForMinimum());
    }

    float getRadius() {
        return constrain(RainbowMath.map(brushOptionsPreferences.getBrushSizePercentage(),
                PERCENTAGE_MIN,
                PERCENTAGE_MAX,
                RADIUS_MIN,
                RADIUS_MAX));
    }

    private float constrain(float radius) {
        return Math.min(RADIUS_MAX, Math.max(RADIUS_MIN, radius));
    }

    private float calculateRadiusFactorForMinimum() {
        return SCALE_FACTOR;
    }

    float getMaximumRadius() {
        return Math.min(RADIUS_MAX, getRadius());
    }
}
