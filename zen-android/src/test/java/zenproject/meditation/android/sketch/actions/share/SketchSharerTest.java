package zenproject.meditation.android.sketch.actions.share;

import android.app.Activity;
import android.content.Intent;

import com.google.android.gms.analytics.Tracker;
import com.juankysoriano.rainbow.core.drawing.RainbowDrawer;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.robolectric.annotation.Config;

import zenproject.meditation.android.BuildConfig;
import zenproject.meditation.android.ContextRetriever;
import zenproject.meditation.android.RobolectricLauncherGradleTestRunner;
import zenproject.meditation.android.ZenTestBase;
import zenproject.meditation.android.analytics.AnalyticsTracker;
import zenproject.meditation.android.sketch.ZenSketch;

import static org.fest.assertions.api.Assertions.assertThat;
import static org.mockito.Matchers.anyMapOf;
import static org.mockito.Mockito.verify;

@RunWith(RobolectricLauncherGradleTestRunner.class)
@Config(constants = BuildConfig.class)
public class SketchSharerTest extends ZenTestBase {
    @Mock
    private ZenSketch zenSketch;
    @Mock
    private RainbowDrawer rainbowDrawer;
    @Mock
    private Tracker tracker;
    @Mock
    private Activity activity;
    @Captor
    private ArgumentCaptor<Intent> intentCaptor;

    private SketchSharer sketchSharer;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        AnalyticsTracker.INSTANCE.inject(tracker);
        ContextRetriever.INSTANCE.inject(activity);

        sketchSharer = new SketchSharer(rainbowDrawer);
    }

    @Test
    public void testShareSketchTracksAnalytics() {
        sketchSharer.shareSketch();

        verify(tracker).send(anyMapOf(String.class, String.class));
    }

    @Test
    public void testThatShareSketchStartsActivityWithActionChooser() {
        sketchSharer.shareSketch();

        verify(activity).startActivity(intentCaptor.capture());

        assertThat(intentCaptor.getValue().getAction()).isEqualTo(Intent.ACTION_CHOOSER);
    }

    @Test
    public void testThatNewInstanceReturnsNotNullSketchSharer() {
        assertThat(SketchSharer.newInstance()).isNotNull();
    }

    @Test
    public void testThatNewInstanceReturnsANewInstance() {
        SketchSharer secondInstance = SketchSharer.newInstance();
        SketchSharer firstInstance = SketchSharer.newInstance();

        assertThat(firstInstance).isNotEqualTo(secondInstance);
    }
}