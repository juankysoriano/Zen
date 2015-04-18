package zenproject.meditation.android.ui.menu.buttons;

import android.support.annotation.IdRes;

public enum MenuButton {
    BRUSH(1),
    RESTART(2),
    MUSIC(3),
    FLOWER(4),
    SCREENSHOT(5),
    SHARE(6),
    MENU(7);

    private final int id;

    MenuButton(@IdRes int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }
}
