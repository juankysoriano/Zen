package zenproject.meditation.android;

import android.app.Application;

import com.novoda.notils.logger.simple.Log;

import zenproject.meditation.android.preferences.BrushOptionsPreferences;
import zenproject.meditation.android.sketch.painting.ink.BrushColor;

public class ZenApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        ContextRetriever.INSTANCE.inject(this);

        ensureThatNewSessionWillHaveAVisibleColor();

        Log.setShowLogs(BuildConfig.DEBUG);
    }

    private void ensureThatNewSessionWillHaveAVisibleColor() {
        BrushOptionsPreferences brushOptionsPreferences = BrushOptionsPreferences.newInstance();
        if (brushOptionsPreferences.getBrushColor() == BrushColor.ERASE) {
            brushOptionsPreferences.applyBrushColor(BrushColor.DARK);
        }
    }
}
