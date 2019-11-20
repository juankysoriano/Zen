package zenproject.meditation.android.analytics;

import zenproject.meditation.android.sketch.painting.flowers.Flower;
import zenproject.meditation.android.sketch.painting.ink.BrushColor;

public interface ZenAnalytics {

    void trackBrushColor(BrushColor color);

    void trackBrushSize(int size);

    void trackFlower(Flower flower);

    void trackShare();

    void trackClearSketch();
}
