package zenproject.meditation.android.persistence;

import android.content.SharedPreferences;

import org.fest.assertions.api.Assertions;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import zenproject.meditation.android.RobolectricLauncherGradleTestRunner;
import zenproject.meditation.android.ZenTestBase;
import zenproject.meditation.android.sketch.painting.ink.BrushColor;

import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.*;

@RunWith(RobolectricLauncherGradleTestRunner.class)
public class BrushOptionsPreferencesTest extends ZenTestBase {
    private static final String PREF_NAME = "BrushPreferences";
    private static final String PREF_BRUSH_COLOR = PREF_NAME + "Color";
    private static final String PREF_BRUSH_SIZE = PREF_NAME + "Size";
    private static final int ANY_PERCENTAGE = 10;
    private static final BrushColor ANY_COLOR = BrushColor.ACCENT;
    @Mock
    private SharedPreferences sharedPreferences;
    @Mock
    private SharedPreferences.Editor editor;

    private BrushOptionsPreferences brushOptionsPreferences;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        when(sharedPreferences.edit()).thenReturn(editor);
        when(editor.putInt(anyString(), anyInt())).thenReturn(editor);

        brushOptionsPreferences = new BrushOptionsPreferences(sharedPreferences);
    }

    @Test
    public void testThatApplyBrushSizePutsValuesInEditor() {
        brushOptionsPreferences.applyBrushSize(ANY_PERCENTAGE);

        verify(editor).putInt(PREF_BRUSH_SIZE, ANY_PERCENTAGE);
    }

    @Test
    public void testThatGetBrushSizeReturnsGetsValueFromSharedPreferences() {
        givenThatSizeWasApplied();

        Assertions.assertThat(brushOptionsPreferences.getBrushSize()).isEqualTo(ANY_PERCENTAGE);
    }

    private void givenThatSizeWasApplied() {
        when(sharedPreferences.getInt(eq(PREF_BRUSH_SIZE), anyInt())).thenReturn(ANY_PERCENTAGE);
    }

    @Test
    public void testThatApplyBrushSizeAppliesChanges() {
        brushOptionsPreferences.applyBrushSize(ANY_PERCENTAGE);

        verify(editor).apply();
    }

    @Test
    public void testThatGetBrushColorReturnsGetsValueFromSharedPreferences() {
        givenThatColorWasApplied();

        Assertions.assertThat(brushOptionsPreferences.getBrushColor()).isEqualTo(ANY_COLOR);
    }

    @Test
    public void testThatGetBranchColorReturnsGetsValueFromSharedPreferences() {
        brushOptionsPreferences.getBranchColor();

        verify(sharedPreferences).getInt(eq(PREF_BRUSH_COLOR), anyInt());
    }

    private void givenThatColorWasApplied() {
        when(sharedPreferences.getInt(eq(PREF_BRUSH_COLOR), anyInt())).thenReturn(BrushColor.ACCENT.toAndroidColor());
    }

    @Test
    public void testThatApplyBrushColorPutsValuesInEditor() {
        brushOptionsPreferences.applyBrushColor(ANY_COLOR);

        verify(editor).putInt(PREF_BRUSH_COLOR, ANY_COLOR.toAndroidColor());
    }

    @Test
    public void testThatApplyBrushColorAppliesChanges() {
        brushOptionsPreferences.applyBrushSize(ANY_PERCENTAGE);

        verify(editor).apply();
    }

    @Test
    public void testThatBrushOptionsPreferencesNewInstanceReturnsNotNullBrushOptionsPreferences() {
        Assertions.assertThat(BrushOptionsPreferences.newInstance()).isNotNull();
    }

    @Test
    public void testThatBrushOptionsPreferencesNewInstanceReturnsANewInstance() {
        BrushOptionsPreferences firstInstance = BrushOptionsPreferences.newInstance();
        BrushOptionsPreferences secondInstance = BrushOptionsPreferences.newInstance();

        Assertions.assertThat(firstInstance).isNotEqualTo(secondInstance);
    }
}