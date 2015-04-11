package zenproject.meditation.android.sketch.painting.flowers.branch;

import com.juankysoriano.rainbow.core.matrix.RVector;

import org.junit.Test;
import org.junit.runner.RunWith;

import zenproject.meditation.android.ContextRetriever;
import zenproject.meditation.android.R;
import zenproject.meditation.android.RobolectricLauncherGradleTestRunner;
import zenproject.meditation.android.ZenTestBase;

import static org.fest.assertions.api.Assertions.assertThat;

@RunWith(RobolectricLauncherGradleTestRunner.class)
public class BranchTest extends ZenTestBase {
    private static final float ANY_POSITION = 10;
    private static final float MIN_RADIUS = ContextRetriever.INSTANCE.getResources().getDimension(R.dimen.branch_min_radius);
    private static final float RADIUS_OVER_MIN = MIN_RADIUS * 2;
    private static final float RADIUS_UNDER_MIN = MIN_RADIUS / 2;
    private static final float MIN_RADIUS_TO_BLOOM = ContextRetriever.INSTANCE.getResources().getDimension(R.dimen.branch_min_bloom_radius);
    private static final float RADIUS_TO_BLOOM_OVER_MIN = MIN_RADIUS_TO_BLOOM * 2;
    private static final float RADIUS_TO_BLOOM_UNDER_MIN = MIN_RADIUS_TO_BLOOM / 2;
    private static final float ANY_RADIUS = 2;
    private static final float ANY_ANGLE = 2;

    private Branch branch;

    @Test
    public void testThatCreateFromCreatesBranchFromThePositionOfPassedBranch() {
        Branch otherBranch = Branch.createAt(ANY_POSITION, ANY_POSITION);
        branch = Branch.createFrom(otherBranch);

        assertThat(branch.getX()).isEqualTo(otherBranch.getX());
        assertThat(branch.getY()).isEqualTo(otherBranch.getY());
    }

    @Test
    public void testThatCreateAtCreatesBranchFromThePositionPassed() {
        branch = Branch.createAt(ANY_POSITION, ANY_POSITION);

        assertThat(branch.getX()).isEqualTo(ANY_POSITION);
        assertThat(branch.getY()).isEqualTo(ANY_POSITION);
    }

    @Test
    public void testThatIsDeadReturnsTrueIfRadiusIsLowerThanMinRadius() {
        branch = new Branch(new RVector(ANY_POSITION, ANY_POSITION), ANY_ANGLE, RADIUS_UNDER_MIN, 0);

        assertThat(branch.isDead()).isTrue();
    }

    @Test
    public void testThatIsDeadReturnsFalseIFRadiusIsGreaterThanMinRadius() {
        branch = new Branch(new RVector(ANY_POSITION, ANY_POSITION), ANY_ANGLE, RADIUS_OVER_MIN, 0);

        assertThat(branch.isDead()).isFalse();
    }

    @Test
    public void testThatCanBloomReturnsFalseIfRadiusIsLowerThanMinBloomRadius() {
        branch = new Branch(new RVector(ANY_POSITION, ANY_POSITION), ANY_ANGLE, RADIUS_TO_BLOOM_UNDER_MIN, 0);

        assertThat(branch.canBloom()).isFalse();
    }

    @Test
    public void testThatIsDeadReturnsTrueIfRadiusIsGreaterThanMinBloomRadius() {
        branch = new Branch(new RVector(ANY_POSITION, ANY_POSITION), ANY_ANGLE, RADIUS_TO_BLOOM_OVER_MIN, 0);

        assertThat(branch.canBloom()).isTrue();
    }

    @Test
    public void testThatUpdateChangesPosition() {
        branch = new Branch(new RVector(ANY_POSITION, ANY_POSITION), ANY_ANGLE, ANY_RADIUS, 0);

        branch.update();

        assertThat(branch.getX()).isNotEqualTo(branch.getOldX());
        assertThat(branch.getX()).isNotEqualTo(branch.getOldY());
    }

    @Test
    public void testThatUpdateBacksUpPosition() {
        branch = new Branch(new RVector(ANY_POSITION, ANY_POSITION), ANY_ANGLE, ANY_RADIUS, 0);

        branch.update();

        assertThat(branch.getX()).isNotEqualTo(ANY_POSITION);
        assertThat(branch.getX()).isNotEqualTo(ANY_POSITION);
        assertThat(branch.getOldX()).isEqualTo(ANY_POSITION);
        assertThat(branch.getOldY()).isEqualTo(ANY_POSITION);
    }
}