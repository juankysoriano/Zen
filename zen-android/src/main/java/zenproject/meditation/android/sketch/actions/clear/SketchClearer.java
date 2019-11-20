package zenproject.meditation.android.sketch.actions.clear;

import zenproject.meditation.android.SketchRetriever;
import zenproject.meditation.android.analytics.AnalyticsTracker;
import zenproject.meditation.android.sketch.ZenSketch;
import zenproject.meditation.android.ui.sketch.ZenSketchView;
import zenproject.meditation.android.ui.sketch.clear.SketchClearListener;

public class SketchClearer implements SketchClearListener {
    private final ZenSketch zenSketch;
    private final ZenSketchView zenSketchView;

    SketchClearer(ZenSketch zenSketch, ZenSketchView zenSketchView) {
        this.zenSketch = zenSketch;
        this.zenSketchView = zenSketchView;
    }

    public static SketchClearer newInstance(ZenSketchView zenSketchView) {
        return new SketchClearer(SketchRetriever.INSTANCE.getZenSketch(), zenSketchView);
    }

    public void clearSketch() {
        zenSketchView.clear();
        AnalyticsTracker.INSTANCE.trackClearSketch();
    }

    @Override
    public void onSketchCleared() {
        if (zenSketch.isSetup()) {
            zenSketch.clear();
        }
    }
}
