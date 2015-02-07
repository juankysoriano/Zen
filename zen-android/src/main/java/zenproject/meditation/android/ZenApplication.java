package zenproject.meditation.android;

import android.app.Application;

import com.novoda.notils.logger.simple.Log;

public class ZenApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        ContextRetriever.INSTANCE.inject(getApplicationContext());
        Log.setShowLogs(BuildConfig.DEBUG);
    }
}
