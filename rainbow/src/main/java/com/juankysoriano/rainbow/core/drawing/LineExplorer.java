package com.juankysoriano.rainbow.core.drawing;

import com.juankysoriano.rainbow.utils.RainbowMath;

public class LineExplorer {
    private static final int STEP = 3;

    public void exploreLine(final float x,
                            final float y,
                            final float px,
                            final float py,
                            final RainbowDrawer rainbowDrawer,
                            final RainbowDrawer.PointDetectedListener listener) {
        if (x == px && y == py) {
            doProcess(x, y, rainbowDrawer, listener);
        }

        float dx = x - px;
        float dy = y - py;
        if (dx == 0) {
            if (RainbowMath.min(y, py) == py) {
                for (float i = RainbowMath.min(y, py); i < RainbowMath.max(y, py); i += STEP) {
                    doProcess(x, i, rainbowDrawer, listener);
                }
            } else {
                for (float i = RainbowMath.max(y, py); i >= RainbowMath.min(y, py); i -= STEP) {
                    doProcess(x, i, rainbowDrawer, listener);
                }
            }
            return;
        }
        float k = dy / dx;
        float m = y - x * k;
        if (RainbowMath.min(x, px) == px) {
            for (float i = RainbowMath.min(x, px); i < RainbowMath.max(x, px); i += STEP / RainbowMath.max(1, RainbowMath.abs(k))) {
                doProcess(i, k * i + m, rainbowDrawer, listener);
            }
        } else {
            for (float i = RainbowMath.max(x, px); i >= RainbowMath.min(x, px); i -= STEP / RainbowMath.max(1, RainbowMath.abs(k))) {
                doProcess(i, k * i + m, rainbowDrawer, listener);
            }
        }
    }

    private void doProcess(float x, float y, RainbowDrawer rainbowDrawer, RainbowDrawer.PointDetectedListener listener) {
        listener.onPointDetected(x, y, rainbowDrawer);
    }
}
