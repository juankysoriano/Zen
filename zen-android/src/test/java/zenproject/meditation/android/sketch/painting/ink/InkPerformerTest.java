package zenproject.meditation.android.sketch.painting.ink;

import com.juankysoriano.rainbow.core.drawing.RainbowDrawer;
import com.juankysoriano.rainbow.core.event.RainbowInputController;
import com.juankysoriano.rainbow.core.graphics.RainbowImage;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.robolectric.annotation.Config;

import zenproject.meditation.android.BuildConfig;
import zenproject.meditation.android.R;
import zenproject.meditation.android.RobolectricLauncherGradleTestRunner;
import zenproject.meditation.android.ZenTestBase;
import zenproject.meditation.android.sketch.painting.flowers.branch.BranchesList;

import static org.fest.assertions.api.Assertions.assertThat;
import static org.mockito.Matchers.anyFloat;
import static org.mockito.Mockito.*;

@RunWith(RobolectricLauncherGradleTestRunner.class)
@Config(constants = BuildConfig.class)
public class InkPerformerTest extends ZenTestBase {

    @Mock
    private InkDrop inkDrop;
    @Mock
    private BranchesList branchesList;
    @Spy
    private RainbowDrawer rainbowDrawer = new RainbowDrawer();
    @Mock
    private RainbowInputController rainbowInputController;
    @Mock
    private RainbowImage loadedImage;

    private InkPerformer inkPerformer;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        when(inkDrop.getBrushColor()).thenReturn(BrushColor.DARK);
        disableRainbowDrawerInvocations();

        inkPerformer = new InkPerformer(inkDrop, rainbowDrawer, rainbowInputController);
    }

    @Test
    public void testThatInitConfiguresRainbowDrawer() {
        inkPerformer.init();

        verify(rainbowDrawer).noStroke();
        verify(rainbowDrawer).smooth();
        verify(rainbowDrawer).loadImage(R.drawable.brush_ink, RainbowImage.LOAD_ORIGINAL_SIZE, inkPerformer);
    }

    @Test
    public void testThatWhenDoStepIfEnabledThenInkDropRadiusIsUpdated() {
        givenThatFingerHasMoved();
        inkPerformer.enable();

        inkPerformer.doStep();

        verify(inkDrop, atLeast(1)).updateInkRadius();
    }

    @Test
    public void testThatWhenDoStepIfDisabledThenDropWithoutImageIsPainted() {
        givenThatFingerHasMoved();
        inkPerformer.onLoadFail();
        inkPerformer.disable();

        inkPerformer.doStep();

        verify(rainbowDrawer, never()).line(anyFloat(), anyFloat(), anyFloat(), anyFloat());
    }

    @Test
    public void testThatWhenDoStepIfDisabledThenInkDropRadiusINotsUpdated() {
        givenThatFingerHasMoved();
        inkPerformer.disable();

        inkPerformer.doStep();

        verify(inkDrop, never()).updateInkRadius();
    }

    @Test
    public void testThatWhenDoStepIfEnabledAndLoadFailThenDropWithoutImageIsPainted() {
        givenThatFingerHasMoved();
        inkPerformer.onLoadFail();
        inkPerformer.enable();

        inkPerformer.doStep();

        verify(rainbowDrawer, atLeast(1)).line(anyFloat(), anyFloat(), anyFloat(), anyFloat());
    }

    @Test
    public void testThatWhenDoStepIfEnabledAndLoadSuccessThenDropWithoutImageIsPainted() {
        givenThatFingerHasMoved();
        inkPerformer.onLoadSucceed(loadedImage);
        inkPerformer.enable();

        inkPerformer.doStep();

        verify(rainbowDrawer, atLeast(1)).line(anyFloat(), anyFloat(), anyFloat(), anyFloat());
    }

    @Test
    public void testThatWhenDoStepIfEnabledColorIsRetrievedFromInkDrop() {
        givenThatFingerHasMoved();
        inkPerformer.enable();

        inkPerformer.doStep();

        verify(inkDrop, atLeast(1)).getBrushColor();
    }

    @Test
    public void testThatWhenResetThenInkDropRadiusIsReset() {
        inkPerformer.reset();

        verify(inkDrop).resetRadius();
    }

    @Test
    public void testThatNewInstanceReturnsNotNullInkPerformer() {
        assertThat(InkPerformer.newInstance(inkDrop, rainbowDrawer, rainbowInputController)).isNotNull();
    }

    @Test
    public void testThatNewInstanceReturnsANewInstance() {
        InkPerformer firstInstance = InkPerformer.newInstance(inkDrop, rainbowDrawer, rainbowInputController);
        InkPerformer secondInstance = InkPerformer.newInstance(inkDrop, rainbowDrawer, rainbowInputController);

        assertThat(firstInstance).isNotEqualTo(secondInstance);
    }

    private void givenThatFingerHasMoved() {
        when(rainbowInputController.getPreviousSmoothX()).thenReturn(1f);
        when(rainbowInputController.getPreviousSmoothY()).thenReturn(1f);
        when(rainbowInputController.getSmoothX()).thenReturn(2f);
        when(rainbowInputController.getSmoothY()).thenReturn(2f);
    }

    private void disableRainbowDrawerInvocations() {
        Mockito.doNothing().when(rainbowDrawer).noStroke();
        Mockito.doNothing().when(rainbowDrawer).smooth();
        Mockito.doNothing().when(rainbowDrawer).loadImage(anyInt(), anyInt(), any(RainbowImage.LoadPictureListener.class));

        Mockito.doNothing().when(rainbowDrawer).tint(anyInt());
        Mockito.doNothing().when(rainbowDrawer).imageMode(anyInt());
        Mockito.doNothing().when(rainbowDrawer).pushMatrix();
        Mockito.doNothing().when(rainbowDrawer).translate(anyFloat(), anyFloat());
        Mockito.doNothing().when(rainbowDrawer).rotate(anyFloat());
        Mockito.doNothing().when(rainbowDrawer).image(any(RainbowImage.class), anyFloat(), anyFloat(), anyFloat(), anyFloat());
        Mockito.doNothing().when(rainbowDrawer).tint(anyInt());

        Mockito.doNothing().when(rainbowDrawer).stroke(anyInt(), anyFloat());
        Mockito.doNothing().when(rainbowDrawer).strokeWeight(anyFloat());
        Mockito.doNothing().when(rainbowDrawer).line(anyFloat(), anyFloat(), anyFloat(), anyFloat());

    }
}