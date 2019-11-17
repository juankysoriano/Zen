package zenproject.meditation.android.sketch.actions.clear;

import com.google.android.gms.analytics.Tracker;

import org.fest.assertions.api.Assertions;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import zenproject.meditation.android.ZenTestBase;
import zenproject.meditation.android.analytics.AnalyticsTracker;
import zenproject.meditation.android.sketch.ZenSketch;
import zenproject.meditation.android.ui.sketch.ZenSketchView;

import static org.mockito.Matchers.anyMapOf;
import static org.mockito.Mockito.verify;

public class SketchClearerTest extends ZenTestBase {
    @Mock
    private ZenSketch zenSketch;
    @Mock
    private ZenSketchView zenSketchView;
    @Mock
    private Tracker tracker;

    private SketchClearer sketchClearer;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        AnalyticsTracker.INSTANCE.inject(tracker);
        sketchClearer = new SketchClearer(zenSketch, zenSketchView);
    }

    @Test
    public void testThatWhenClearSketchIsPerformedThenZenSketchIsCleared() {
        sketchClearer.clearSketch();

        verify(zenSketchView).clear();
    }

    @Test
    public void testClearSketchTracksAnalytics() {
        sketchClearer.clearSketch();

        verify(tracker).send(anyMapOf(String.class, String.class));
    }

    @Test
    public void testOnSketchClearedClearsZenSketch() {
        sketchClearer.onSketchCleared();

        verify(zenSketch).clear();
    }

    /**
     * "Test everything that you could possibly break"
     * - Kent Beck's
     */

    @Test
    public void testThatNewInstanceReturnsNotNullSketchClearer() {
        Assertions.assertThat(SketchClearer.newInstance(zenSketchView)).isNotNull();
    }

    @Test
    public void testThatNewInstanceReturnsANewInstance() {
        SketchClearer firstInstance = SketchClearer.newInstance(zenSketchView);
        SketchClearer secondInstance = SketchClearer.newInstance(zenSketchView);

        Assertions.assertThat(firstInstance).isNotEqualTo(secondInstance);
    }
}
