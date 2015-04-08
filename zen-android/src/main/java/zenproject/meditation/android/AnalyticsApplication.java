package zenproject.meditation.android;

import android.app.Application;

import com.crashlytics.android.Crashlytics;
import com.google.android.gms.analytics.GoogleAnalytics;

import io.fabric.sdk.android.Fabric;
import zenproject.meditation.android.analytics.AnalyticsTracker;

public class AnalyticsApplication extends Application {

    @Override
    public void onCreate() {
        Crashlytics crashlytics = new Crashlytics.Builder().disabled(isAnalyticsDisabled()).build();
        Fabric.with(this, crashlytics);

        GoogleAnalytics analytics = GoogleAnalytics.getInstance(getApplicationContext());
        analytics.setAppOptOut(isAnalyticsDisabled());
        AnalyticsTracker.INSTANCE.inject(analytics.newTracker(R.xml.zen_tracker));
    }

    private boolean isAnalyticsDisabled() {
        return getResources().getBoolean(R.bool.analyticsDisabled);
    }
}
