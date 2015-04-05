package zenproject.meditation.android.sketch.actions.screenshot;

import com.google.android.gms.analytics.Tracker;
import com.juankysoriano.rainbow.core.drawing.RainbowDrawer;

import org.fest.assertions.api.Assertions;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import zenproject.meditation.android.ContextRetriever;
import zenproject.meditation.android.R;
import zenproject.meditation.android.RobolectricLauncherGradleTestRunner;
import zenproject.meditation.android.ZenTestBase;
import zenproject.meditation.android.analytics.AnalyticsTracker;
import zenproject.meditation.android.sketch.ZenSketch;

import static org.mockito.Matchers.anyMapOf;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(RobolectricLauncherGradleTestRunner.class)
public class ScreenshotTakerTest extends ZenTestBase {
    private static final String ZEN = ContextRetriever.INSTANCE.getResources().getString(R.string.app_name);
    private static final String PICTURE_TITLE = ContextRetriever.INSTANCE.getResources().getString(R.string.picture_title);

    @Mock
    private ZenSketch zenSketch;
    @Mock
    private RainbowDrawer rainbowDrawer;
    @Mock
    private Tracker tracker;

    private ScreenshotTaker screenshotTaker;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        AnalyticsTracker.INSTANCE.inject(tracker);
        when(zenSketch.getRainbowDrawer()).thenReturn(rainbowDrawer);

        screenshotTaker = new ScreenshotTaker(zenSketch);
    }

    @Test
    public void testTakeScreenshotPerformsSaveOnRainbowDrawer() throws Exception {
        screenshotTaker.takeScreenshot();

        verify(rainbowDrawer).save(ZEN, PICTURE_TITLE);
    }

    @Test
    public void testTakeScreenshotTracksAnalytics() {
        screenshotTaker.takeScreenshot();

        verify(tracker).send(anyMapOf(String.class, String.class));
    }

    @Test
    public void testThatScreenshotTakerNewInstanceReturnsNotNullSketchClearer() {
        Assertions.assertThat(ScreenshotTaker.newInstance(zenSketch)).isNotNull();
    }

    @Test
    public void testThatScreenshotTakerNewInstanceReturnsANewInstance() {
        ScreenshotTaker secondInstance = ScreenshotTaker.newInstance(zenSketch);
        ScreenshotTaker firstInstance = ScreenshotTaker.newInstance(zenSketch);

        Assertions.assertThat(firstInstance).isNotEqualTo(secondInstance);
    }
}