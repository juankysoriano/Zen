package zenproject.meditation.android.sketch;

import com.juankysoriano.rainbow.core.drawing.RainbowDrawer;
import com.juankysoriano.rainbow.core.event.RainbowInputController;

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
import zenproject.meditation.android.sketch.music.MusicPerformer;
import zenproject.meditation.android.sketch.painting.SketchInteractionListener;
import zenproject.meditation.android.sketch.painting.flowers.Flower;
import zenproject.meditation.android.sketch.painting.flowers.branch.BranchPerformer;
import zenproject.meditation.android.sketch.painting.ink.InkPerformer;

import static org.mockito.Mockito.verify;

@RunWith(RobolectricLauncherGradleTestRunner.class)
public class ZenSketchTest extends ZenTestBase {
    private static final int DEFAULT_COLOR = ContextRetriever.INSTANCE.getResources().getColor(R.color.colorSketch);

    private ZenSketch zenSketch;
    @Mock
    private MusicPerformer musicPerformer;
    @Mock
    private InkPerformer inkPerformer;
    @Mock
    private BranchPerformer branchPerformer;
    @Mock
    private RainbowDrawer rainbowDrawer;
    @Mock
    private RainbowInputController rainbowInputController;
    @Mock
    private SketchInteractionListener sketchInteractionListener;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        zenSketch = new ZenSketch(
                musicPerformer,
                inkPerformer,
                branchPerformer,
                rainbowDrawer,
                rainbowInputController,
                sketchInteractionListener
        );
    }

    @Test
    public void testThatOnSketchSetupAttachesInteractionListener() {
        zenSketch.onSketchSetup();

        verify(rainbowInputController).attach(sketchInteractionListener);
    }

    @Test
    public void testThatOnSketchSetupSetsDefaultBackgroundColor() {
        zenSketch.onSketchSetup();

        verify(rainbowDrawer).background(0);
    }

    @Test
    public void testThatOnSketchSetupInitInkPerformer() {
        zenSketch.onSketchSetup();

        verify(inkPerformer).init();
    }

    @Test
    public void testThatOnSketchSetupInitBranchPerformer() {
        zenSketch.onSketchSetup();

        verify(branchPerformer).init();
    }

    @Test
    public void testThatOnSketchSetupInitMusicPerformer() {
        zenSketch.onSketchSetup();

        verify(musicPerformer).init();
    }

    @Test
    public void testThatOnStepPerformsStepOnMusicPerformer() {
        zenSketch.onStep();

        verify(branchPerformer).doStep();
    }

    @Test
    public void testThatOnFramePerformsStepOnMusicPerformer() {
        zenSketch.onFrame();

        verify(musicPerformer).doStep();
    }

    @Test
    public void testThatOnDrawingPauseDisablesMusicPerformer() {
        zenSketch.onDrawingPause();

        verify(musicPerformer).disable();
    }

    @Test
    public void testThatOnDrawingResumeEnablesMusicPerformer() {
        zenSketch.onDrawingResume();

        verify(musicPerformer).enable();
    }

    @Test
    public void testThatOnSketchDestroyedDetachesInteractionListener() {
        zenSketch.onSketchDestroy();

        verify(rainbowInputController).detach();
    }

    @Test
    public void testThatOnFlowerSelectedDelegatesToBranchDrawer() {
        zenSketch.onFlowerSelected(Flower.POPPY);

        verify(branchPerformer).onFlowerSelected(Flower.POPPY);
    }

    @Test
    public void testThatClearRestoresDefaultColorToRainbowDrawer() {
        zenSketch.clear();

        verify(rainbowDrawer).background(DEFAULT_COLOR);
    }

    @Test
    public void testThatClearPerformsResetOnBranchPerformer() {
        zenSketch.clear();

        verify(branchPerformer).reset();
    }

    @Test
    public void testThatNewInstanceReturnsNotNullBrushOptionsPreferences() {
        Assertions.assertThat(ZenSketch.newInstance()).isNotNull();
    }

    @Test
    public void testThatNewInstanceReturnsANewInstance() {
        ZenSketch firstInstance = ZenSketch.newInstance();
        ZenSketch secondInstance = ZenSketch.newInstance();

        Assertions.assertThat(firstInstance).isNotEqualTo(secondInstance);
    }
}
