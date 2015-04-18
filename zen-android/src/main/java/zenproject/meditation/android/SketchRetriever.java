package zenproject.meditation.android;

import zenproject.meditation.android.sketch.ZenSketch;

public enum SketchRetriever {
    INSTANCE;

    private ZenSketch zenSketch;

    public void inject(ZenSketch zenSketch) {
        this.zenSketch = zenSketch;
    }

    public ZenSketch getZenSketch() {
        return zenSketch;
    }
}
