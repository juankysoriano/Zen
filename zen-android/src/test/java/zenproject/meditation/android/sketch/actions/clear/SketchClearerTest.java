package zenproject.meditation.android.sketch.actions.clear;

import com.google.android.gms.analytics.Tracker;

import org.fest.assertions.api.Assertions;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import zenproject.meditation.android.RobolectricLauncherGradleTestRunner;
import zenproject.meditation.android.ZenTestBase;
import zenproject.meditation.android.analytics.AnalyticsTracker;
import zenproject.meditation.android.sketch.ZenSketch;

import static org.mockito.Matchers.anyMapOf;
import static org.mockito.Mockito.verify;

@RunWith(RobolectricLauncherGradleTestRunner.class)
public class SketchClearerTest extends ZenTestBase {
    @Mock
    private ZenSketch zenSketch;
    @Mock
    private Tracker tracker;

    private SketchClearer sketchClearer;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        AnalyticsTracker.INSTANCE.inject(tracker);

        sketchClearer = new SketchClearer(zenSketch);
    }

    @Test
    public void testThatWhenClearSketchIsPerformedThenZenSketchIsCleared() {
        sketchClearer.clearSketch();

        verify(zenSketch).clear();
    }

    @Test
    public void testClearSketchTracksAnalytics() {
        sketchClearer.clearSketch();

        verify(tracker).send(anyMapOf(String.class, String.class));
    }

    /**
     * "Test everything that you could possibly break"
     * - Kent Beck's
     */

    @Test
    public void testThatSketchClearerNewInstanceReturnsNotNullSketchClearer() {
        Assertions.assertThat(SketchClearer.newInstance(zenSketch)).isNotNull();
    }

    @Test
    public void testThatSketchClearerNewInstanceReturnsANewInstance() {
        SketchClearer firstInstance = SketchClearer.newInstance(zenSketch);
        SketchClearer secondInstance = SketchClearer.newInstance(zenSketch);

        Assertions.assertThat(firstInstance).isNotEqualTo(secondInstance);
    }
}
