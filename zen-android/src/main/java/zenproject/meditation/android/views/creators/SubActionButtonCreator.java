package zenproject.meditation.android.views.creators;

import android.content.Context;
import android.support.annotation.ColorRes;
import android.support.annotation.DrawableRes;
import android.support.annotation.IdRes;
import android.view.Gravity;

import zenproject.meditation.android.ContextRetriever;
import zenproject.meditation.android.R;
import zenproject.meditation.android.views.FloatingActionButton;

public class SubActionButtonCreator {
    private static final int SIZE = ContextRetriever.INSTANCE.getCurrentContext().getResources().getDimensionPixelSize(R.dimen.blue_sub_action_button_size);
    private static final int MARGIN = ContextRetriever.INSTANCE.getCurrentContext().getResources().getDimensionPixelSize(R.dimen.blue_sub_action_button_content_margin);

    public static FloatingActionButton createFrom(Context context, @ColorRes int color, @DrawableRes int drawableId, @IdRes int resId) {
        return new zenproject.meditation.android.views.FloatingActionButton.Builder(context)
                .withButtonSize(SIZE)
                .withMargins(MARGIN, MARGIN, MARGIN, MARGIN)
                .withButtonColor(ContextRetriever.INSTANCE.getCurrentContext().getResources().getColor(color))
                        .withGravity(Gravity.TOP | Gravity.END)
                        .withDrawable(ContextRetriever.INSTANCE.getCurrentContext().getResources().getDrawable(drawableId))
                        .withId(resId)
                        .create();
    }
}
