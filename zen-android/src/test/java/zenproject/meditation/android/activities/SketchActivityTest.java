package zenproject.meditation.android.activities;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.robolectric.annotation.Config;

import zenproject.meditation.android.BuildConfig;
import zenproject.meditation.android.RobolectricLauncherGradleTestRunner;
import zenproject.meditation.android.ZenTestBase;
import zenproject.meditation.android.sketch.ZenSketch;

import static org.mockito.Mockito.verify;

@RunWith(RobolectricLauncherGradleTestRunner.class)
@Config(constants = BuildConfig.class)
public class SketchActivityTest extends ZenTestBase {
    @Mock
    private ZenSketch zenSketch;

    private SketchActivity sketchActivity;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        sketchActivity = new SketchActivity(zenSketch);
    }

    @Test
    public void testThatOnStartZenSketchIsStarted() {
        sketchActivity.onStart();

        verify(zenSketch).start();
    }

    @Test
    public void testThatOnResumeZenSketchIsResumed() {
        sketchActivity.onResume();

        verify(zenSketch).resume();
    }

    @Test
    public void testThatOnPauseZenSketchIsPaused() {
        sketchActivity.onPause();

        verify(zenSketch).pause();
    }

    @Test
    public void testThatOnStopZenSketchIsStopped() {
        sketchActivity.onStop();

        verify(zenSketch).stop();
    }

    @Test
    public void testThatOnDestroyZenSketchIsDestroyed() {
        sketchActivity.onDestroy();

        verify(zenSketch).destroy();
    }
}