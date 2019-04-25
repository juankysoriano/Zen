package zenproject.meditation.android.sketch.actions.share;

import com.juankysoriano.rainbow.core.drawing.RainbowDrawer;

import zenproject.meditation.android.ContextRetriever;
import zenproject.meditation.android.R;
import zenproject.meditation.android.SketchRetriever;

public class SketchSharer {
    private static final String INTENT_TYPE = "image/png";
    private static final String ZEN = ContextRetriever.INSTANCE.getResources().getString(R.string.app_name);
    private static final String PICTURE_TITLE = ContextRetriever.INSTANCE.getResources().getString(R.string.picture_title);
    private final RainbowDrawer rainbowDrawer;

    protected SketchSharer(RainbowDrawer rainbowDrawer) {
        this.rainbowDrawer = rainbowDrawer;
    }

    public static SketchSharer newInstance() {
        return new SketchSharer(SketchRetriever.INSTANCE.getZenSketch().getRainbowDrawer());
    }

    public void shareSketch() {
//        Resources resources = ContextRetriever.INSTANCE.getResources();
//
//        Intent intent = new Intent(Intent.ACTION_SEND)
//                .setType(INTENT_TYPE)
//                .putExtra(Intent.EXTRA_SUBJECT, resources.getString(R.string.share_subjet))
//                .putExtra(Intent.EXTRA_TEXT, resources.getString(R.string.share_text))
//                .putExtra(Intent.EXTRA_STREAM, rainbowDrawer.save(PICTURE_TITLE, ZEN));
//
//        Intent chooser = Intent.createChooser(intent, resources.getString(R.string.share_with_friends));
//
//        ContextRetriever.INSTANCE.getActivity().startActivity(chooser);
//
//        AnalyticsTracker.INSTANCE.trackShare();
    }
}
