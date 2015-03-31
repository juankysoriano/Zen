package zenproject.meditation.android;

import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

import zenproject.meditation.android.activities.ZenActivity;
import zenproject.meditation.android.preferences.BrushColor;
import zenproject.meditation.android.preferences.Flower;

public enum AnalyticsTracker {
    INSTANCE;

    private Tracker tracker;

    AnalyticsTracker() {
    }

    private Tracker retrieveTracker() {
        if (tracker == null) {
            GoogleAnalytics analytics = GoogleAnalytics.getInstance(ContextRetriever.INSTANCE.getCurrentContext());
            tracker = analytics.newTracker(R.xml.zen_tracker);
        }
        return tracker;
    }

    public void trackDialogOpened(String dialogTag) {
        Tracker tracker = retrieveTracker();
        tracker.setScreenName(dialogTag);
        tracker.send(new HitBuilders.ScreenViewBuilder().build());
    }

    public void trackBrush(BrushColor color, int size) {
        Tracker tracker = retrieveTracker();
        tracker.send(new HitBuilders.EventBuilder()
                .setCategory("Brush")
                .setAction("Color changed")
                .setLabel(color.name())
                .setValue(1)
                .build());

        tracker.send(new HitBuilders.EventBuilder()
                .setCategory("Brush")
                .setAction("Size changed")
                .setLabel("Brush size " + size + "%")
                .set("brushSize", String.valueOf(size))
                .build());
    }

    public void trackFlower(Flower flower) {
        Tracker tracker = retrieveTracker();
        tracker.send(new HitBuilders.EventBuilder()
                .setCategory("Flower")
                .setAction("Flower selected")
                .setLabel(flower.name())
                .setValue(1)
                .build());
    }

    public void trackMusic() {

    }

    public void trackScreenshot() {
        Tracker tracker = retrieveTracker();
        tracker.send(new HitBuilders.EventBuilder()
                .setCategory("Sketch")
                .setAction("Screenshot")
                .setValue(1)
                .build());
    }

    public void trackShare() {
        Tracker tracker = retrieveTracker();
        tracker.send(new HitBuilders.EventBuilder()
                .setCategory("Sketch")
                .setAction("Share")
                .setValue(1)
                .build());
    }

    public void trackClearSketch() {
        Tracker tracker = retrieveTracker();
        tracker.send(new HitBuilders.EventBuilder()
                .setCategory("Sketch")
                .setAction("Cleared")
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
}

