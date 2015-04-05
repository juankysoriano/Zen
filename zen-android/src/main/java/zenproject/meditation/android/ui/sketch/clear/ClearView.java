package zenproject.meditation.android.ui.sketch.clear;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.graphics.Point;
import android.util.AttributeSet;
import android.view.View;

public class ClearView extends View {
    private ClearDrawable clearDrawable;

    public ClearView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ClearView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onFinishInflate() {
        clearDrawable = ClearDrawable.newInstance(getContext());
        setBackground(clearDrawable);
    }

    public void startClearWith(final Animator.AnimatorListener animatorListener) {
        clearDrawable.startRadiusAnimation(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                show();
                animatorListener.onAnimationStart(animation);
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                animatorListener.onAnimationEnd(animation);
                clearDrawable.startAlphaAnimation(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        hide();
                    }
                });
            }

            @Override
            public void onAnimationCancel(Animator animation) {
                animatorListener.onAnimationCancel(animation);
            }

            @Override
            public void onAnimationRepeat(Animator animation) {
                animatorListener.onAnimationRepeat(animation);
            }
        });
    }

    private void show() {
        setVisibility(VISIBLE);
    }

    private void hide() {
        setVisibility(GONE);
    }

    public void setClearOrigin(Point origin) {
        clearDrawable.setOrigin(origin);

    }

    public void setClearRadius(float radius) {
        clearDrawable.setRadius(radius);
    }

    public boolean isClearing() {
        return getVisibility() == VISIBLE;
    }
}
