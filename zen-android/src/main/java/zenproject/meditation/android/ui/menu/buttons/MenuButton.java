package zenproject.meditation.android.ui.menu.buttons;

import zenproject.meditation.android.R;

public enum MenuButton {
    BRUSH(R.id.button_brush),
    RESTART(R.id.button_restart),
    MUSIC(R.id.button_music),
    FLOWER(R.id.button_flower),
    SHARE(R.id.button_share),
    MENU(R.id.button_menu);

    private final int id;

    MenuButton(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }
}
