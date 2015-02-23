package zenproject.meditation.android.views.creators;

import android.content.Context;

import zenproject.meditation.android.ContextRetriever;
import zenproject.meditation.android.R;
import zenproject.meditation.android.views.MenuBackgroundView;

public class MenuBackgroundCreator {
    private final static int COLOR = ContextRetriever.INSTANCE.getCurrentContext().getResources().getColor(R.color.colorPrimaryDark);

    static MenuBackgroundView createWith(Context context) {
        return new MenuBackgroundView.Builder(context)
                .withColor(COLOR)
                .create();
    }
}
