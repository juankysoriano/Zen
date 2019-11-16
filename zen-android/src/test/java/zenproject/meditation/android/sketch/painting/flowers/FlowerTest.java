package zenproject.meditation.android.sketch.painting.flowers;

import org.fest.assertions.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.annotation.Config;

import zenproject.meditation.android.BuildConfig;
import zenproject.meditation.android.ZenTestBase;

@RunWith(RobolectricLauncherGradleTestRunner.class)
@Config(constants = BuildConfig.class)
public class FlowerTest extends ZenTestBase {

    private static final int UNKNOWN_ORDINAL = -1;
    private static final int POPPY_ORDINAL = 2;

    @Test
    public void testThatNONEFlowerHasNoLeafs() {
        Assertions.assertThat(Flower.NONE.getFlowerLeafRes()).isEmpty();
    }

    @Test
    public void testThatAllOthersFlowersButNONEhaveLeafs() {
        for(Flower flower: Flower.values()) {
            if(flower != Flower.NONE) {
                Assertions.assertThat(flower.getFlowerLeafRes().size()).isGreaterThan(0);
            }
        }
    }

    @Test
    public void testThatAllAvailableFlowersHasPositiveMinSize() {
        for (Flower flower : Flower.values()) {
            Assertions.assertThat(flower.getMinSize()).isGreaterThanOrEqualTo(0);
        }
    }

    @Test
    public void testThatAllAvailableFlowersHasPositiveMaxSize() {
        for (Flower flower : Flower.values()) {
            Assertions.assertThat(flower.getMaxSize()).isGreaterThanOrEqualTo(0);
        }
    }

    @Test
    public void testThatGettingFlowerFromUnknownOrdinalReturnsNONE() {
        Assertions.assertThat(Flower.from(UNKNOWN_ORDINAL)).isEqualTo(Flower.NONE);
    }

    @Test
    public void testThatGettingFlowerFromKnownOrdinalReturnsCorrectFlower() {
        Assertions.assertThat(Flower.from(POPPY_ORDINAL)).isEqualTo(Flower.POPPY);
    }
}
