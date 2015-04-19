package zenproject.meditation.android.sketch.painting;

import android.view.MotionEvent;

import com.juankysoriano.rainbow.core.drawing.RainbowDrawer;

import org.fest.assertions.api.Assertions;
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
import zenproject.meditation.android.sketch.painting.ink.InkPerformer;

import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

@RunWith(RobolectricLauncherGradleTestRunner.class)
@Config(constants = BuildConfig.class)
public class SketchInteractionListenerTest extends ZenTestBase {
    @Mock
    private InkPerformer inkPerformer;
    @Mock
    private RainbowDrawer rainbowDrawer;
    @Mock
    private ZenSketch.PaintListener listener;
    @Mock
    private MotionEvent motionEvent;

    private SketchInteractionListener sketchInteractionListener;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        sketchInteractionListener = new SketchInteractionListener(inkPerformer);
    }

    @Test
    public void testThatOnScreenTouchedPerformsResetOnInkPerformer() {
        sketchInteractionListener.onSketchTouched(motionEvent, rainbowDrawer);

        verify(inkPerformer).reset();
    }

    @Test
    public void testThatIfHaveOnPaintingListenerThenOnScreenTouchFiresOnPaintingStart() {
        givenThatHasPaintingListener();

        sketchInteractionListener.onSketchTouched(motionEvent, rainbowDrawer);

        verify(listener).onPaintingStart();
    }

    @Test
    public void testThatIfDoesNotHaveOnPaintingListenerThenOnScreenTouchDoesNotFireOnPaintingStart() {
        sketchInteractionListener.onSketchTouched(motionEvent, rainbowDrawer);

        verify(listener, never()).onPaintingStart();
    }

    @Test
    public void testThatIfHaveOnPaintingListenerThenOnScreenReleasedFiresOnPaintingEnd() {
        givenThatHasPaintingListener();

        sketchInteractionListener.onSketchReleased(motionEvent, rainbowDrawer);

        verify(listener).onPaintingEnd();
    }

    @Test
    public void testThatIfDoesNotHaveOnPaintingListenerThenOnScreenReleasedDoesNotFireOnPaintingEnd() {
        sketchInteractionListener.onSketchReleased(motionEvent, rainbowDrawer);

        verify(listener, never()).onPaintingEnd();
    }

    @Test
    public void testThatOnFingerDraggedDoesStepOnInkPerformer() {
        sketchInteractionListener.onFingerDragged(motionEvent, rainbowDrawer);

        verify(inkPerformer).doStep();
    }

    @Test
    public void testThatNewInstanceReturnsNotNullSketchInteractionListener() {
        Assertions.assertThat(SketchInteractionListener.newInstance(inkPerformer)).isNotNull();
    }

    @Test
    public void testThatNewInstanceReturnsANewInstance() {
        SketchInteractionListener firstInstance = SketchInteractionListener.newInstance(inkPerformer);
        SketchInteractionListener secondInstance = SketchInteractionListener.newInstance(inkPerformer);

        Assertions.assertThat(firstInstance).isNotEqualTo(secondInstance);
    }

    private void givenThatHasPaintingListener() {
        sketchInteractionListener.setPaintListener(listener);
    }

}