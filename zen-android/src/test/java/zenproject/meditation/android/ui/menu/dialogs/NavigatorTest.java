package zenproject.meditation.android.ui.menu.dialogs;

import android.app.FragmentManager;

import org.fest.assertions.api.Assertions;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import zenproject.meditation.android.ContextRetriever;
import zenproject.meditation.android.ZenTestBase;
import zenproject.meditation.android.activities.ZenActivity;
import zenproject.meditation.android.ui.menu.dialogs.brush.BrushOptionsDialog;
import zenproject.meditation.android.ui.menu.dialogs.flower.FlowerOptionsDialog;

import static org.mockito.Mockito.any;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class NavigatorTest extends ZenTestBase {
    @Mock
    private BrushOptionsDialog brushOptionsDialog;
    @Mock
    private FlowerOptionsDialog flowerOptionsDialog;
    @Mock
    private FragmentManager fragmentManager;
    @Mock
    private ZenActivity zenActivity;

    private Navigator navigator;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        navigator = new Navigator(fragmentManager, brushOptionsDialog, flowerOptionsDialog);
    }

    @Test
    public void testThatWhenOpenBrushSelectionDialogIfDialogNotAlreadyAddedThenShowWillBeCalled() {
        navigator.openBrushSelectionDialog();

        verify(brushOptionsDialog).show(fragmentManager, BrushOptionsDialog.TAG);
    }

    @Test
    public void testThatWhenOpenBrushSelectionDialogIfDialogAlreadyIsNotAddedThenShowWillNotBeCalled() {
        givenThatBrushOptionsDialogIsAdded();

        navigator.openBrushSelectionDialog();

        verify(brushOptionsDialog, never()).show(any(FragmentManager.class), anyString());
    }

    private void givenThatBrushOptionsDialogIsAdded() {
        when(brushOptionsDialog.isAdded()).thenReturn(true);
    }

    @Test
    public void testThatWhenOpenFlowerSelectionDialogIfDialogNotAlreadyAddedThenShowWillBeCalled() {
        navigator.openFlowerSelectionDialog();

        verify(flowerOptionsDialog).show(fragmentManager, FlowerOptionsDialog.TAG);
    }

    @Test
    public void testThatWhenOpenFlowerSelectionDialogIfDialogAlreadyIsNotAddedThenShowWillNotBeCalled() {
        givenThatFlowerOptionsDialogIsAdded();

        navigator.openFlowerSelectionDialog();

        verify(flowerOptionsDialog, never()).show(any(FragmentManager.class), anyString());
    }

    private void givenThatFlowerOptionsDialogIsAdded() {
        when(flowerOptionsDialog.isAdded()).thenReturn(true);
    }

    @Test
    public void testThatNewInstanceReturnsNotNullNavigator() {
        givenThatHasActivity();

        Assertions.assertThat(Navigator.newInstance()).isNotNull();
    }

    @Test
    public void testThatNewInstanceReturnsANewInstance() {
        givenThatHasActivity();

        Navigator firstInstance = Navigator.newInstance();
        Navigator secondInstance = Navigator.newInstance();

        Assertions.assertThat(firstInstance).isNotEqualTo(secondInstance);
    }

    private void givenThatHasActivity() {
        ContextRetriever.INSTANCE.inject(zenActivity);
    }
}
