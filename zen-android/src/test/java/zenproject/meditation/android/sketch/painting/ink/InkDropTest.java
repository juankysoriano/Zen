package zenproject.meditation.android.sketch.painting.ink;

import com.juankysoriano.rainbow.core.event.RainbowInputController;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import zenproject.meditation.android.ContextRetriever;
import zenproject.meditation.android.R;
import zenproject.meditation.android.RobolectricLauncherGradleTestRunner;
import zenproject.meditation.android.ZenTestBase;
import zenproject.meditation.android.preferences.BrushOptionsPreferences;

import static org.fest.assertions.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(RobolectricLauncherGradleTestRunner.class)
@Config(constants = BuildConfig.class)
public class InkDropTest extends ZenTestBase {
    private static final float INITIAL_RADIUS = ContextRetriever.INSTANCE.getResources().getDimension(R.dimen.ink_drop_min_radius);
    private static final float INK_VELOCITY_THRESHOLD = ContextRetriever.INSTANCE.getResources().getDimension(R.dimen.ink_velocity_threshold);
    private static final float RADIUS_STEP = ContextRetriever.INSTANCE.getResources().getDimension(R.dimen.ink_drop_step);
    private static final float BIG_RADIUS = 10f;

    @Mock
    private InkDropSizeLimiter inkDropSizeLimiter;
    @Mock
    private BrushOptionsPreferences brushOptionsPreferences;
    @Mock
    private RainbowInputController rainbowInputController;

    private InkDrop inkDrop;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        when(inkDropSizeLimiter.getMaximumRadius()).thenReturn(BIG_RADIUS);
        when(inkDropSizeLimiter.getMinimumRadius()).thenReturn(INITIAL_RADIUS);
        inkDrop = new InkDrop(inkDropSizeLimiter, brushOptionsPreferences, rainbowInputController);
    }

    @Test
    public void testThatInitialRadiusIsTheAbsoluteMinRadius() {
        assertThat(inkDrop.getRadius()).isEqualTo(INITIAL_RADIUS);
    }

    @Test
    public void testThatGetBrushColorRetrievesFromBrushOptionsPreferences() {
        inkDrop.getBrushColor();

        verify(brushOptionsPreferences).getBrushColor();
    }

    @Test
    public void testThatGetMaxRadiusRetrievesFromInkDropSizeLimiter() {
        inkDrop.getMaxRadius();

        verify(inkDropSizeLimiter).getMaximumRadius();
    }

    @Test
    public void testThatUpdateInkRadiusIncreasesRadiusIfTouchingScreenAndFingerMovingSlow() {
        givenThatRadiusShouldIncrease();

        inkDrop.updateInkRadius();

        assertThat(inkDrop.getRadius()).isGreaterThan(INITIAL_RADIUS);
    }

    @Test
    public void testThatUpdateInkRadiusDecreasesRadiusIfFingerMovingFast() {
        givenThatRadiusHasPreviouslyIncreasedTwoSteps();
        givenThatFingerIsMovingFast();
        givenThatScreenIsTouched();

        inkDrop.updateInkRadius();

        assertThat(inkDrop.getRadius()).isEqualTo(RADIUS_STEP + INITIAL_RADIUS);
    }

    @Test
    public void testThatUpdateInkRadiusDecreasesRadiusIfNotTouchingScreen() {
        givenThatRadiusHasPreviouslyIncreasedTwoSteps();
        givenThatFingerIsMovingSlow();
        givenThatScreenIsNotTouched();

        inkDrop.updateInkRadius();

        assertThat(inkDrop.getRadius()).isEqualTo(RADIUS_STEP + INITIAL_RADIUS);
    }

    @Test
    public void testThatResetRadiusSetsRadiusToAbsoluteMinimum() {
        givenThatRadiusHasPreviouslyIncreasedTwoSteps();

        inkDrop.resetRadius();

        assertThat(inkDrop.getRadius()).isEqualTo(INITIAL_RADIUS);
    }

    @Test
    public void testThatNewInstanceReturnsNotNullInkDrop() {
        assertThat(InkDrop.newInstance(rainbowInputController)).isNotNull();
    }

    @Test
    public void testThatNewInstanceReturnsANewInstance() {
        InkDrop firstInstance = InkDrop.newInstance(rainbowInputController);
        InkDrop secondInstance = InkDrop.newInstance(rainbowInputController);

        assertThat(firstInstance).isNotEqualTo(secondInstance);
    }

    private void givenThatFingerIsMovingSlow() {
        when(rainbowInputController.getFingerVelocity()).thenReturn(-1f);
    }

    private void givenThatFingerIsMovingFast() {
        when(rainbowInputController.getFingerVelocity()).thenReturn(INK_VELOCITY_THRESHOLD+1f);
    }

    private void givenThatScreenIsTouched() {
        when(rainbowInputController.isScreenTouched()).thenReturn(true);
    }

    private void givenThatScreenIsNotTouched() {
        when(rainbowInputController.isScreenTouched()).thenReturn(false);
    }

    private void givenThatRadiusHasPreviouslyIncreasedTwoSteps() {
        givenThatRadiusShouldIncrease();
        inkDrop.updateInkRadius();
        inkDrop.updateInkRadius();
    }

    private void givenThatRadiusShouldIncrease() {
        givenThatScreenIsTouched();
        givenThatFingerIsMovingSlow();
    }
}