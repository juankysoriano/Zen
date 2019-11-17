package zenproject.meditation.android.sketch.painting.ink;

import org.junit.Test;

import zenproject.meditation.android.ZenTestBase;

import static org.fest.assertions.api.Assertions.assertThat;

public class BrushColorTest extends ZenTestBase {

    private static final int UNKNOW_COLOR = -1;

    @Test
    public void testThatGettingBrushColorFromUnknownColorReturnsDARK() {
        assertThat(BrushColor.from(UNKNOW_COLOR)).isEqualTo(BrushColor.DARK);
    }

    @Test
    public void testThatGettingFlowerFromKnownOrdinalReturnsCorrectFlower() {
        assertThat(BrushColor.from(BRUSH_PRIMARY_COLOR)).isEqualTo(BrushColor.PRIMARY);
        assertThat(BrushColor.from(BRUSH_ACCENT_COLOR)).isEqualTo(BrushColor.ACCENT);
        assertThat(BrushColor.from(DARK_BRUSH_COLOR)).isEqualTo(BrushColor.DARK);
        assertThat(BrushColor.from(AMBER_BRUSH_COLOR)).isEqualTo(BrushColor.AMBER);
        assertThat(BrushColor.from(SKETCH_COLOR)).isEqualTo(BrushColor.ERASE);
    }
}
