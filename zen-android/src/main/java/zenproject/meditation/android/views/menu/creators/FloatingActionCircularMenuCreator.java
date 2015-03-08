package zenproject.meditation.android.views.menu.creators;

import android.content.Context;
import android.support.annotation.DrawableRes;
import android.support.annotation.IdRes;
import android.view.Gravity;

import com.oguzdev.circularfloatingactionmenu.library.CircularMenu;

import zenproject.meditation.android.ContextRetriever;
import zenproject.meditation.android.R;
import zenproject.meditation.android.views.menu.FloatingActionButton;

public class FloatingActionCircularMenuCreator {
    private static final int MENU_RADIUS = ContextRetriever.INSTANCE.getCurrentContext().getResources().getDimensionPixelSize(R.dimen.red_action_menu_radius);
    private final static int BUTTON_SIZE = ContextRetriever.INSTANCE.getCurrentContext().getResources().getDimensionPixelSize(R.dimen.red_action_button_size);
    private final static int MARGIN = ContextRetriever.INSTANCE.getCurrentContext().getResources().getDimensionPixelOffset(R.dimen.action_button_margin);
    private final static int MENU_COLOR = ContextRetriever.INSTANCE.getCurrentContext().getResources().getColor(R.color.colorAccent);

    public static CircularMenu createWith(Context context) {
        return new CircularMenu.Builder(context)
                .setRadius(MENU_RADIUS)
                .addSubActionView(SubActionButtonCreator.createFrom(context, R.color.colorPrimary, R.drawable.brush, MenuId.BRUSH_ID))
                .addSubActionView(SubActionButtonCreator.createFrom(context, R.color.colorPrimary, R.drawable.canvas, MenuId.CANVAS_ID))
                .addSubActionView(SubActionButtonCreator.createFrom(context, R.color.colorPrimary, R.drawable.music, MenuId.MUSIC_ID))
                .addSubActionView(SubActionButtonCreator.createFrom(context, R.color.colorPrimaryLight, R.drawable.share, MenuId.SHARE_ID))
                .addSubActionView(SubActionButtonCreator.createFrom(context, R.color.colorPrimaryLight, R.drawable.save, MenuId.SAVE_ID))
                .addSubActionView(SubActionButtonCreator.createFrom(context, R.color.colorAccent, R.drawable.restart, MenuId.RESTART_ID))
                .setStartAngle(185)
                .setEndAngle(85)
                .attachTo(createMenuButtonWith(context, R.drawable.menu, MenuId.MENU_ID))
                .build();
    }

    private static FloatingActionButton createMenuButtonWith(Context context, @DrawableRes int drawableId, @IdRes int resId) {
        return new FloatingActionButton.Builder(context)
                .withButtonSize(BUTTON_SIZE)
                .withMargins(MARGIN, MARGIN, MARGIN, MARGIN)
                .withButtonColor(MENU_COLOR)
                .withGravity(Gravity.TOP | Gravity.END)
                .withDrawable(ContextRetriever.INSTANCE.getCurrentContext().getResources().getDrawable(drawableId))
                .withId(resId)
                .create();
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
