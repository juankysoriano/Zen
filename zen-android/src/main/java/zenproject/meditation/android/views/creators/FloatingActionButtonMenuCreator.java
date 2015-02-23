package zenproject.meditation.android.views.creators;

import android.content.Context;
import android.support.annotation.IdRes;

import com.oguzdev.circularfloatingactionmenu.library.CircularMenu;

import zenproject.meditation.android.ContextRetriever;
import zenproject.meditation.android.R;

public class FloatingActionButtonMenuCreator {
    private static final int MENU_RADIUS = ContextRetriever.INSTANCE.getCurrentContext().getResources().getDimensionPixelSize(R.dimen.red_action_menu_radius);

    public static CircularMenu createWith(Context context) {
        return new CircularMenu.Builder(context)
                .setRadius(MENU_RADIUS)
                .addSubActionView(SubActionButtonCreator.createFrom(context, R.color.colorAccent, R.drawable.brush, MenuId.BRUSH_ID))
                .addSubActionView(SubActionButtonCreator.createFrom(context, R.color.colorAccent, R.drawable.canvas, MenuId.CANVAS_ID))
                .addSubActionView(SubActionButtonCreator.createFrom(context, R.color.colorAccent, R.drawable.music, MenuId.MUSIC_ID))
                .addSubActionView(SubActionButtonCreator.createFrom(context, R.color.colorAccentLight, R.drawable.share, MenuId.SHARE_ID))
                .addSubActionView(SubActionButtonCreator.createFrom(context, R.color.colorAccentLight, R.drawable.save, MenuId.SAVE_ID))
                .addSubActionView(SubActionButtonCreator.createFrom(context, R.color.colorPrimary, R.drawable.restart, MenuId.RESTART_ID))
                .setStartAngle(185)
                .setEndAngle(85)
                .attachTo(MenuButtonCreator.createWith(context, R.drawable.menu, MenuId.MENU_ID))
                .build();
    }

    public interface MenuId {
        @IdRes
        static final int BRUSH_ID = 1;
        @IdRes
        static final int RESTART_ID = 2;
        @IdRes
        static final int MUSIC_ID = 3;
        @IdRes
        static final int CANVAS_ID = 4;
        @IdRes
        static final int SAVE_ID = 5;
        @IdRes
        static final int SHARE_ID = 6;
        @IdRes
        static final int MENU_ID = 7;
    }
}
