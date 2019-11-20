package zenproject.meditation.android.ui.menu;

import com.oguzdev.circularfloatingactionmenu.library.CircularMenu;

import org.fest.assertions.api.Assertions;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import zenproject.meditation.android.ZenTestBase;
import zenproject.meditation.android.ui.menu.buttons.FloatingActionButton;
import zenproject.meditation.android.ui.menu.buttons.MenuButton;
import zenproject.meditation.android.ui.sketch.ZenSketchView;

import static org.mockito.Mockito.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class ZenMenuTest extends ZenTestBase {
    @Mock
    private ZenSketchView zenSketchView;
    @Mock
    private CircularMenu circularMenu;
    @Mock
    private FloatingActionButton menuButton;

    private ZenMenu zenMenu;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        zenMenu = new ZenMenu(zenSketchView);
        when(zenSketchView.getCircularMenu()).thenReturn(circularMenu);
        when(circularMenu.getActionView()).thenReturn(menuButton);
    }

    @Test
    public void testThatToggleDelegatesOnCircularMenu() {
        zenMenu.toggle();

        verify(circularMenu).toggle(true);
    }

    @Test
    public void testThatToggleRotatesMenuButton() {
        zenMenu.toggle();

        verify(menuButton).rotate();
    }

    @Test
    public void testThatOnPaintingStartDoesPostRunnable() {
        zenMenu.onPaintingStart();

        verify(zenSketchView).post(any(Runnable.class));
    }

    @Test
    public void testThatOnPaintingStopPostsRunnable() {
        zenMenu.onPaintingStop();

        verify(zenSketchView).post(any(Runnable.class));
    }

    @Test
    public void testThatGetButtonViewForReturnsMenuButtonIfItIsRequested() {
        Assertions.assertThat(zenMenu.getButtonViewFor(MenuButton.MENU)).isEqualTo(menuButton);
    }

    @Test
    public void testThatGetButtonViewForFindsButtonIfRequestedIsNotMenu() {
        zenMenu.getButtonViewFor(MenuButton.SHARE);

        verify(circularMenu).findSubActionViewWithId(MenuButton.SHARE.getId());
    }

    @Test
    public void testThatOnMenuOpenedHidesMenuButtonIfItIsClosedAndIsPainting() {
        givenThatMenuIsClosed();
        zenMenu.onPaintingStart();
        zenMenu.onMenuOpened(circularMenu);

        verify(menuButton, times(1)).hide();
    }

    @Test
    public void testThatOnMenuOpenedTogglesMenuButtonIfItIsOpenedAndIsPainting() {
        givenThatMenuIsOpened();
        zenMenu.onPaintingStart();
        zenMenu.onMenuOpened(circularMenu);

        verify(circularMenu, times(1)).toggle(true);
    }

    @Test
    public void testThatOnMenuOpenedStartRotatesMenuButtonIfItIsOpenedAndIsPainting() {
        givenThatMenuIsOpened();
        zenMenu.onPaintingStart();
        zenMenu.onMenuOpened(circularMenu);

        verify(menuButton, times(1)).rotate();
    }

    @Test
    public void testThatOnMenuOpenedDoesNothingIfNotPainting() {
        givenThatMenuIsOpened();
        zenMenu.onPaintingStop();
        zenMenu.onMenuOpened(circularMenu);

        verify(circularMenu, never()).toggle(true);
        verify(menuButton, never()).rotate();
        verify(menuButton, never()).hide();
    }

    @Test
    public void testThatOnMenuClosedDoesNothingIfNotPainting() {
        zenMenu.onPaintingStop();
        zenMenu.onMenuClosed(circularMenu);

        verify(menuButton, never()).hide();
    }

    @Test
    public void testThatOnMenuClosedHidesMenuButtonIfPainting() {
        zenMenu.onPaintingStart();
        zenMenu.onMenuClosed(circularMenu);

        verify(menuButton, times(1)).hide();
    }

    @Test
    public void testThatNewInstanceReturnsNotNullInstance() {
        Assertions.assertThat(ZenMenu.newInstance(zenSketchView)).isNotNull();
    }

    @Test
    public void testThatNewInstanceReturnsANewInstance() {
        ZenMenu firstInstance = ZenMenu.newInstance(zenSketchView);
        ZenMenu secondInstance = ZenMenu.newInstance(zenSketchView);

        Assertions.assertThat(firstInstance).isNotEqualTo(secondInstance);
    }

    private void givenThatMenuIsClosed() {
        when(circularMenu.isOpen()).thenReturn(false);
    }

    private void givenThatMenuIsOpened() {
        when(circularMenu.isOpen()).thenReturn(true);
    }
}
