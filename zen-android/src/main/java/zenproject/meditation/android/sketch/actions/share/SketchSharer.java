package zenproject.meditation.android.sketch.actions.share;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;

import com.juankysoriano.rainbow.core.drawing.RainbowDrawer;

import java.io.File;

import zenproject.meditation.android.ContextRetriever;
import zenproject.meditation.android.R;
import zenproject.meditation.android.SketchRetriever;
import zenproject.meditation.android.analytics.AnalyticsTracker;

public class SketchSharer {
    private static final String INTENT_TYPE = "image/*";

    private final ScreenshotTaker screenshotTaker;

    public static SketchSharer newInstance() {
        Context context = ContextRetriever.INSTANCE.getApplicationContext();
        RainbowDrawer rainbowDrawer = SketchRetriever.INSTANCE.getZenSketch().getRainbowDrawer();
        File saveFolder = new File(context.getCacheDir(), "images");
        String authority = context.getString(R.string.file_provider);
        ScreenshotTaker screenshotTaker = new ScreenshotTaker(context, rainbowDrawer, saveFolder, authority);
        return new SketchSharer(screenshotTaker);
    }

    SketchSharer(ScreenshotTaker screenshotTaker) {
        this.screenshotTaker = screenshotTaker;
    }

    public void shareSketch() {
        Uri shareUri = screenshotTaker.takeScreenshot();
        if (shareUri == null) {
            return;
        }

        Resources resources = ContextRetriever.INSTANCE.getResources();
        Intent intent = new Intent(Intent.ACTION_SEND)
                .setType(INTENT_TYPE)
                .putExtra(Intent.EXTRA_SUBJECT, resources.getString(R.string.share_subjet))
                .putExtra(Intent.EXTRA_TEXT, resources.getString(R.string.share_text))
                .putExtra(Intent.EXTRA_STREAM, shareUri);

        ContextRetriever.INSTANCE.getActivity().startActivity(intent);
        AnalyticsTracker.INSTANCE.trackShare();
    }
}

