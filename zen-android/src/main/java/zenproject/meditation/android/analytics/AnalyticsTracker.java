package zenproject.meditation.android.analytics;

import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

import zenproject.meditation.android.activities.ZenActivity;
import zenproject.meditation.android.sketch.painting.flowers.Flower;
import zenproject.meditation.android.sketch.painting.ink.BrushColor;

/**
 * TODO having AnalyticsTracker implementing ZenAnalytics interface then it is mockable so we should pass this as collaborator and test interactions.
 */
public enum AnalyticsTracker implements ZenAnalytics {
    INSTANCE;

    private Tracker tracker;

    public void inject(Tracker tracker) {
        this.tracker = tracker;
    }

    @Override
    public void trackDialogOpened(String dialogTag) {
        tracker.setScreenName(dialogTag);
        tracker.send(new HitBuilders.ScreenViewBuilder().build());
    }

    @Override
    public void trackBrush(BrushColor color, int size) {
        tracker.send(new HitBuilders.EventBuilder()
                .setCategory(BrushTracking.BRUSH)
                .setAction(BrushTracking.COLOR_SELECTED)
                .setLabel(color.name())
                .setValue(1)
                .build());

        tracker.send(new HitBuilders.EventBuilder()
                .setCategory(BrushTracking.BRUSH)
                .setAction(BrushTracking.SIZE_SELECTED)
                .setLabel("Brush size " + size + "%")
                .set(BrushTracking.SIZE_VAR, String.valueOf(size))
                .build());
    }

    @Override
    public void trackFlower(Flower flower) {
        tracker.send(new HitBuilders.EventBuilder()
                .setCategory(FlowerTracking.FLOWER)
                .setAction(FlowerTracking.FLOWER_SELECTED)
                .setLabel(flower.name())
                .setValue(1)
                .build());
    }

    @Override
    public void trackMusic() {
        //no-op
    }

    @Override
    public void trackScreenshot() {
        tracker.send(new HitBuilders.EventBuilder()
                .setCategory(SketchTracking.SKETCH)
                .setAction(SketchTracking.SCREENSHOT)
                .setValue(1)
                .build());
    }

    @Override
    public void trackShare() {
        tracker.send(new HitBuilders.EventBuilder()
                .setCategory(SketchTracking.SKETCH)
                .setAction(SketchTracking.SHARED)
                .setValue(1)
                .build());
    }

    @Override
    public void trackClearSketch() {
        tracker.send(new HitBuilders.EventBuilder()
                .setCategory(SketchTracking.SKETCH)
                .setAction(SketchTracking.CLEARED)
                .setValue(1)
                .build());
    }

    @Override
    public void trackActivityStart(ZenActivity zenActivity) {
        GoogleAnalytics.getInstance(zenActivity).reportActivityStart(zenActivity);
        dispatch(zenActivity);
    }

    @Override
    public void trackActivityStop(ZenActivity zenActivity) {
        GoogleAnalytics.getInstance(zenActivity).reportActivityStop(zenActivity);
        dispatch(zenActivity);
    }

    private void dispatch(ZenActivity zenActivity) {
        GoogleAnalytics.getInstance(zenActivity).dispatchLocalHits();
    }

    protected class SketchTracking {
        protected static final String SCREENSHOT = "Screenshot";
        protected static final String SKETCH = "Sketch";
        protected static final String CLEARED = "Cleared";
        protected static final String SHARED = "Shared";

        private SketchTracking() {
            //no-op
        }
    }

    protected class BrushTracking {
        protected static final String BRUSH = "Brush";
        protected static final String COLOR_SELECTED = "Color selected";
        protected static final String SIZE_SELECTED = "Size selected";
        protected static final String SIZE_VAR = "size_value";

        private BrushTracking() {
            //no-op
        }
    }

    protected class FlowerTracking {
        protected static final String FLOWER = "Flower";
        protected static final String FLOWER_SELECTED = "Flower selected";

        private FlowerTracking() {
            //no-op
        }
    }
}

