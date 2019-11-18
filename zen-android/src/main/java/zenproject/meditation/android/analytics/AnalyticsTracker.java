package zenproject.meditation.android.analytics;

import android.os.Bundle;

import com.google.firebase.analytics.FirebaseAnalytics;

import zenproject.meditation.android.sketch.painting.flowers.Flower;
import zenproject.meditation.android.sketch.painting.ink.BrushColor;

import static com.google.firebase.analytics.FirebaseAnalytics.Event;
import static com.google.firebase.analytics.FirebaseAnalytics.Param;

/**
 * TODO having AnalyticsTracker implementing ZenAnalytics interface then it is mockable so we should pass this as collaborator and test interactions.
 */
public enum AnalyticsTracker implements ZenAnalytics {
    INSTANCE;

    public static final String VALUE = "value";
    private FirebaseAnalytics analytics;

    public void inject(FirebaseAnalytics analytics) {
        this.analytics = analytics;
    }

    @Override
    public void trackDialogOpened(String dialogTag) {
        if (analytics == null) return;

        Bundle bundle = new Bundle();
        bundle.putString(Param.ITEM_CATEGORY, "screen");
        bundle.putString(Param.ITEM_NAME, "dialog");
        analytics.logEvent(Event.VIEW_ITEM, bundle);
    }

    @Override
    public void trackBrush(BrushColor color, int size) {
        if (analytics == null) return;

        Bundle colorBundle = new Bundle();
        colorBundle.putString(Param.ITEM_CATEGORY, BrushTracking.BRUSH);
        colorBundle.putString(Param.ITEM_NAME, BrushTracking.COLOR);
        colorBundle.putString(VALUE, color.name());
        analytics.logEvent("color_changed_event", colorBundle);

        Bundle sizeBundle = new Bundle();
        sizeBundle.putString(Param.ITEM_CATEGORY, BrushTracking.BRUSH);
        sizeBundle.putString(Param.ITEM_NAME, BrushTracking.SIZE);
        sizeBundle.putInt(VALUE, size);
        analytics.logEvent("size_changed_event", sizeBundle);
    }

    @Override
    public void trackFlower(Flower flower) {
        if (analytics == null) return;

        Bundle bundle = new Bundle();
        bundle.putString(Param.ITEM_CATEGORY, FlowerTracking.FLOWER);
        bundle.putString(Param.ITEM_NAME, FlowerTracking.SPECIES);
        bundle.putString(VALUE, flower.name());
        analytics.logEvent("species_changed_event", bundle);
    }

    @Override
    public void trackShare() {
        if (analytics == null) return;

        Bundle bundle = new Bundle();
        bundle.putString(Param.ITEM_CATEGORY, SketchTracking.SKETCH);
        bundle.putString(Param.ITEM_NAME, SketchTracking.SHARED);
        analytics.logEvent("share_event", bundle);
    }

    @Override
    public void trackClearSketch() {
        if (analytics == null) return;

        Bundle bundle = new Bundle();
        bundle.putString(Param.ITEM_CATEGORY, SketchTracking.SKETCH);
        bundle.putString(Param.ITEM_NAME, SketchTracking.CLEARED);
        analytics.logEvent("clear_event", bundle);
    }

    @SuppressWarnings("PMD.UnusedModifier")
    protected static final class SketchTracking {
        private static final String SKETCH = "Sketch";
        private static final String CLEARED = "Cleared";
        private static final String SHARED = "Shared";

        private SketchTracking() {
            //no-op
        }
    }

    @SuppressWarnings("PMD.UnusedModifier")
    protected static final class BrushTracking {
        private static final String BRUSH = "Brush";
        private static final String COLOR = "Color";
        private static final String SIZE = "Size";

        private BrushTracking() {
            //no-op
        }
    }

    @SuppressWarnings("PMD.UnusedModifier")
    protected static final class FlowerTracking {
        private static final String FLOWER = "Flower";
        private static final String SPECIES = "Species";

        private FlowerTracking() {
            //no-op
        }
    }
}

