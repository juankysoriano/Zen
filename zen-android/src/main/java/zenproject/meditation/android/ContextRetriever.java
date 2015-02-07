package zenproject.meditation.android;

import android.content.Context;

public enum ContextRetriever {
    INSTANCE;

    private Context context;

    public void inject(Context context) {
        this.context = context;
    }

    public Context getContext() {
        return context;
    }
}
