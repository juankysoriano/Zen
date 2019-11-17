package zenproject.meditation.android.sketch.painting;

import org.fest.assertions.api.Assertions;
import org.junit.Before;
import org.junit.Test;

import zenproject.meditation.android.ZenTestBase;

public class PaintStepSkipperTest extends ZenTestBase {

    private PaintStepSkipper paintStepSkipper;

    @Before
    public void setUp() {
        paintStepSkipper = new PaintStepSkipper();
    }

    @Test
    public void testThatWhenNoStepIsRecordedThenShouldNotSkipStep() {
        Assertions.assertThat(paintStepSkipper.hasToSkipStep()).isFalse();
    }

    @Test
    public void testThatWhenOneStepIsRecordedThenShouldSkipStep() {
        paintStepSkipper.recordStep();

        Assertions.assertThat(paintStepSkipper.hasToSkipStep()).isTrue();
    }

    @Test
    public void testThatFirstTwoStepsShouldBeSkippedButNotTheThird() {
        for(int i = 0; i < 2 ; i ++) {
            paintStepSkipper.recordStep();
            Assertions.assertThat(paintStepSkipper.hasToSkipStep()).isTrue();
        }
        paintStepSkipper.recordStep();
        Assertions.assertThat(paintStepSkipper.hasToSkipStep()).isFalse();
    }
}
