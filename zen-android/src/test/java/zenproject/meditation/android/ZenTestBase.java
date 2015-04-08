package zenproject.meditation.android;

import org.robolectric.Robolectric;

public class ZenTestBase {
    static {
        ContextRetriever.INSTANCE.inject(Robolectric.application);
    }
}
