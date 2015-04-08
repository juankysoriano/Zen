package zenproject.meditation.android;

import com.novoda.notils.logger.simple.Log;

import zenproject.meditation.android.sketch.painting.ink.BrushColor;
import zenproject.meditation.android.persistence.BrushOptionsPreferences;

public class ZenApplication extends AnalyticsApplication {

    @Override
    public void onCreate() {
        super.onCreate();

        ContextRetriever.INSTANCE.inject(this);

        Log.setShowLogs(BuildConfig.DEBUG);
    }

    @Override
    public void onTerminate() {
        ensureThatNextSessionWillHaveAVisibleColor();
    }

    private void ensureThatNextSessionWillHaveAVisibleColor() {
        BrushOptionsPreferences brushOptionsPreferences = BrushOptionsPreferences.newInstance();
        if (brushOptionsPreferences.getBrushColor() == BrushColor.ERASE) {
            brushOptionsPreferences.applyBrushColor(BrushColor.DARK);
        }
    }
}
