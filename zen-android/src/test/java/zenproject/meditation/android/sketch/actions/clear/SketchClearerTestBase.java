package zenproject.meditation.android.sketch.actions.clear;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import zenproject.meditation.android.RobolectricLauncherGradleTestRunner;
import zenproject.meditation.android.ZenTestBase;
import zenproject.meditation.android.sketch.ZenSketch;

import static org.mockito.Mockito.verify;

@RunWith(RobolectricLauncherGradleTestRunner.class)
public class SketchClearerTestBase extends ZenTestBase {
    @Mock
    private ZenSketch zenSketch;

    private SketchClearer sketchClearer;

    @Before
    public void setUp() {

        MockitoAnnotations.initMocks(this);
        sketchClearer = new SketchClearer(zenSketch);
    }

    @Test
    public void testThatWhenClearSketchIsPerformedThenZenSketchIsCleared() {
        sketchClearer.clearSketch();

        verify(zenSketch).clear();
    }
}
