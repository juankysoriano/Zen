package zenproject.meditation.android.views.menu.creators;

import android.content.Context;
import android.support.annotation.DrawableRes;
import android.support.annotation.IdRes;
import android.support.v4.content.ContextCompat;
import android.view.Gravity;

import com.oguzdev.circularfloatingactionmenu.library.CircularMenu;

import zenproject.meditation.android.ContextRetriever;
import zenproject.meditation.android.R;
import zenproject.meditation.android.views.menu.FloatingActionButton;

public abstract class FloatingActionCircularMenuCreator {
    private static final int MENU_RADIUS = ContextRetriever.INSTANCE.getCurrentResources().getDimensionPixelSize(R.dimen.red_action_menu_radius);
    private static final int BUTTON_SIZE = ContextRetriever.INSTANCE.getCurrentResources().getDimensionPixelSize(R.dimen.red_action_button_size);
    private static final int MARGIN = ContextRetriever.INSTANCE.getCurrentResources().getDimensionPixelOffset(R.dimen.action_button_margin);
    private static final int MENU_COLOR = ContextRetriever.INSTANCE.getCurrentResources().getColor(R.color.colorAccent);

    public static CircularMenu createWith(Context context) {
        return new CircularMenu.Builder(context)
                .setRadius(MENU_RADIUS)
                .addSubActionView(SubActionButtonCreator.createFrom(context, R.color.colorPrimary, R.drawable.brush, MenuId.BRUSH_ID))
                .addSubActionView(SubActionButtonCreator.createFrom(context, R.color.colorPrimary, R.drawable.bunch_flowers, MenuId.FLOWERS_ID))
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
                .withDrawable(ContextCompat.getDrawable(ContextRetriever.INSTANCE.getCurrentContext(), drawableId))
                .withId(resId)
                .create();
    }

    public interface MenuId {
        @IdRes
        int BRUSH_ID = 1;
        @IdRes
        int RESTART_ID = 2;
        @IdRes
        int MUSIC_ID = 3;
        @IdRes
        int FLOWERS_ID = 4;
        @IdRes
        int SAVE_ID = 5;
        @IdRes
        int SHARE_ID = 6;
        @IdRes
        int MENU_ID = 7;
    }
}
