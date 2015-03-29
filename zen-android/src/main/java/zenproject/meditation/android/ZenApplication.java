package zenproject.meditation.android;

import android.app.Application;

import com.novoda.notils.logger.simple.Log;

import zenproject.meditation.android.preferences.BrushColor;
import zenproject.meditation.android.preferences.BrushOptionsPreferences;

public class ZenApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        ContextRetriever.INSTANCE.inject(getApplicationContext());
        BrushOptionsPreferences.newInstance().applyBrushColor(BrushColor.ERASE.toAndroidColor());
        Log.setShowLogs(BuildConfig.DEBUG);
    }
}
