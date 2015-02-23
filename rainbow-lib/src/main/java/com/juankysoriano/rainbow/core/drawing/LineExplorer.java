package com.juankysoriano.rainbow.core.drawing;

import com.juankysoriano.rainbow.utils.RainbowMath;

public class LineExplorer {
    private static final float STEP = 3;

    public void exploreLine(final float px,
                            final float py,
                            final float x,
                            final float y,
                            final RainbowDrawer rainbowDrawer,
                            final RainbowDrawer.PointDetectedListener listener) {
        if (x == px && y == py) {
            doProcessOnPoint(x, y, rainbowDrawer, listener);
        }

        float dx = x - px;
        float dy = y - py;
        if (isVerticalLine(x, px)) {
            processVerticalLine(x, y, py, rainbowDrawer, listener);
            return;
        } else {
            float k = dy / dx;
            float m = y - x * k;
            if (isLineGoingLeft(x, px)) {
                processLineGoingLeft(x, px, rainbowDrawer, listener, k, m);
            } else {
                processLineGoingRight(x, px, rainbowDrawer, listener, k, m);
            }
        }

    }

    private void doProcessOnPoint(float x, float y, RainbowDrawer rainbowDrawer, RainbowDrawer.PointDetectedListener listener) {
        listener.onPointDetected(x, y, rainbowDrawer);
    }

    private boolean isVerticalLine(float x, float px) {
        return x - px == 0;
    }

    private void processVerticalLine(float x, float y, float py, RainbowDrawer rainbowDrawer, RainbowDrawer.PointDetectedListener listener) {
        if (isLineGoingDown(y, py)) {
            processVerticalLineGoingDown(x, y, py, rainbowDrawer, listener);
        } else {
            processVerticalLineGoingUp(x, y, py, rainbowDrawer, listener);
        }
    }

    private boolean isLineGoingLeft(float x, float px) {
        return RainbowMath.min(x, px) == px;
    }

    private void processLineGoingLeft(float x, float px, RainbowDrawer rainbowDrawer, RainbowDrawer.PointDetectedListener listener, float k, float m) {
        for (float i = RainbowMath.min(x, px); i < RainbowMath.max(x, px); i += STEP / RainbowMath.max(1, RainbowMath.abs(k))) {
            doProcessOnPoint(i, k * i + m, rainbowDrawer, listener);
        }
    }

    private void processLineGoingRight(float x, float px, RainbowDrawer rainbowDrawer, RainbowDrawer.PointDetectedListener listener, float k, float m) {
        for (float i = RainbowMath.max(x, px); i > RainbowMath.min(x, px); i -= STEP / RainbowMath.max(1, RainbowMath.abs(k))) {
            doProcessOnPoint(i, k * i + m, rainbowDrawer, listener);
        }
    }

    private boolean isLineGoingDown(float y, float py) {
        return RainbowMath.min(y, py) == py;
    }

    private void processVerticalLineGoingDown(float x, float y, float py, RainbowDrawer rainbowDrawer, RainbowDrawer.PointDetectedListener listener) {
        for (float i = RainbowMath.min(y, py); i < RainbowMath.max(y, py); i += STEP) {
            doProcessOnPoint(x, i, rainbowDrawer, listener);
        }
    }

    private void processVerticalLineGoingUp(float x, float y, float py, RainbowDrawer rainbowDrawer, RainbowDrawer.PointDetectedListener listener) {
        for (float i = RainbowMath.max(y, py); i > RainbowMath.min(y, py); i -= STEP) {
            doProcessOnPoint(x, i, rainbowDrawer, listener);
        }
    }
}
