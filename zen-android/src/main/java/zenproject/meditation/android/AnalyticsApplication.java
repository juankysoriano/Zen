package zenproject.meditation.android;

import android.app.Application;

import com.crashlytics.android.Crashlytics;

import io.fabric.sdk.android.Fabric;

public class AnalyticsApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        if (doAnalytics()) {
            Fabric.with(this, new Crashlytics());
        }
    }

    private boolean doAnalytics() {
        return !getResources().getBoolean(R.bool.analyticsDisabled);
    }
}
