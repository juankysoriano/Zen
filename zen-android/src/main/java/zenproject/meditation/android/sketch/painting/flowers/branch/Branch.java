package zenproject.meditation.android.sketch.painting.flowers.branch;

import com.juankysoriano.rainbow.core.matrix.RVector;
import com.juankysoriano.rainbow.utils.RainbowMath;

import zenproject.meditation.android.ContextRetriever;
import zenproject.meditation.android.R;

/**
 * TODO
 * This class could be much better written, attention to the two constructors.
 * We could avoid writing so many sh*t there.
 */
public class Branch {
    private static final float MIN_RADIUS = ContextRetriever.INSTANCE.getResources().getDimension(R.dimen.branch_min_radius);
    private static final float MIN_RADIUS_TO_BLOOM = ContextRetriever.INSTANCE.getResources().getDimension(R.dimen.branch_min_bloom_radius);
    private static final float DEFAULT_RADIUS = ContextRetriever.INSTANCE.getResources().getDimension(R.dimen.branch_default_radius);
    private static final float MIN_STEP = 0.05f;
    private static final float MAX_STEP = 0.25f;
    private static final float MIN_RADIUS_FACTOR = 0.9f;
    private static final float MAX_RADIUS_FACTOR = 1.1f;
    private static final float SHRINK = RainbowMath.random(0.94f, 0.96f);
    private float step;
    private float angle;
    private float radius;
    private RVector position;
    private RVector previousPosition;

    protected Branch(Branch branch) {
        this(branch.position, branch.angle, branch.radius);
        step = branch.step > 0 ? -generateRandomStep() : generateRandomStep();
    }

    protected Branch(RVector position, float angle, float radius) {
        this.position = new RVector(position.x, position.y);
        this.previousPosition = new RVector(this.position.x, this.position.y);
        this.angle = angle;
        this.step = generateRandomStep();
        this.radius = radius * RainbowMath.random(MIN_RADIUS_FACTOR, MAX_RADIUS_FACTOR);
    }

    private float generateRandomStep() {
        return RainbowMath.random(MIN_STEP, MAX_STEP);
    }

    public static Branch createFrom(Branch branch) {
        return new Branch(branch);
    }

    public static Branch createAt(float x, float y) {
        float angle = RainbowMath.random(-RainbowMath.PI, RainbowMath.PI);
        RVector pos = new RVector(x, y);

        return new Branch(pos, angle, DEFAULT_RADIUS);
    }

    public boolean isDead() {
        return RainbowMath.abs(radius) < MIN_RADIUS;
    }

    public boolean canBloom() {
        return RainbowMath.abs(radius) > MIN_RADIUS_TO_BLOOM;
    }

    public void update() {
        backupPosition();
        updatePosition();
        updateAngle();
        updateRadius();
    }

    private void backupPosition() {
        previousPosition.x = position.x;
        previousPosition.y = position.y;
    }

    private void updatePosition() {
        position.x += radius * RainbowMath.cos(angle);
        position.y += radius * RainbowMath.sin(angle);
    }

    private void updateAngle() {
        angle += step;
    }

    private void updateRadius() {
        radius *= SHRINK;
    }

    public float getX() {
        return position.x;
    }

    public float getY() {
        return position.y;
    }

    public float getOldX() {
        return previousPosition.x;
    }

    public float getOldY() {
        return previousPosition.y;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Branch)) {
            return false;
        }

        Branch branch = (Branch) o;

        return previousPosition.equals(branch.previousPosition) && position.equals(branch.position);

    }

    @Override
    public int hashCode() {
        int result = position.hashCode();
        result = 31 * result + previousPosition.hashCode();
        return result;
    }
}
