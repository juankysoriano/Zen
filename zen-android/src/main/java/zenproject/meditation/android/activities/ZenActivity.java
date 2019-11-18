package zenproject.meditation.android.activities;

import android.app.Activity;
import android.os.Bundle;

import zenproject.meditation.android.ContextRetriever;

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
    protected void onDestroy() {
        ContextRetriever.INSTANCE.inject((Activity) null);
        super.onDestroy();
    }
}
