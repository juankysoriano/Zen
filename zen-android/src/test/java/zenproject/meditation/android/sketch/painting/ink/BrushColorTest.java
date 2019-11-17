package zenproject.meditation.android.sketch.painting.ink;

import org.junit.Test;

import zenproject.meditation.android.ZenTestBase;

import static org.fest.assertions.api.Assertions.assertThat;

public class BrushColorTest extends ZenTestBase {

    private static final int UNKNOW_COLOR = -1;
    private static final int ACCENT_COLOR = BrushColor.ACCENT.toAndroidColor();

    @Test
    public void testThatGettingBrushColorFromUnknownColorReturnsDARK() {
        assertThat(BrushColor.from(UNKNOW_COLOR)).isEqualTo(BrushColor.DARK);
    }

    @Test
    public void testThatGettingFlowerFromKnownOrdinalReturnsCorrectFlower() {
        assertThat(BrushColor.from(ACCENT_COLOR)).isEqualTo(BrushColor.ACCENT);
    }
}
