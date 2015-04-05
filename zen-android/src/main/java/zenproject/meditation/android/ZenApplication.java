package zenproject.meditation.android;

import com.novoda.notils.logger.simple.Log;

import zenproject.meditation.android.sketch.painting.ink.BrushColor;
import zenproject.meditation.android.persistence.BrushOptionsPreferences;

public class ZenApplication extends AnalyticsApplication {

    @Override
    public void onCreate() {
        super.onCreate();

        ContextRetriever.INSTANCE.inject(getApplicationContext());

        selectDarkColorIfCurrentIsErase();

        Log.setShowLogs(BuildConfig.DEBUG);
    }

    private void selectDarkColorIfCurrentIsErase() {
        BrushOptionsPreferences brushOptionsPreferences = BrushOptionsPreferences.newInstance();
        if (brushOptionsPreferences.getBrushColor() == BrushColor.ERASE) {
            brushOptionsPreferences.applyBrushColor(BrushColor.DARK);
        }
    }
}
