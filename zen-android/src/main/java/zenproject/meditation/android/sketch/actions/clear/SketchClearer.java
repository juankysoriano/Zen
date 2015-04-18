package zenproject.meditation.android.sketch.actions.clear;

import zenproject.meditation.android.SketchRetriever;
import zenproject.meditation.android.analytics.AnalyticsTracker;
import zenproject.meditation.android.sketch.ZenSketch;

public class SketchClearer {
    private final ZenSketch zenSketch;

    protected SketchClearer(ZenSketch zenSketch) {
        this.zenSketch = zenSketch;
    }

    public static SketchClearer newInstance() {
        return new SketchClearer(SketchRetriever.INSTANCE.getZenSketch());
    }

    public void clearSketch() {
        zenSketch.clear();
        AnalyticsTracker.INSTANCE.trackClearSketch();
    }
}
