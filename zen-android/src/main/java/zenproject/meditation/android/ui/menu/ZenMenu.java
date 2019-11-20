package zenproject.meditation.android.ui.menu;

import com.oguzdev.circularfloatingactionmenu.library.CircularMenu;

import zenproject.meditation.android.sketch.ZenSketch;
import zenproject.meditation.android.ui.menu.buttons.FloatingActionButton;
import zenproject.meditation.android.ui.menu.buttons.MenuButton;
import zenproject.meditation.android.ui.sketch.ZenSketchView;

public class ZenMenu implements CircularMenu.MenuStateChangeListener, ZenSketch.PaintListener {

    private final ZenSketchView zenSketchView;
    private boolean isPainting;

    ZenMenu(ZenSketchView zenSketchView) {
        this.zenSketchView = zenSketchView;
    }

    public static ZenMenu newInstance(ZenSketchView zenSketchView) {
        return new ZenMenu(zenSketchView);
    }

    public void toggle() {
        getCircularMenu().toggle(true);
        getMenuButton().rotate();
    }

    private FloatingActionButton getMenuButton() {
        return getButtonViewFor(MenuButton.MENU);
    }

    private CircularMenu getCircularMenu() {
        return zenSketchView.getCircularMenu();
    }

    public FloatingActionButton getButtonViewFor(MenuButton menuButton) {
        switch (menuButton) {
            case MENU:
                return (FloatingActionButton) getCircularMenu().getActionView();
            default:
                return (FloatingActionButton) getCircularMenu().findSubActionViewWithId(menuButton.getId());
        }
    }

    private void hide() {
        if (getCircularMenu().isOpen()) {
            getCircularMenu().toggle(true);
            getMenuButton().rotate();
        } else {
            getMenuButton().hide();
        }
    }

    private void show() {
        getMenuButton().show();
    }

    @Override
    public void onMenuOpened(CircularMenu circularMenu) {
        if (isPainting) {
            hide();
        }
    }

    @Override
    public void onMenuClosed(CircularMenu circularMenu) {
        if (isPainting) {
            getMenuButton().hide();
        }
    }

    @Override
    public void onPaintingStart() {
        isPainting = true;
        zenSketchView.post(this::hide);
    }

    @Override
    public void onPaintingStop() {
        isPainting = false;
        zenSketchView.post(this::show);
    }
}
