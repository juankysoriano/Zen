package zenproject.meditation.android.ui.menu.buttons.creators;

import android.content.Context;
import android.support.annotation.ColorRes;
import android.support.annotation.DrawableRes;
import android.support.annotation.IdRes;
import android.support.v4.content.ContextCompat;
import android.view.Gravity;

import zenproject.meditation.android.ContextRetriever;
import zenproject.meditation.android.R;
import zenproject.meditation.android.ui.menu.buttons.FloatingActionButton;

public abstract class SubActionButtonCreator {
    private static final int SIZE = ContextRetriever.INSTANCE.getResources().getDimensionPixelSize(R.dimen.sub_floating_action_button_size);
    private static final int MARGIN = ContextRetriever.INSTANCE.getResources().getDimensionPixelSize(R.dimen.sub_floating_action_button_margin);

    public static FloatingActionButton createFrom(Context context, @ColorRes int color, @DrawableRes int drawableId, @IdRes int resId) {
        return new FloatingActionButton.Builder(context)
                .withButtonSize(SIZE)
                .withMargins(MARGIN, MARGIN, MARGIN, MARGIN)
                .withButtonColor(ContextRetriever.INSTANCE.getCurrentContext().getResources().getColor(color))
                .withGravity(Gravity.TOP | Gravity.END)
                .withDrawable(ContextCompat.getDrawable(ContextRetriever.INSTANCE.getCurrentContext(), drawableId))
                .withId(resId)
                .create();
    }
}
