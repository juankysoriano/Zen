package zenproject.meditation.android.sketch.painting.ink;

import org.fest.assertions.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.annotation.Config;

import zenproject.meditation.android.BuildConfig;
import zenproject.meditation.android.RobolectricLauncherGradleTestRunner;
import zenproject.meditation.android.ZenTestBase;

@RunWith(RobolectricLauncherGradleTestRunner.class)
@Config(constants = BuildConfig.class)
public class BrushColorTest extends ZenTestBase {

    private static final int UNKNOW_COLOR = -1;
    private static final int ACCENT_COLOR = BrushColor.ACCENT.toAndroidColor();

    @Test
    public void testThatGettingBrushColorFromUnknownColorReturnsDARK() {
        Assertions.assertThat(BrushColor.from(UNKNOW_COLOR)).isEqualTo(BrushColor.DARK);
    }

    @Test
    public void testThatGettingFlowerFromKnownOrdinalReturnsCorrectFlower() {
        Assertions.assertThat(BrushColor.from(ACCENT_COLOR)).isEqualTo(BrushColor.ACCENT);
    }
}