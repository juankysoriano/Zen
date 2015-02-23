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
                .addSubActionView(SubActionButtonCreator.createFrom(context, R.drawable.eraser, MenuIds.ERASER_ID))
                .addSubActionView(SubActionButtonCreator.createFrom(context, R.drawable.brush, MenuIds.BRUSH_ID))
                .addSubActionView(SubActionButtonCreator.createFrom(context, R.drawable.bunch_flowers, MenuIds.FLOWERS_ID))
                .addSubActionView(SubActionButtonCreator.createFrom(context, R.drawable.canvas, MenuIds.CANVAS_ID))
                .addSubActionView(SubActionButtonCreator.createFrom(context, R.drawable.music, MenuIds.MUSIC_ID))
                .setStartAngle(180)
                .setEndAngle(90)
                .attachTo(FloatingActionButtonCreator.createWith(context, R.drawable.music, MenuIds.RESET_ID))
                .build();
    }

    public interface MenuIds {
        @IdRes
        static final int BRUSH_ID = 0;
        @IdRes
        static final int ERASER_ID = 1;
        @IdRes
        static final int RESET_ID = 2;
        @IdRes
        static final int FLOWERS_ID = 3;
        @IdRes
        static final int MUSIC_ID = 4;
        @IdRes
        static final int CANVAS_ID = 5;
        @IdRes
        static final int SAVE_ID = 6;
    }
}
