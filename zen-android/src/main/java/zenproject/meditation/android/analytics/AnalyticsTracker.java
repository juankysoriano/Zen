package zenproject.meditation.android.analytics;

import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

import zenproject.meditation.android.ContextRetriever;
import zenproject.meditation.android.R;
import zenproject.meditation.android.activities.ZenActivity;
import zenproject.meditation.android.sketch.painting.ink.BrushColor;
import zenproject.meditation.android.sketch.painting.flowers.Flower;

public enum AnalyticsTracker {
    INSTANCE;

    private Tracker tracker;

    AnalyticsTracker() {
    }

    private Tracker retrieveTracker() {
        if (!hasTracker()) {
            GoogleAnalytics analytics = GoogleAnalytics.getInstance(ContextRetriever.INSTANCE.getCurrentContext());
            tracker = analytics.newTracker(R.xml.zen_tracker);
        }
        return tracker;
    }

    private boolean hasTracker() {
        return tracker != null;
    }

    public void trackDialogOpened(String dialogTag) {
        Tracker dialogTracker = hasTracker() ? tracker : retrieveTracker();
        dialogTracker.setScreenName(dialogTag);
        dialogTracker.send(new HitBuilders.ScreenViewBuilder().build());
    }

    public void trackBrush(BrushColor color, int size) {
        Tracker brushTracker = hasTracker() ? tracker : retrieveTracker();
        brushTracker.send(new HitBuilders.EventBuilder()
                .setCategory(BrushTracking.BRUSH)
                .setAction(BrushTracking.COLOR_SELECTED)
                .setLabel(color.name())
                .setValue(1)
                .build());

        brushTracker.send(new HitBuilders.EventBuilder()
                .setCategory(BrushTracking.BRUSH)
                .setAction(BrushTracking.SIZE_SELECTED)
                .setLabel("Brush size " + size + "%")
                .set(BrushTracking.SIZE_VAR, String.valueOf(size))
                .build());
    }

    public void trackFlower(Flower flower) {
        Tracker flowerTracker = hasTracker() ? tracker : retrieveTracker();
        flowerTracker.send(new HitBuilders.EventBuilder()
                .setCategory(FlowerTracking.FLOWER)
                .setAction(FlowerTracking.FLOWER_SELECTED)
                .setLabel(flower.name())
                .setValue(1)
                .build());
    }

    public void trackMusic() {

    }

    public void trackScreenshot() {
        Tracker screenshotTracker = hasTracker() ? tracker : retrieveTracker();
        screenshotTracker.send(new HitBuilders.EventBuilder()
                .setCategory(SketchTracking.SKETCH)
                .setAction(SketchTracking.SCREENSHOT)
                .setValue(1)
                .build());
    }

    public void trackShare() {
        Tracker shareTracker = hasTracker() ? tracker : retrieveTracker();
        shareTracker.send(new HitBuilders.EventBuilder()
                .setCategory(SketchTracking.SKETCH)
                .setAction(SketchTracking.SHARED)
                .setValue(1)
                .build());
    }

    public void trackClearSketch() {
        Tracker clearTracker = hasTracker() ? tracker : retrieveTracker();
        clearTracker.send(new HitBuilders.EventBuilder()
                .setCategory(SketchTracking.SKETCH)
                .setAction(SketchTracking.CLEARED)
                .setValue(1)
                .build());
    }

    public void trackActivityStart(ZenActivity zenActivity) {
        GoogleAnalytics.getInstance(zenActivity).reportActivityStart(zenActivity);
        dispatch(zenActivity);
    }

    public void trackActivityStop(ZenActivity zenActivity) {
        GoogleAnalytics.getInstance(zenActivity).reportActivityStop(zenActivity);
        dispatch(zenActivity);
    }

    private void dispatch(ZenActivity zenActivity) {
        GoogleAnalytics.getInstance(zenActivity).dispatchLocalHits();
    }

    private static class SketchTracking {
        private static final String SKETCH = "Sketch";
        private static final String CLEARED = "Cleared";
        private static final String SHARED = "Shared";
        private static final String SCREENSHOT = "Screenshot";
    }

    private static class BrushTracking {
        private static final String BRUSH = "Brush";
        private static final String COLOR_SELECTED = "Color selected";
        private static final String SIZE_SELECTED = "Size selected";
        private static final String SIZE_VAR = "size_value";
    }

    private static class FlowerTracking {
        private static final String FLOWER = "Flower";
        private static final String FLOWER_SELECTED = "Flower selected";
    }
}

