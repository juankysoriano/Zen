package zenproject.meditation.android;

import android.app.Activity;
import android.app.Application;

import org.fest.assertions.api.Assertions;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.verify;

public class ContextRetrieverTest extends ZenTestBase {

    @Mock
    private Application application;
    @Mock
    private Activity activity;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testThatWhenAnApplicationContextIsInjectedThenApplicationContextIsRetrieved() {
        ContextRetriever.INSTANCE.inject(application);

        ContextRetriever.INSTANCE.getApplicationContext();

        verify(application).getApplicationContext();
    }

    @Test
    public void testThatWhenAnActivityIsInjectedThenTheSameActivityIsRetrieved() {
        ContextRetriever.INSTANCE.inject(activity);

        Assertions.assertThat(ContextRetriever.INSTANCE.getActivity()).isEqualTo(activity);
    }

    @Test
    public void testThatGetResourcesRetrievesFromApplicationContext() {
        ContextRetriever.INSTANCE.inject(application);
        ContextRetriever.INSTANCE.getResources();

        verify(application).getResources();
    }
}
