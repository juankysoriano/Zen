package zenproject.meditation.android.views.reveal;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.view.animation.AccelerateDecelerateInterpolator;

import zenproject.meditation.android.R;

public class RevealDrawable extends Drawable {

    private static final float CONCEALED = 0f;
    private static final float REVEALED = 1f;
    private static final int OPAQUE = 255;
    private static final int TRANSPARENT = 0;
    private static final int REVEAL_DURATION = 500;
    private final Paint revealPaint;
    private float radius;
    private Point origin;
    protected float revealScale;
    protected int revealAlpha;

    public static RevealDrawable newInstance(Context context) {
        Paint revealPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        int color = context.getResources().getColor(R.color.colorAccent);
        revealPaint.setColor(color);
        revealPaint.setStyle(Paint.Style.FILL);
        return new RevealDrawable(revealPaint);
    }

    protected RevealDrawable(Paint paint) {
        this.revealPaint = paint;
    }

    @Override
    public void draw(Canvas canvas) {
        canvas.drawCircle(origin.x, origin.y, radius * revealScale, revealPaint);
    }

    @Override
    public void setAlpha(int alpha) {
        revealAlpha = alpha;
        revealPaint.setAlpha(alpha);
        invalidateSelf();
    }

    @Override
    public int getAlpha() {
        return revealAlpha;
    }

    @Override
    public void setColorFilter(ColorFilter cf) {
        revealPaint.setColorFilter(cf);
    }

    public void setRadius(float radius) {
        this.radius = radius;
    }

    public void setOrigin(Point origin) {
        this.origin = origin;
    }

    @Override
    public int getOpacity() {
        return revealPaint.getAlpha();
    }

    protected float getScale() {
        return revealScale;
    }

    protected void setScale(float revealScale) {
        this.revealScale = revealScale;
        invalidateSelf();
    }

    public void startRadiusAnimation(Animator.AnimatorListener animatorListener) {
        setAlpha(OPAQUE);

        Animator animator = generateRadiusAnimation();
        animator.addListener(animatorListener);
        animator.setDuration(REVEAL_DURATION);
        animator.setInterpolator(new AccelerateDecelerateInterpolator());
        animator.start();
    }

    public void startAlphaAnimation(Animator.AnimatorListener animatorListener) {
        Animator animator = generateAlphaAnimation();
        animator.addListener(animatorListener);
        animator.setInterpolator(new AccelerateDecelerateInterpolator());
        animator.start();
    }

    private Animator generateRadiusAnimation() {
        return ObjectAnimator.ofFloat(this, "scale", CONCEALED, REVEALED);
    }

    private Animator generateAlphaAnimation() {
        return ObjectAnimator.ofInt(this, "alpha", OPAQUE, TRANSPARENT);
    }
}
