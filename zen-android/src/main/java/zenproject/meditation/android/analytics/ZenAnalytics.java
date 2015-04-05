package zenproject.meditation.android.analytics;

import zenproject.meditation.android.activities.ZenActivity;
import zenproject.meditation.android.sketch.painting.flowers.Flower;
import zenproject.meditation.android.sketch.painting.ink.BrushColor;

public interface ZenAnalytics {
    void trackDialogOpened(String dialogTag);

    void trackBrush(BrushColor color, int size);

    void trackFlower(Flower flower);

    void trackMusic();

    void trackScreenshot();

    void trackShare();

    void trackClearSketch();

    void trackActivityStart(ZenActivity zenActivity);

    void trackActivityStop(ZenActivity zenActivity);
}
