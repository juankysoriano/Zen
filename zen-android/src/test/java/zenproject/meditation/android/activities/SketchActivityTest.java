package zenproject.meditation.android.activities;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import zenproject.meditation.android.RobolectricLauncherGradleTestRunner;
import zenproject.meditation.android.ZenTestBase;
import zenproject.meditation.android.sketch.ZenSketch;
import zenproject.meditation.android.sketch.actions.clear.SketchClearer;
import zenproject.meditation.android.sketch.actions.screenshot.ScreenshotTaker;
import zenproject.meditation.android.ui.sketch.ZenSketchView;

import static org.mockito.Mockito.verify;

@RunWith(RobolectricLauncherGradleTestRunner.class)
public class SketchActivityTest extends ZenTestBase {
    @Mock
    private ZenSketch zenSketch;
    @Mock
    private SketchClearer sketchClearer;
    @Mock
    private ScreenshotTaker screenshotTaker;
    @Mock
    private Navigator navigator;
    @Mock
    private ZenSketchView zenSketchView;

    private SketchActivity sketchActivity;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        sketchActivity = new SketchActivity(zenSketch, navigator, screenshotTaker, sketchClearer);
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