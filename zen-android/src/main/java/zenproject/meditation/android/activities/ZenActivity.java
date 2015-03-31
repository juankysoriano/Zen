package zenproject.meditation.android.activities;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;

import zenproject.meditation.android.AnalyticsTracker;
import zenproject.meditation.android.ContextRetriever;

public abstract class ZenActivity extends FragmentActivity implements View.OnAttachStateChangeListener {
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
        ContextRetriever.INSTANCE.inject(getApplicationContext());
        super.onDestroy();
    }
}
