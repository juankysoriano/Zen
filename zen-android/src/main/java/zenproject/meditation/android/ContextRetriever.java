package zenproject.meditation.android;

import android.app.Activity;
import android.app.Application;
import android.content.ContentResolver;
import android.content.Context;
import android.content.res.Resources;

public enum ContextRetriever {
    INSTANCE;

    private Application application;
    private Activity activity;

    public void inject(Application application) {
        this.application = application;
    }

    public void inject(Activity activity) {
        this.activity = activity;
    }

    public Context getApplicationContext() {
        return application.getApplicationContext();
    }

    public Activity getActivity() {
        return activity;
    }

    public Resources getResources() {
        return application.getResources();
    }

    public ContentResolver getContentResolver() {
        return application.getContentResolver();
    }

}
