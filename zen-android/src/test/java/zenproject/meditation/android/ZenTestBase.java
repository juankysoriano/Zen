package zenproject.meditation.android;

import org.robolectric.RuntimeEnvironment;

public class ZenTestBase {
    static {
        ContextRetriever.INSTANCE.inject(RuntimeEnvironment.application);
    }
}
