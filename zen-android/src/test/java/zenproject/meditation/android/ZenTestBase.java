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
    protected static final float INK_DROP_MIN_RADIUS = 0.75f;
    protected static final float INK_DROP_MAX_RADIUS = 120f;
    protected static final float INK_DROP_STEP = 1f;
    protected static final float INK_VELOCITY_THRESHOLD = 16f;
    protected static final float BRANCH_MIN_RADIUS = 0.25f;
    protected static final float BRANCH_MIN_BLOOM_RADIUS = 1.5f;
    private static final float BRANCH_DEFAULT_RADIUS = 3f;

    protected static final int DARK_BRUSH_COLOR = Integer.parseInt("212121", 16);
    protected static final int AMBER_BRUSH_COLOR = Integer.parseInt("FFC107", 16);
    protected static final int BRUSH_PRIMARY_COLOR = Integer.parseInt("3F51B5", 16);
    protected static final int BRUSH_ACCENT_COLOR = Integer.parseInt("FF5252", 16);
    protected static final int SKETCH_COLOR = Integer.parseInt("FFFDE7", 16);

    static {
        Resources mockResources = mock(Resources.class);
        when(mockResources.getDimension(R.dimen.ink_drop_min_radius)).thenReturn(INK_DROP_MIN_RADIUS);
        when(mockResources.getDimension(R.dimen.ink_drop_max_radius)).thenReturn(INK_DROP_MAX_RADIUS);
        when(mockResources.getDimension(R.dimen.ink_velocity_threshold)).thenReturn(INK_VELOCITY_THRESHOLD);
        when(mockResources.getDimension(R.dimen.ink_drop_step)).thenReturn(INK_DROP_STEP);
        when(mockResources.getDimension(R.dimen.branch_min_radius)).thenReturn(BRANCH_MIN_RADIUS);
        when(mockResources.getDimension(R.dimen.branch_min_bloom_radius)).thenReturn(BRANCH_MIN_BLOOM_RADIUS);
        when(mockResources.getDimension(R.dimen.branch_default_radius)).thenReturn(BRANCH_DEFAULT_RADIUS);
        when(mockResources.getColor(R.color.dark_brush)).thenReturn(DARK_BRUSH_COLOR);
        when(mockResources.getColor(R.color.amber_brush)).thenReturn(AMBER_BRUSH_COLOR);
        when(mockResources.getColor(R.color.colorPrimary)).thenReturn(BRUSH_PRIMARY_COLOR);
        when(mockResources.getColor(R.color.colorAccent)).thenReturn(BRUSH_ACCENT_COLOR);
        when(mockResources.getColor(R.color.colorSketch)).thenReturn(SKETCH_COLOR);
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
    }
}
