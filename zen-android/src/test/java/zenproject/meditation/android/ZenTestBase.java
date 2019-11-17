package zenproject.meditation.android;

import android.app.Application;
import android.content.SharedPreferences;
import android.content.res.Resources;

import com.google.android.gms.analytics.Tracker;
import com.juankysoriano.rainbow.core.drawing.RainbowDrawer;

import zenproject.meditation.android.analytics.AnalyticsTracker;
import zenproject.meditation.android.sketch.ZenSketch;

import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ZenTestBase {
    static {
        Resources mockResources = mock(Resources.class);
        SharedPreferences mockSharedPreferences = mock(SharedPreferences.class);
        Application mockApplication = mock(Application.class);
        when(mockApplication.getResources()).thenReturn(mockResources);
        when(mockApplication.getApplicationContext()).thenReturn(mockApplication);
        when(mockApplication.getSharedPreferences(anyString(), anyInt())).thenReturn(mockSharedPreferences);
        ContextRetriever.INSTANCE.inject(mockApplication);
        ZenSketch mockSketch = mock(ZenSketch.class);
        RainbowDrawer mockDrawer = mock(RainbowDrawer.class);
        when(mockSketch.getRainbowDrawer()).thenReturn(mockDrawer);
        SketchRetriever.INSTANCE.inject(mockSketch);
        Tracker mockTracker = mock(Tracker.class);
        AnalyticsTracker.INSTANCE.inject(mockTracker);

        when(mockResources.getDimension(R.dimen.ink_drop_min_radius)).thenReturn(0.75f);
        when(mockResources.getDimension(R.dimen.ink_drop_max_radius)).thenReturn(120f);
        when(mockResources.getDimension(R.dimen.ink_velocity_threshold)).thenReturn(16f);
        when(mockResources.getDimension(R.dimen.ink_drop_step)).thenReturn(1f);
        when(mockResources.getDimension(R.dimen.branch_min_radius)).thenReturn(1.25f);
        when(mockResources.getDimension(R.dimen.branch_min_bloom_radius)).thenReturn(1.5f);
        when(mockResources.getDimension(R.dimen.branch_default_radius)).thenReturn(3f);

        when(mockResources.getColor(R.color.dark_brush)).thenReturn(0);
        when(mockResources.getColor(R.color.amber_brush)).thenReturn(1);
        when(mockResources.getColor(R.color.colorPrimary)).thenReturn(2);
        when(mockResources.getColor(R.color.colorAccent)).thenReturn(3);
        when(mockResources.getColor(R.color.colorSketch)).thenReturn(4);

    }
}
