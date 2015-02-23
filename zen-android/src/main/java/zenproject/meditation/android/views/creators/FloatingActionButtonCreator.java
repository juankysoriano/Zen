package zenproject.meditation.android.views.creators;

import android.content.Context;
import android.support.annotation.DrawableRes;
import android.support.annotation.IdRes;
import android.view.Gravity;

import zenproject.meditation.android.ContextRetriever;
import zenproject.meditation.android.R;
import zenproject.meditation.android.views.FloatingActionButton;

public class FloatingActionButtonCreator {
    private final static int BUTTON_SIZE = ContextRetriever.INSTANCE.getCurrentContext().getResources().getDimensionPixelSize(R.dimen.red_action_button_size);
    private final static int MARGIN = ContextRetriever.INSTANCE.getCurrentContext().getResources().getDimensionPixelOffset(R.dimen.action_button_margin);
    private final static int COLOR = ContextRetriever.INSTANCE.getCurrentContext().getResources().getColor(R.color.colorPrimary);

    static FloatingActionButton createWith(Context context, @DrawableRes int drawableId, @IdRes int resId) {
        return new FloatingActionButton.Builder(context)
                .withButtonSize(BUTTON_SIZE)
                .withMargins(MARGIN, MARGIN, MARGIN, MARGIN)
                .withButtonColor(COLOR)
                .withGravity(Gravity.TOP | Gravity.END)
                .withDrawable(ContextRetriever.INSTANCE.getCurrentContext().getResources().getDrawable(drawableId))
                .withId(resId)
                .create();
    }
}
