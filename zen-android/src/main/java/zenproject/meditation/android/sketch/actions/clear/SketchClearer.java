package zenproject.meditation.android.sketch.actions.clear;

import zenproject.meditation.android.sketch.ZenSketch;

public class SketchClearer {
    private final ZenSketch zenSketch;

    protected SketchClearer(ZenSketch zenSketch) {
        this.zenSketch = zenSketch;
    }

    public static SketchClearer newInstance(ZenSketch zenSketch) {
        return new SketchClearer(zenSketch);
    }

    public void clearSketch() {
        zenSketch.clear();
    }
}
