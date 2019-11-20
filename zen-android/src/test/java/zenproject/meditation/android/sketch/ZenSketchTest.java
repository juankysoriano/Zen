package zenproject.meditation.android.sketch;

import com.juankysoriano.rainbow.core.drawing.RainbowDrawer;
import com.juankysoriano.rainbow.core.event.RainbowInputController;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import zenproject.meditation.android.ZenTestBase;
import zenproject.meditation.android.sketch.music.MusicPerformer;
import zenproject.meditation.android.sketch.painting.SketchInteractionListener;
import zenproject.meditation.android.sketch.painting.flowers.Flower;
import zenproject.meditation.android.sketch.painting.flowers.branch.BranchPerformer;
import zenproject.meditation.android.sketch.painting.ink.InkPerformer;
import zenproject.meditation.android.ui.sketch.ZenSketchView;

import static org.fest.assertions.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;

public class ZenSketchTest extends ZenTestBase {

    private ZenSketch zenSketch;
    @Mock
    private MusicPerformer musicPerformer;
    @Mock
    private InkPerformer inkPerformer;
    @Mock
    private BranchPerformer branchPerformer;
    @Mock
    private ZenSketchView zenSketchView;
    @Mock
    private RainbowDrawer rainbowDrawer;
    @Mock
    private RainbowInputController rainbowInputController;
    @Mock
    private SketchInteractionListener sketchInteractionListener;
    @Mock
    private ZenSketch.Listener sketchListener;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        zenSketch = new ZenSketch(
                musicPerformer,
                inkPerformer,
                branchPerformer,
                zenSketchView,
                rainbowDrawer,
                rainbowInputController,
                sketchInteractionListener,
                sketchListener
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

        verify(rainbowDrawer).background(SKETCH_COLOR);
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
        zenSketch.onDrawingStep();

        verify(musicPerformer).doStep();
    }

    @Test
    public void testThatOnFramePerformsStepOnBranchPerformer() {
        zenSketch.onDrawingStep();

        verify(branchPerformer).doStep();
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

        verify(rainbowDrawer).background(SKETCH_COLOR);
    }

    @Test
    public void testThatClearPerformsResetOnBranchPerformer() {
        zenSketch.clear();

        verify(branchPerformer).reset();
    }

    @Test
    public void testThatNotifiesSketchStart() {
        zenSketch.onDrawingStart();

        verify(sketchListener).onSketchStart();
    }

    @Test
    public void testThatNotifiesSketchStop() {
        zenSketch.onDrawingStop();

        verify(sketchListener).onSketchStop();
    }

    @Test
    public void testThatNewInstanceReturnsNotNullBrushOptionsPreferences() {
        assertThat(ZenSketch.newInstance(zenSketchView, sketchListener)).isNotNull();
    }

    @Test
    public void testThatNewInstanceReturnsANewInstance() {
        ZenSketch firstInstance = ZenSketch.newInstance(zenSketchView, sketchListener);
        ZenSketch secondInstance = ZenSketch.newInstance(zenSketchView, sketchListener);

        assertThat(firstInstance).isNotEqualTo(secondInstance);
    }
}
