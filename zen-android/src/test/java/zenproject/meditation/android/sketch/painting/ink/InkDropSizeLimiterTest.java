package zenproject.meditation.android.sketch.painting.ink;

import org.fest.assertions.api.Assertions;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import zenproject.meditation.android.ContextRetriever;
import zenproject.meditation.android.R;
import zenproject.meditation.android.RobolectricLauncherGradleTestRunner;
import zenproject.meditation.android.ZenTestBase;
import zenproject.meditation.android.persistence.BrushOptionsPreferences;

@RunWith(RobolectricLauncherGradleTestRunner.class)
public class InkDropSizeLimiterTest extends ZenTestBase {
    private static final float MINIMUM_RADIUS = ContextRetriever.INSTANCE.getResources().getDimension(R.dimen.ink_drop_min_radius);
    private static final float MAXIMUM_RADIUS = ContextRetriever.INSTANCE.getResources().getDimension(R.dimen.ink_drop_max_radius);

    @Mock
    private BrushOptionsPreferences brushOptionsPreferences;

    private InkDropSizeLimiter inkDropSizeLimiter;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        inkDropSizeLimiter = new InkDropSizeLimiter(brushOptionsPreferences);
    }

    @Test
    public void getMinimumRadiusReturnsMinimum() {
        givenAPercentageForAVeryLowRadius();

        Assertions.assertThat(inkDropSizeLimiter.getMinimumRadius()).isEqualTo(MINIMUM_RADIUS);
    }

    @Test
    public void getMaximumRadiusReturnsMaximum() {
        givenAPercentageForAVeryHighRadius();

        Assertions.assertThat(inkDropSizeLimiter.getMaximumRadius()).isEqualTo(MAXIMUM_RADIUS);
    }

    @Test
    public void testThatWhenRadiusIsLowerThatMinimumThenMinimumIsReturned() {
        givenAPercentageForAVeryLowRadius();

        Assertions.assertThat(inkDropSizeLimiter.getRadius()).isEqualTo(MINIMUM_RADIUS);
    }

    @Test
    public void testThatWhenRadiusIsGreaterThatMaximumThenMaximumIsReturned() {
        givenAPercentageForAVeryHighRadius();

        Assertions.assertThat(inkDropSizeLimiter.getRadius()).isEqualTo(MAXIMUM_RADIUS);
    }

    @Test
    public void testThatWhenRadiusIsBetweenMaxAndMinRadiusIsReturned() {
        givenAPercentageForARadiusInBetween();

        Assertions.assertThat(inkDropSizeLimiter.getRadius()).isLessThan(MAXIMUM_RADIUS).isGreaterThan(MINIMUM_RADIUS);
    }

    @Test
    public void testThatNewInstanceReturnsNotNullInkDropSizeLimiter() {
        Assertions.assertThat(InkDropSizeLimiter.newInstance(brushOptionsPreferences)).isNotNull();
    }

    @Test
    public void testThatNewInstanceReturnsANewInstance() {
        InkDropSizeLimiter firstInstance = InkDropSizeLimiter.newInstance(brushOptionsPreferences);
        InkDropSizeLimiter secondInstance = InkDropSizeLimiter.newInstance(brushOptionsPreferences);

        Assertions.assertThat(firstInstance).isNotEqualTo(secondInstance);
    }

    private void givenAPercentageForAVeryLowRadius() {
        Mockito.when(brushOptionsPreferences.getBrushSizePercentage()).thenReturn(-1);
    }

    private void givenAPercentageForAVeryHighRadius() {
        Mockito.when(brushOptionsPreferences.getBrushSizePercentage()).thenReturn(200);
    }

    private void givenAPercentageForARadiusInBetween() {
        Mockito.when(brushOptionsPreferences.getBrushSizePercentage()).thenReturn(50);
    }
}
