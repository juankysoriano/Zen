package zenproject.meditation.android.sketch.painting.ink;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import zenproject.meditation.android.ZenTestBase;
import zenproject.meditation.android.preferences.BrushOptionsPreferences;

import static org.fest.assertions.api.Assertions.assertThat;

public class InkDropSizeLimiterTest extends ZenTestBase {
    private static final float MINIMUM_RADIUS = 0.75f;
    private static final float MAXIMUM_RADIUS = 120f;

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

        assertThat(inkDropSizeLimiter.getMinimumRadius()).isEqualTo(MINIMUM_RADIUS);
    }

    @Test
    public void getMaximumRadiusReturnsMaximum() {
        givenAPercentageForAVeryHighRadius();

        assertThat(inkDropSizeLimiter.getMaximumRadius()).isEqualTo(MAXIMUM_RADIUS);
    }

    @Test
    public void testThatWhenRadiusIsLowerThatMinimumThenMinimumIsReturned() {
        givenAPercentageForAVeryLowRadius();

        assertThat(inkDropSizeLimiter.getRadius()).isEqualTo(MINIMUM_RADIUS);
    }

    @Test
    public void testThatWhenRadiusIsGreaterThatMaximumThenMaximumIsReturned() {
        givenAPercentageForAVeryHighRadius();

        assertThat(inkDropSizeLimiter.getRadius()).isEqualTo(MAXIMUM_RADIUS);
    }

    @Test
    public void testThatWhenRadiusIsBetweenMaxAndMinRadiusIsReturned() {
        givenAPercentageForARadiusInBetween();

        assertThat(inkDropSizeLimiter.getRadius()).isLessThan(MAXIMUM_RADIUS).isGreaterThan(MINIMUM_RADIUS);
    }

    @Test
    public void testThatNewInstanceReturnsNotNullInkDropSizeLimiter() {
        assertThat(InkDropSizeLimiter.newInstance(brushOptionsPreferences)).isNotNull();
    }

    @Test
    public void testThatNewInstanceReturnsANewInstance() {
        InkDropSizeLimiter firstInstance = InkDropSizeLimiter.newInstance(brushOptionsPreferences);
        InkDropSizeLimiter secondInstance = InkDropSizeLimiter.newInstance(brushOptionsPreferences);

        assertThat(firstInstance).isNotEqualTo(secondInstance);
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
