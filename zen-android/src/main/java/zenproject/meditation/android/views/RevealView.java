package zenproject.meditation.android.views;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.graphics.Point;
import android.util.AttributeSet;
import android.view.View;

import zenproject.meditation.android.views.drawable.RevealDrawable;

public class RevealView extends View {
    private RevealDrawable revealDrawable;

    public RevealView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public RevealView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onFinishInflate() {
        revealDrawable = RevealDrawable.newInstance(getContext());
        setBackground(revealDrawable);
    }

    public void startRevealWith(final Animator.AnimatorListener animatorListener) {
        revealDrawable.startRadiusAnimation(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                show();
                animatorListener.onAnimationStart(animation);
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                animatorListener.onAnimationEnd(animation);
                revealDrawable.startAlphaAnimation(new AnimatorListenerAdapter() {
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

    public void setRevealOrigin(Point origin) {
        revealDrawable.setOrigin(origin);

    }

    public void setRevealRadius(float radius) {
        revealDrawable.setRadius(radius);
    }

    public boolean isRevealing() {
        return getVisibility() == VISIBLE;
    }
}
