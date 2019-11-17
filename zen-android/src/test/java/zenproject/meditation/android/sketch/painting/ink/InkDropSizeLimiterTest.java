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

        assertThat(inkDropSizeLimiter.getMinimumRadius()).isEqualTo(INK_DROP_MIN_RADIUS);
    }

    @Test
    public void getMaximumRadiusReturnsMaximum() {
        givenAPercentageForAVeryHighRadius();

        assertThat(inkDropSizeLimiter.getMaximumRadius()).isEqualTo(INK_DROP_MAX_RADIUS);
    }

    @Test
    public void testThatWhenRadiusIsLowerThatMinimumThenMinimumIsReturned() {
        givenAPercentageForAVeryLowRadius();

        assertThat(inkDropSizeLimiter.getRadius()).isEqualTo(INK_DROP_MIN_RADIUS);
    }

    @Test
    public void testThatWhenRadiusIsGreaterThatMaximumThenMaximumIsReturned() {
        givenAPercentageForAVeryHighRadius();

        assertThat(inkDropSizeLimiter.getRadius()).isEqualTo(INK_DROP_MAX_RADIUS);
    }

    @Test
    public void testThatWhenRadiusIsBetweenMaxAndMinRadiusIsReturned() {
        givenAPercentageForARadiusInBetween();

        assertThat(inkDropSizeLimiter.getRadius()).isLessThan(INK_DROP_MAX_RADIUS).isGreaterThan(INK_DROP_MIN_RADIUS);
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
