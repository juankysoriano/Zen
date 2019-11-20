package zenproject.meditation.android.analytics;

import android.os.Bundle;

import com.google.firebase.analytics.FirebaseAnalytics;

import zenproject.meditation.android.sketch.painting.flowers.Flower;
import zenproject.meditation.android.sketch.painting.ink.BrushColor;

import static com.google.firebase.analytics.FirebaseAnalytics.Param;

/**
 * TODO having AnalyticsTracker implementing ZenAnalytics interface then it is mockable so we should pass this as collaborator and test interactions.
 */
public enum AnalyticsTracker implements ZenAnalytics {
    INSTANCE;

    public static final String PARAM_NAME = "name";
    private FirebaseAnalytics analytics;

    public void inject(FirebaseAnalytics analytics) {
        this.analytics = analytics;
    }

    @Override
    public void trackBrushColor(BrushColor color) {
        if (analytics == null) {
            return;
        }

        Bundle colorBundle = new Bundle();
        colorBundle.putString(Param.ITEM_CATEGORY, BrushTracking.BRUSH);
        colorBundle.putString(Param.ITEM_NAME, BrushTracking.COLOR);
        colorBundle.putString(PARAM_NAME, color.name());
        analytics.logEvent("color_changed_event", colorBundle);
    }

    @Override
    public void trackBrushSize(int percentage) {
        if (analytics == null) {
            return;
        }

        Bundle sizeBundle = new Bundle();
        sizeBundle.putString(Param.ITEM_CATEGORY, BrushTracking.BRUSH);
        sizeBundle.putString(Param.ITEM_NAME, BrushTracking.SIZE);
        sizeBundle.putString(PARAM_NAME, Size.from(percentage).value);
        analytics.logEvent("size_changed_event", sizeBundle);
    }

    @Override
    public void trackFlower(Flower flower) {
        if (analytics == null) {
            return;
        }

        Bundle bundle = new Bundle();
        bundle.putString(Param.ITEM_CATEGORY, FlowerTracking.FLOWER);
        bundle.putString(Param.ITEM_NAME, FlowerTracking.SPECIES);
        bundle.putString("name", flower.name());
        analytics.logEvent("species_changed_event", bundle);
    }

    @Override
    public void trackShare() {
        if (analytics == null) {
            return;
        }

        Bundle bundle = new Bundle();
        bundle.putString(Param.ITEM_CATEGORY, SketchTracking.SKETCH);
        bundle.putString(Param.ITEM_NAME, SketchTracking.SHARED);
        analytics.logEvent("share_event", bundle);
    }

    @Override
    public void trackClearSketch() {
        if (analytics == null) {
            return;
        }

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

enum Size {
    SMALL("small"),
    MID_SMALL("mid_small"),
    MID("mid"),
    MID_LARGE("mid_large"),
    LARGE("large");

    final String value;

    Size(String value) {
        this.value = value;
    }

    static Size from(int percentage) {
        if (0 <= percentage && percentage < 20) {
            return SMALL;
        } else if (20 <= percentage && percentage < 40) {
            return MID_SMALL;
        } else if (40 <= percentage && percentage < 60) {
            return MID;
        } else if (60 <= percentage && percentage < 80) {
            return MID_LARGE;
        } else {
            return LARGE;
        }
    }
}

