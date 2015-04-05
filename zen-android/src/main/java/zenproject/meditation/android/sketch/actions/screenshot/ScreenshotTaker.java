package zenproject.meditation.android.sketch.actions.screenshot;

import com.novoda.notils.logger.toast.Toaster;

import zenproject.meditation.android.ContextRetriever;
import zenproject.meditation.android.R;
import zenproject.meditation.android.analytics.AnalyticsTracker;
import zenproject.meditation.android.sketch.ZenSketch;

public class ScreenshotTaker {
    private static final String ZEN = ContextRetriever.INSTANCE.getResources().getString(R.string.app_name);
    private static final String PICTURE_TITLE = ContextRetriever.INSTANCE.getResources().getString(R.string.picture_title);
    private final ZenSketch zenSketch;

    protected ScreenshotTaker(ZenSketch zenSketch) {
        this.zenSketch = zenSketch;
    }

    public static ScreenshotTaker newInstance(ZenSketch zenSketch) {
        return new ScreenshotTaker(zenSketch);
    }

    public void takeScreenshot() {
        zenSketch.getRainbowDrawer().save(ZEN, PICTURE_TITLE);
        Toaster.newInstance(ContextRetriever.INSTANCE.getCurrentContext()).popToast(R.string.save_sketch);
        AnalyticsTracker.INSTANCE.trackScreenshot();
    }
}
