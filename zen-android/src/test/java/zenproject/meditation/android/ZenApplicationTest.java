package zenproject.meditation.android;

import org.fest.assertions.api.Assertions;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

import zenproject.meditation.android.preferences.BrushOptionsPreferences;
import zenproject.meditation.android.sketch.painting.ink.BrushColor;

@RunWith(RobolectricLauncherGradleTestRunner.class)
@Config(constants = BuildConfig.class)
public class ZenApplicationTest extends ZenTestBase {
    private BrushOptionsPreferences brushOptionsPreferences;
    private ZenApplication zenApplication;

    @Before
    public void setUp() {
        zenApplication = new ZenApplication();
        brushOptionsPreferences = BrushOptionsPreferences.newInstance();

        ContextRetriever.INSTANCE.inject(RuntimeEnvironment.application);
    }

    @Test
    public void testThatWhenOnCreateIsCalledIfColorIsEraseItIsRestoredToDark() {
        brushOptionsPreferences.applyBrushColor(BrushColor.ERASE);

        zenApplication.onCreate();

        Assertions.assertThat(brushOptionsPreferences.getBrushColor() == BrushColor.DARK);

    }

    @Test
    public void testThatWhenOnCreateIsCalledIfColorIsOtherThanEraseItIsPreserverd() {
        brushOptionsPreferences.applyBrushColor(BrushColor.ACCENT);

        zenApplication.onCreate();

        Assertions.assertThat(brushOptionsPreferences.getBrushColor() == BrushColor.ACCENT);
    }
}