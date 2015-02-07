package zenproject.meditation.android.model;

import com.juankysoriano.rainbow.utils.RainbowMath;

import zenproject.meditation.android.ContextRetriever;
import zenproject.meditation.android.R;

public class InkDrop {
    private static final float SPRING = 0.1f;
    private static final float DAMP = 0.6f;
    private static final float MINIMUM_RADIUS = ContextRetriever.INSTANCE.getContext().getResources().getDimension(R.dimen.ink_drop_min_radius);
    private static final float MAXIMUM_RADIUS = ContextRetriever.INSTANCE.getContext().getResources().getDimension(R.dimen.ink_drop_max_radius);
    private static final float RADIUS_STEP = ContextRetriever.INSTANCE.getContext().getResources().getDimension(R.dimen.ink_drop_step);
    private float x;
    private float y;
    private float xOld;
    private float yOld;
    private float radius;
    private float xVel;
    private float yVel;

    public float getRadius() {
        return radius;
    }

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

    public void resetTo(float x, float y) {
        this.x = x;
        this.y = y;
        this.xOld = x;
        this.yOld = y;
        this.xVel = 0;
        this.yVel = 0;
    }

    public void updateInkRadius(boolean fingerOnScreen) {
        if (isAccelerating() || !fingerOnScreen) {
            radius -= RADIUS_STEP;
        } else if (isDecelerating()) {
            radius += RADIUS_STEP * 2;
        }

        constrainRadius();
    }

    private boolean isAccelerating() {
        return calculateVelocity() > MINIMUM_RADIUS;
    }

    private boolean isDecelerating() {
        return !isAccelerating();
    }

    public boolean isMoving() {
        return calculateVelocity() > 0.001;
    }

    public void resetRadius() {
        radius = 0;
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

    private void constrainRadius() {
        radius = RainbowMath.constrain(radius, MINIMUM_RADIUS, MAXIMUM_RADIUS);
    }

    private float calculateVelocity() {
        return RainbowMath.dist(0, 0, xVel, yVel);
    }
}