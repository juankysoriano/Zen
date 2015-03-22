package zenproject.meditation.android.sketch.performers.flowers;

import com.juankysoriano.rainbow.core.matrix.RVector;
import com.juankysoriano.rainbow.utils.RainbowMath;

import zenproject.meditation.android.ContextRetriever;
import zenproject.meditation.android.R;

public class Branch {
    private static final float MIN_RADIUS = ContextRetriever.INSTANCE.getCurrentResources().getDimension(R.dimen.branch_min_radius);
    private static final float MIN_RADIUS_TO_SPROUD = ContextRetriever.INSTANCE.getCurrentResources().getDimension(R.dimen.branch_min_sproud_radius);
    private static final float DEFAULT_RADIUS = ContextRetriever.INSTANCE.getCurrentResources().getDimension(R.dimen.branch_default_radius);
    private static final float STEP = 0.2f;
    private static final float SHRINK = 0.95f;
    private float step;
    private float angle;
    private float radius;
    private RVector position;
    private RVector previousPosition;

    Branch(Branch branch) {
        this(branch.position, branch.angle, branch.radius);
        step = -(branch.step);
    }

    Branch(RVector position, float angle, float radius) {
        this.position = new RVector(position.x, position.y);
        this.previousPosition = new RVector(this.position.x, this.position.y);
        this.angle = angle;
        this.step = STEP;
        this.radius = radius * 1.05f;
    }

    public static Branch createFrom(Branch branch) {
        return new Branch(branch);
    }

    public static Branch createAt(float x, float y) {
        float angle = RainbowMath.random(-RainbowMath.PI, RainbowMath.PI);
        RVector pos = new RVector(x, y);

        return new Branch(pos, angle, RainbowMath.random(DEFAULT_RADIUS * 0.75f, DEFAULT_RADIUS));
    }

    public boolean isDead() {
        return RainbowMath.abs(radius) < MIN_RADIUS;
    }

    public boolean canSproud() {
        return RainbowMath.abs(radius) > MIN_RADIUS_TO_SPROUD;
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
