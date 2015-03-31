package zenproject.meditation.android;

import com.novoda.notils.logger.simple.Log;

import zenproject.meditation.android.preferences.BrushColor;
import zenproject.meditation.android.preferences.BrushOptionsPreferences;

public class ZenApplication extends CrashLyticsApplication {

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
