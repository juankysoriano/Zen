package zenproject.meditation.android.activities;

import android.app.Activity;
import android.os.Bundle;

import zenproject.meditation.android.ContextRetriever;
import zenproject.meditation.android.analytics.AnalyticsTracker;

public abstract class ZenActivity extends Activity {

    protected ZenActivity() {
        ContextRetriever.INSTANCE.inject(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ContextRetriever.INSTANCE.inject(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        AnalyticsTracker.INSTANCE.trackActivityStart(this);
    }

    @Override
    protected void onStop() {
        AnalyticsTracker.INSTANCE.trackActivityStop(this);
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        ContextRetriever.INSTANCE.inject((Activity) null);
        super.onDestroy();
    }
}
