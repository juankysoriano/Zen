package zenproject.meditation.android;

import android.app.Application;

import com.google.firebase.analytics.FirebaseAnalytics;

import zenproject.meditation.android.analytics.AnalyticsTracker;

public class AnalyticsApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        AnalyticsTracker.INSTANCE.inject(FirebaseAnalytics.getInstance(this));
    }
}
