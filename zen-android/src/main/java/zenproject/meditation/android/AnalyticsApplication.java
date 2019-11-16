package zenproject.meditation.android;

import android.app.Application;

import com.google.android.gms.analytics.GoogleAnalytics;

import zenproject.meditation.android.analytics.AnalyticsTracker;

public class AnalyticsApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        GoogleAnalytics analytics = GoogleAnalytics.getInstance(getApplicationContext());
        analytics.setAppOptOut(isAnalyticsDisabled());
        analytics.setDryRun(isAnalyticsDisabled());
        AnalyticsTracker.INSTANCE.inject(analytics.newTracker(R.xml.zen_tracker));
    }

    private boolean isAnalyticsDisabled() {
        return BuildConfig.ANALYTICS_DISABLED;
    }
}
