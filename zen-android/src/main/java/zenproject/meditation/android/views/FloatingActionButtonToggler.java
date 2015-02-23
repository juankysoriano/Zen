package zenproject.meditation.android.views;

import android.graphics.Point;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;

import com.nineoldandroids.view.ViewPropertyAnimator;

public class FloatingActionButtonToggler {
    private final static long TRANSLATE_DURATION_MILLIS = 200;
    private final FloatingActionButton floatingActionButton;

    public static FloatingActionButtonToggler from(FloatingActionButton floatingActionButton) {
        return new FloatingActionButtonToggler(floatingActionButton);
    }

    protected FloatingActionButtonToggler(FloatingActionButton floatingActionButton) {
        this.floatingActionButton = floatingActionButton;
    }

    public void hide() {
        ViewPropertyAnimator.animate(floatingActionButton).setInterpolator(new AccelerateDecelerateInterpolator())
                .setDuration(TRANSLATE_DURATION_MILLIS)
                .translationY(-floatingActionButton.getBottom());
    }

    public void show() {
        ViewPropertyAnimator.animate(floatingActionButton).setInterpolator(new AccelerateDecelerateInterpolator())
                .setDuration(TRANSLATE_DURATION_MILLIS)
                .translationY(0);
    }

    public void setOnClickListener(View.OnClickListener listener) {
        floatingActionButton.setOnClickListener(listener);
    }

    public Point getCentre() {
        return new Point((int) floatingActionButton.getX() + floatingActionButton.getWidth() / 2,
                (int) floatingActionButton.getY() + floatingActionButton.getHeight() / 2);
    }
}
