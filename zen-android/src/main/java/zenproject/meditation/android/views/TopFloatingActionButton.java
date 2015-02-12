package zenproject.meditation.android.views;

import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.animation.AccelerateDecelerateInterpolator;

import com.melnykov.fab.FloatingActionButton;
import com.nineoldandroids.view.ViewPropertyAnimator;

public class TopFloatingActionButton implements View.OnClickListener {
    private final static long TRANSLATE_DURATION_MILLIS = 200;
    private final FloatingActionButton floatingActionButton;

    public static TopFloatingActionButton from(FloatingActionButton floatingActionButton) {
        return new TopFloatingActionButton(floatingActionButton);
    }

    protected TopFloatingActionButton(FloatingActionButton floatingActionButton) {
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

    @Override
    public void onClick(View v) {
        ViewAnimationUtils.createCircularReveal(v, (int) v.getX(), (int) v.getY(), 0f, 500f);
    }
}
