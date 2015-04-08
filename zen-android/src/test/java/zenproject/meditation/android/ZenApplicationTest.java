package zenproject.meditation.android;

import org.fest.assertions.api.Assertions;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import zenproject.meditation.android.persistence.BrushOptionsPreferences;
import zenproject.meditation.android.sketch.painting.ink.BrushColor;

@RunWith(RobolectricLauncherGradleTestRunner.class)
public class ZenApplicationTest extends ZenTestBase {

    private ZenApplication zenApplication;
    private BrushOptionsPreferences brushOptionsPreferences;

    @Before
    public void setUp() {
        brushOptionsPreferences = BrushOptionsPreferences.newInstance();
        zenApplication = new ZenApplication();
    }

    @Test
    public void testThatOnTerminateIfColorIsEraseThenItIsReplacedByDark() {
        givenThatColorBeforeTerminateIs(BrushColor.ERASE);
        zenApplication.onTerminate();

        Assertions.assertThat(brushOptionsPreferences.getBrushColor()).isEqualTo(BrushColor.DARK);
    }

    @Test
    public void testThatOnTerminateIfColorIsNotEraseThenItIsNotChanged() {
        givenThatColorBeforeTerminateIs(BrushColor.ACCENT);
        zenApplication.onTerminate();

        Assertions.assertThat(brushOptionsPreferences.getBrushColor()).isEqualTo(BrushColor.ACCENT);
    }

    private void givenThatColorBeforeTerminateIs(BrushColor brushColor) {
        brushOptionsPreferences.applyBrushColor(brushColor);
    }
}