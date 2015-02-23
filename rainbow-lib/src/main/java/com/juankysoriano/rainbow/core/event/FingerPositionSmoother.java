package com.juankysoriano.rainbow.core.event;

import com.juankysoriano.rainbow.utils.RainbowMath;

class FingerPositionSmoother {
    private static final float SPRING = 0.2f;
    private static final float DAMP = 0.5f;
    private float x;
    private float y;
    private float xOld;
    private float yOld;
    private float xVel;
    private float yVel;

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public float getOldX() {
        return xOld;
    }

    public float getOldY() {
        return yOld;
    }

    public void moveTo(float x, float y) {
        backupPosition();
        updateVelocity(x, y);
        updatePosition();
    }

    private void backupPosition() {
        xOld = x;
        yOld = y;
    }

    private void updateVelocity(float newX, float newY) {
        float xDistance = (newX - x) * SPRING;
        float yDistance = (newY - y) * SPRING;

        xVel = (xVel + xDistance) * DAMP;
        yVel = (yVel + yDistance) * DAMP;
    }

    private void updatePosition() {
        x += xVel;
        y += yVel;
    }

    public void resetTo(float x, float y) {
        this.x = x;
        this.y = y;
        this.xOld = x;
        this.yOld = y;
    }

    public float getFingerVelocity() {
        return RainbowMath.dist(0, 0, xVel, yVel);
    }
}