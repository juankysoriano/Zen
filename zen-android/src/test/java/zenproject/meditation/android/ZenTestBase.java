package zenproject.meditation.android;

import com.juankysoriano.rainbow.core.drawing.RainbowDrawer;

import org.mockito.Mockito;
import org.robolectric.RuntimeEnvironment;

import zenproject.meditation.android.sketch.ZenSketch;

public class ZenTestBase {
    static {
        ContextRetriever.INSTANCE.inject(RuntimeEnvironment.application);
        ZenSketch mockSketch = Mockito.mock(ZenSketch.class);
        RainbowDrawer mockDrawer = Mockito.mock(RainbowDrawer.class);
        Mockito.when(mockSketch.getRainbowDrawer()).thenReturn(mockDrawer);
        SketchRetriever.INSTANCE.inject(mockSketch);
    }
}
