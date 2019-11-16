package zenproject.meditation.android.preferences;

import android.content.SharedPreferences;

import org.fest.assertions.api.Assertions;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import zenproject.meditation.android.ZenTestBase;
import zenproject.meditation.android.sketch.painting.flowers.Flower;

import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.*;

@RunWith(RobolectricLauncherGradleTestRunner.class)

public class FlowerOptionsPreferencesTest extends ZenTestBase {
    private static final String PREF_NAME = "FlowerOptionPreferences";
    private static final String PREF_FLOWER = PREF_NAME + "Flower";
    private static final Flower ANY_FLOWER = Flower.POPPY;

    @Mock
    private SharedPreferences sharedPreferences;
    @Mock
    private SharedPreferences.Editor editor;

    private FlowerOptionPreferences flowerOptionPreferences;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        when(sharedPreferences.edit()).thenReturn(editor);
        when(editor.putInt(anyString(), anyInt())).thenReturn(editor);

        flowerOptionPreferences = new FlowerOptionPreferences(sharedPreferences);
    }

    @Test
    public void testThatApplyFlowerPutsValuesInEditor() {
        flowerOptionPreferences.applyFlower(ANY_FLOWER);

        verify(editor).putInt(PREF_FLOWER, ANY_FLOWER.ordinal());
    }

    @Test
    public void testThatApplyFlowerAppliesChanges() {
        flowerOptionPreferences.applyFlower(ANY_FLOWER);

        verify(editor).apply();
    }

    @Test
    public void testThatGetFlowerReturnsGetsValueFromSharedPreferences() {
        givenThatFlowerWasApplied();

        Assertions.assertThat(flowerOptionPreferences.getFlower()).isEqualTo(ANY_FLOWER);
    }

    private void givenThatFlowerWasApplied() {
        when(sharedPreferences.getInt(eq(PREF_FLOWER), anyInt())).thenReturn(ANY_FLOWER.ordinal());
    }


    @Test
    public void testThatFlowerOptionsPreferencesNewInstanceReturnsNotNullFlowerOptionsPreferences() {
        Assertions.assertThat(FlowerOptionPreferences.newInstance()).isNotNull();
    }

    @Test
    public void testThatFlowerOptionsPreferencesNewInstanceReturnsANewInstance() {
        FlowerOptionPreferences firstInstance = FlowerOptionPreferences.newInstance();
        FlowerOptionPreferences secondInstance = FlowerOptionPreferences.newInstance();

        Assertions.assertThat(firstInstance).isNotEqualTo(secondInstance);
    }
}
