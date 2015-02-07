package zenproject.meditation.android.drawers;

import com.juankysoriano.rainbow.utils.RainbowMath;

public class AngleCalculator {
    public float calculateAngle(float x1, float y1, float x2, float y2) {
        float dx = x1 - x2;
        float dy = y1 - y2;
        return RainbowMath.atan(dy / dx);
    }
}
